package com.balugaq.variousclutter.core.listeners;

import com.balugaq.variousclutter.implementation.slimefun.items.ReducingAgent;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import net.guizhanss.guizhanlib.minecraft.helper.inventory.ItemStackHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ReducingAgentUseListener implements Listener {
    @EventHandler
    public void onAgentUse(InventoryClickEvent event) {
        if (event.isRightClick()) {
            ItemStack currentItem = event.getCurrentItem();
            if (currentItem != null && currentItem.getType() != Material.AIR) {
                ItemStack cursor = event.getCursor();
                if (SlimefunItem.getByItem(cursor) instanceof ReducingAgent reducingAgent) {
                    ItemStack result = reducingAgent.use(cursor, currentItem);
                    event.setCurrentItem(result);
                    Player player = (Player) event.getWhoClicked();
                    player.sendMessage("你对\"" + ItemStackHelper.getDisplayName(currentItem) + "\"使用了还原剂!");
                }
            }
        }
    }
}
