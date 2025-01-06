package com.balugaq.variousclutter.core.listeners;

import com.balugaq.variousclutter.utils.Debug;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.MetadataValue;
import org.joml.Vector3f;

import java.util.Collection;
import java.util.List;

public class CamouflagePlateBreakListener implements Listener {
    @EventHandler
    public void onCamouflagePlateBreak(PlayerInteractEvent event) {
        Debug.debug("PlayerInteractEvent triggered");
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) {
            Debug.debug("Not a left click block event [Code 16]");
            return;
        }

        Block block = event.getClickedBlock();
        if (block == null) {
            Debug.debug("Clicked block is null [Code 17]");
            return;
        }

        BlockFace clickedFace = event.getBlockFace();
        Vector3f offset = new Vector3f(0f, 0f, 0f);
        switch (clickedFace) {
            case UP -> {
                offset.y = 1f;
            }
            case DOWN -> {
                offset.y = 0f;
            }
            case NORTH -> {
                offset.z = 0f;
            }
            case SOUTH -> {
                offset.z = 1f;
            }
            case EAST -> {
                offset.x = 1f;
            }
            case WEST -> {
                offset.x = 0f;
            }
        }

        Location location = block.getLocation().clone().add(offset.x, offset.y, offset.z);
        Collection<BlockDisplay> blockDisplays = location.getNearbyEntitiesByType(BlockDisplay.class, 0.01d);
        Debug.debug("Found " + blockDisplays.size() + " block displays nearby");
        for (BlockDisplay blockDisplay : blockDisplays) {
            List<MetadataValue> metadata = blockDisplay.getMetadata("camouflage_plate");
            Debug.debug("Found " + metadata.size() + " metadata values of key 'camouflage_plate'");
            for (MetadataValue data : metadata) {
                if (data.asBoolean()) {
                    Debug.debug("Removing block display");
                    blockDisplay.remove();
                }
            }
        }
    }
}
