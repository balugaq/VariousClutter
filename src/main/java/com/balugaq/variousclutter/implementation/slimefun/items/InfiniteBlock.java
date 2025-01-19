package com.balugaq.variousclutter.implementation.slimefun.items;

import com.balugaq.variousclutter.VariousClutter;
import com.balugaq.variousclutter.api.slimefun.AbstractItem;
import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import lombok.Getter;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public class InfiniteBlock extends AbstractItem {
    public static final String BS_MATERIAL_KEY = "material";

    public InfiniteBlock(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return false;
            }

            @Override
            public void tick(Block block, SlimefunItem sfItem, SlimefunBlockData blockData) {
                Location location = block.getLocation();
                String s_material = blockData.getAllData().get(BS_MATERIAL_KEY);
                if (s_material != null) {
                    Material material = Material.getMaterial(s_material);
                    if (material != null) {
                        synchronized (VariousClutter.instance.infiniteBlocks) {
                            VariousClutter.instance.infiniteBlocks.put(location, material);
                        }
                    }
                }
            }
        }, new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(@NotNull BlockBreakEvent blockBreakEvent, @NotNull ItemStack itemStack, @NotNull List<ItemStack> list) {
            }
        });
    }
}
