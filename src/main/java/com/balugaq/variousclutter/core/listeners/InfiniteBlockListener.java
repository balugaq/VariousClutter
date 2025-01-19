package com.balugaq.variousclutter.core.listeners;

import com.balugaq.variousclutter.VariousClutter;
import com.balugaq.variousclutter.implementation.slimefun.items.InfiniteBlock;
import com.balugaq.variousclutter.implementation.slimefun.tools.InfiniteBlockBreaker;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.events.SlimefunBlockBreakEvent;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InfiniteBlockListener implements Listener {
    @EventHandler
    public void onInfiniteBlockBreak(SlimefunBlockBreakEvent event) {
        if (SlimefunItem.getByItem(event.getHeldItem()) instanceof InfiniteBlockBreaker) {
            return;
        }

        if (event.getSlimefunItem() instanceof InfiniteBlock) {
            if (StorageCacheUtils.getData(event.getBlockBroken().getLocation(), InfiniteBlock.BS_MATERIAL_KEY) != null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInfiniteBlockTypeSet(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.isOp()) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }

        Location location = block.getLocation();
        if (StorageCacheUtils.getSfItem(location) instanceof InfiniteBlock) {
            if (StorageCacheUtils.getData(location, InfiniteBlock.BS_MATERIAL_KEY) == null) {
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                Material material = itemStack.getType();
                if (material == Material.AIR) {
                    return;
                }

                if (!material.isBlock() || !material.isSolid()) {
                    return;
                }

                block.setType(material);
                synchronized (VariousClutter.instance.infiniteBlocks) {
                    VariousClutter.instance.infiniteBlocks.put(location, material);
                }
                StorageCacheUtils.setData(location, InfiniteBlock.BS_MATERIAL_KEY, material.name());
                player.sendMessage("已设置无限方块的类型");
                event.setCancelled(true);
            }
        }
    }
}
