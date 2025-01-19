package com.balugaq.variousclutter.core.listeners;

import com.balugaq.variousclutter.implementation.slimefun.items.machines.PressureGenerator;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PressureGeneratorListener implements Listener {
    @EventHandler
    public void onPressureGeneratorGenerate(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL) {
            Player player = event.getPlayer();
            Location blockLocation = player.getLocation().toBlockLocation();
            Block block = event.getClickedBlock();
            if (block != null) {
                blockLocation = block.getLocation();
            }
            if (StorageCacheUtils.getSfItem(blockLocation) instanceof PressureGenerator pg) {
                if (pg.getCharge(blockLocation) >= pg.getCapacity()) {
                    player.sendMessage(ChatColor.RED + "[踩踏式发电机] 电量已满！");
                    return;
                }
                pg.generateEnergyAt(blockLocation);
                event.setCancelled(true);
                player.teleport(player.getLocation().clone().add(0, 0.3, 0));
                player.sendMessage(ChatColor.AQUA + "[踩踏式发电机] 正在发电...");
            }
        }
    }
}
