package com.balugaq.variousclutter.core.listeners;

import com.balugaq.variousclutter.VariousClutter;
import com.balugaq.variousclutter.implementation.PortalFrame;
import com.balugaq.variousclutter.utils.Debug;
import com.balugaq.variousclutter.utils.SlimefunItemUtil;
import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Orientable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class SpecialPortalCreateListener implements Listener {
    public static final BlockFace[] DIRECTIONS_3D = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};
    public static final int DISTANCE_LIMIT = 30;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSpecialPortalCreate(BlockPhysicsEvent event) {
        if (!isFire(event.getChangedType())) {
            return;
        }

        Block fire = event.getBlock();
        for (BlockFace face : DIRECTIONS_3D) {
            Block portalFrame = fire.getRelative(face);
            if (isAllowedPortalFrame(portalFrame)) {
                if (tryCreatePortal(null, fire, face)) {
                    return;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSpecialPortalCreate(EntityChangeBlockEvent event) {
        Entity entity = event.getEntity();
        if (!isFire(event.getTo())) {
            Debug.debug("To block is not fire [Code 14]");
            return;
        }

        Block block = event.getBlock();
        if (block == null) {
            Debug.debug("Block is null [Code 15]");
            return;
        }

        for (BlockFace face : DIRECTIONS_3D) {
            if (tryCreatePortal(entity, block, face)) {
                return;
            }
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void opSpecialPortalCreate(BlockDispenseEvent event)  {
        Block block = event.getBlock();
        if (!(block.getBlockData() instanceof Directional directional)) {
            Debug.debug("Block is not directional [Code 12]");
            return;
        }
        BlockFace facing = directional.getFacing();
        Block fire = block.getRelative(facing);
        if (!isFire(fire)) {
            Debug.debug("Fire block is not found [Code 13]");
            return;
        }

        if (event.getItem().getType() == Material.FLINT_AND_STEEL) {
            for (BlockFace face : DIRECTIONS_3D) {
                Block portalFrame = block.getRelative(face);
                if (isAllowedPortalFrame(portalFrame)) {
                    if (tryCreatePortal(null, fire, face)) {
                        return;
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void opSpecialPortalCreate(BlockSpreadEvent event) {
        Block source = event.getSource();
        if (!isFire(source)) {
            Debug.debug("Source block is not fire [Code 6]");
            return;
        }
        Block block = event.getBlock();
        BlockState newState = event.getNewState();
        boolean originCancelled = event.isCancelled();
        // Just cancel the event to prevent the fire spread
        event.setCancelled(true);
        if (isFire(newState.getType())) {
            Block fire = newState.getBlock();
            for (BlockFace face : DIRECTIONS_3D) {
                Block portalFrame = block.getRelative(face);
                if (isAllowedPortalFrame(portalFrame)) {
                    if (tryCreatePortal(null, fire, face)) {
                        return;
                    }
                }
            }
        }

        // Re-set the event when we can't create a portal
        event.setCancelled(originCancelled);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSpecialPortalCreate(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (player == null) {
            Debug.debug("Player is null [Code 7]");
            return;
        }

        Block against = event.getBlockAgainst();
        if (against == null || !isAllowedPortalFrame(against)) {
            Debug.debug("Against block is not portal frame [Code 8]");
            return;
        }

        Block fire = event.getBlockPlaced();
        if (fire == null || !isFire(fire)) {
            Debug.debug("Placed block is not fire [Code 9]");
            return;
        }

        BlockFace face = fire.getFace(against);
        if (face == null) {
            Debug.debug("Face is null [Code 10]");
            return;
        }

        Bukkit.getScheduler().runTaskLater(VariousClutter.instance, () -> {
            tryCreatePortal(player, fire, face);
        }, 1L);
    }

    public boolean isAllowedPortalFrame(Block block) {
        return SlimefunItemUtil.getSfItem(block.getLocation(), PortalFrame.class).isPresent();
    }

    public boolean isFire(Block block) {
        return block.getType() == Material.FIRE || block.getType() == Material.SOUL_FIRE;
    }

    public boolean isFire(Material material) {
        return material == Material.FIRE || material == Material.SOUL_FIRE;
    }

    public boolean tryCreatePortal(Entity entity, Block fire, BlockFace face) {
        Location fireLocation = fire.getLocation();
        if (!isAllowedCreatePortal(fire.getLocation())) {
            Debug.debug("Portal creation cancelled by world [Code 4]");
            return false;
        }
        Debug.debug("Creating portal at " + fireLocation);
        Set<Location> portalBlocks = new HashSet<>();
        BlockFace portalFace = null;
        Set<BlockFace> faces = new HashSet<>();
        faces.add(BlockFace.UP);
        faces.add(BlockFace.DOWN);
        switch (face) {
            case NORTH, SOUTH -> {
                faces.add(BlockFace.NORTH);
                faces.add(BlockFace.SOUTH);
                portalBlocks = tryFindPortalBlocks(fire, faces);
                portalFace = BlockFace.SOUTH;
            }

            case EAST, WEST -> {
                faces.add(BlockFace.EAST);
                faces.add(BlockFace.WEST);
                portalBlocks = tryFindPortalBlocks(fire, faces);
                portalFace = BlockFace.WEST;
            }

            case UP, DOWN -> {
                // Plan A
                faces.add(BlockFace.NORTH);
                faces.add(BlockFace.SOUTH);
                portalBlocks = tryFindPortalBlocks(fire, faces);
                portalFace = BlockFace.SOUTH;

                if (portalBlocks.isEmpty()) {
                    // Plan B
                    faces.remove(BlockFace.NORTH);
                    faces.remove(BlockFace.SOUTH);

                    faces.add(BlockFace.EAST);
                    faces.add(BlockFace.WEST);
                    portalBlocks = tryFindPortalBlocks(fire, faces);
                    portalFace = BlockFace.WEST;
                }
            }
        }

        if (!portalBlocks.isEmpty()) {
            Debug.debug("Creating portal with " + portalBlocks.size() + " blocks");
            return createPortal(fire.getWorld(), entity, portalBlocks, portalFace);
        } else {
            Debug.debug("Portal creation cancelled by no blocks found [Code 0]");
        }

        Debug.debug("Portal creation failed [Code 11]");
        return false;
    }

    @NotNull
    public Set<Location> tryFindPortalBlocks(Block fire, Set<BlockFace> faces) {
        Set<Location> checked = new HashSet<>();
        Stack<Location> locations = new Stack<>();
        Location fireLocation = fire.getLocation();
        locations.push(fireLocation);
        while (!locations.isEmpty()) {
            Location origin = locations.pop();
            if (checked.contains(origin)) {
                continue;
            }
            checked.add(origin);
            Block originBlock = origin.getBlock();
            for (BlockFace checkingFace : faces) {
                Block block = originBlock.getRelative(checkingFace);
                Location location = block.getLocation();
                if (location.distance(fireLocation) > DISTANCE_LIMIT) {
                    Debug.debug("Portal creation cancelled by distance limit [Code 1]");
                    return new HashSet<>();
                }

                if (!locations.contains(block.getLocation())) {
                    Material type = block.getType();
                    if (type == Material.AIR || isFire(type)) {
                        locations.push(block.getLocation());
                    } else if (!isAllowedPortalFrame(block)) {
                        Debug.debug("Portal creation cancelled by block type [Code 2]");
                        return new HashSet<>();
                    }
                }
            }
        }

        Debug.debug("Portal creation found " + checked.size() + " blocks");
        return checked;
    }

    public boolean createPortal(@NotNull World world, @Nullable Entity entity, @NotNull Set<Location> portalBlocks, @NotNull BlockFace portalFace) {
        List<BlockState> portalBlocksList = new ArrayList<>();
        for (Location location : portalBlocks) {
            portalBlocksList.add(location.getBlock().getState());
        }

        PortalCreateEvent event = new PortalCreateEvent(portalBlocksList, world, entity, PortalCreateEvent.CreateReason.FIRE);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            Debug.debug("Portal creation cancelled by event [Code 3]");
            Debug.debug("Cancelled: " + event.isCancelled());
            return false;
        }

        Bukkit.getScheduler().runTaskLater(VariousClutter.instance, () -> {
            for (Location location : portalBlocks) {
                Block block = location.getBlock();
                block.setType(Material.NETHER_PORTAL);

                BlockData data = block.getBlockData();
                if (data instanceof Orientable orientable) {
                    orientable.setAxis(portalFace == BlockFace.NORTH || portalFace == BlockFace.SOUTH ? Axis.Z : Axis.X);
                }
                block.setBlockData(data, false);
            }
            Debug.debug("Portal created with " + portalBlocks.size() + " blocks");
            Debug.debug("Portal face: " + portalFace);
        }, 1L);

        return true;
    }

    public boolean isAllowedCreatePortal(@NotNull Location location) {
        if (location.getWorld().getName().equals("world_the_end")) {
            Debug.debug("Portal creation cancelled by end world [Code 5]");
            return false;
        }

        return true;
    }
}

