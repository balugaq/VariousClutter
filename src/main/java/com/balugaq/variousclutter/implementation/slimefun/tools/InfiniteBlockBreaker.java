package com.balugaq.variousclutter.implementation.slimefun.tools;

import com.balugaq.variousclutter.api.slimefun.AbstractTool;
import com.balugaq.variousclutter.implementation.slimefun.items.InfiniteBlock;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.ToolUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

public class InfiniteBlockBreaker extends AbstractTool implements NotPlaceable {
    public InfiniteBlockBreaker(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public void preRegister() {
        addItemHandler(new ToolUseHandler() {
            @Override
            public void onToolUse(BlockBreakEvent blockBreakEvent, ItemStack itemStack, int i, List<ItemStack> list) {
                Player player = blockBreakEvent.getPlayer();
                if (!player.isOp()) {
                    return;
                }

                Location location = blockBreakEvent.getBlock().getLocation();
                if (StorageCacheUtils.getSfItem(location) instanceof InfiniteBlock) {
                    Slimefun.getDatabaseManager().getBlockDataController().removeBlock(location);
                    player.sendMessage("Infinite block removed.");
                }
            }
        });
    }
}
