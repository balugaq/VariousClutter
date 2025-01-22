package com.balugaq.variousclutter.core.listeners;

import com.balugaq.variousclutter.implementation.slimefun.items.ReducingAgent;
import com.balugaq.variousclutter.utils.Debug;
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
        Debug.debug("ReducingAgentUseListener.onAgentUse");
        if (event.isRightClick()) {
            Debug.debug("ReducingAgentUseListener.onAgentUse: isRightClick");
            ItemStack currentItem = event.getCurrentItem();
            Debug.debug("CurrentItem: " + currentItem);
            if (currentItem != null && currentItem.getType() != Material.AIR) {
                Debug.debug("ReducingAgentUseListener.onAgentUse: currentItem is not null");
                ItemStack cursor = event.getCursor();
                Debug.debug("Cursor: " + cursor);
                if (SlimefunItem.getByItem(cursor) instanceof ReducingAgent reducingAgent) {
                    Debug.debug("ReducingAgentUseListener.onAgentUse: cursor is ReducingAgent");
                    ItemStack result = reducingAgent.use(cursor, currentItem);
                    event.setCurrentItem(result);
                    Player player = (Player) event.getWhoClicked();
                    player.sendMessage("你对\"&r" + ItemStackHelper.getDisplayName(currentItem) + "&r\"使用了还原剂!");
                }
            }
        }
    }
}
