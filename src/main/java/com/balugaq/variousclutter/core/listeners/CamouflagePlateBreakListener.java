package com.balugaq.variousclutter.core.listeners;

import com.balugaq.variousclutter.VariousClutter;
import com.balugaq.variousclutter.implementation.CamouflagePlate;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.Set;

public class CamouflagePlateBreakListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChunkLoad(ChunkLoadEvent event) {
        Entity[] entities = event.getChunk().getEntities();
        for (Entity entity : entities) {
            collectDisplay(entity);
        }
    }

    public void collectDisplay(Entity entity) {
        if (entity instanceof BlockDisplay blockDisplay) {
            Set<String> tags = blockDisplay.getScoreboardTags();
            if (tags.contains(CamouflagePlate.KEY)) {
                synchronized (VariousClutter.instance.camouflagePlates) {
                    VariousClutter.instance.camouflagePlates.add(blockDisplay.getUniqueId());
                }
            }
        }
    }
}
