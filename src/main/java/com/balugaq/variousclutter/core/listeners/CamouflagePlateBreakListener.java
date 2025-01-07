package com.balugaq.variousclutter.core.listeners;

import com.balugaq.variousclutter.utils.Debug;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.Collection;
import java.util.List;

public class CamouflagePlateBreakListener implements Listener {
    private static void clearCamouflagePlate(Location location) {
        Debug.debug("Location: " + location);
        Collection<BlockDisplay> blockDisplays = location.getNearbyEntitiesByType(BlockDisplay.class, 1f);
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCamouflagePlateBreak(BlockBreakEvent event) {
        Debug.debug("BlockBreakEvent triggered");
        Block block = event.getBlock();
        if (block == null) {
            Debug.debug("Clicked block is null [Code 17]");
            return;
        }

        clearCamouflagePlate(block.getLocation());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCamouflagePlateBreak(BlockPistonExtendEvent event) {
        Debug.debug("BlockPistonExtendEvent triggered");
        List<Block> blocks = event.getBlocks();
        if (blocks == null) {
            Debug.debug("Blocks list is null [Code 18]");
            return;
        }

        for (Block block : blocks) {
            clearCamouflagePlate(block.getLocation());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCamouflagePlateBreak(BlockPistonRetractEvent event) {
        Debug.debug("BlockPistonRetractEvent triggered");
        List<Block> blocks = event.getBlocks();
        if (blocks == null) {
            Debug.debug("Blocks list is null [Code 19]");
            return;
        }

        for (Block block : blocks) {
            clearCamouflagePlate(block.getLocation());
        }
    }
}
