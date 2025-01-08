package com.balugaq.variousclutter.core.listeners;

import com.balugaq.variousclutter.VariousClutter;
import com.balugaq.variousclutter.implementation.CamouflagePlate;
import com.balugaq.variousclutter.utils.Debug;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CamouflagePlateBreakListener implements Listener {
    public static final Set<UUID> checkedWorlds = new HashSet<>();
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChunkLoad(ChunkLoadEvent event) {
        Debug.debug("ChunkLoadEvent triggered");
        Entity[] entities = event.getChunk().getEntities();
        Debug.debug("Found " + entities.length + " entities in the chunk");
        for (Entity entity : entities) {
            collectDisplay(entity);
        }
    }
    public void collectDisplay(Entity entity) {
        Debug.debug("&aEntity Type: " + entity.getType());
        if (entity instanceof BlockDisplay blockDisplay) {
            Debug.debug("&cBlock Display found");
            Debug.debug("&cLocation: " + blockDisplay.getLocation());
            Set<String> tags = blockDisplay.getScoreboardTags();
            Debug.debug("&cTags: " + Arrays.toString(tags.toArray()));
            if (tags.contains(CamouflagePlate.KEY)) {
                Debug.debug("&cCamouflage Plate found");
                synchronized (VariousClutter.instance.camouflagePlates) {
                    VariousClutter.instance.camouflagePlates.add(blockDisplay.getUniqueId());
                }
            }
        }
    }
}
