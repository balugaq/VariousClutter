package com.balugaq.variousclutter.implementation;

import com.balugaq.variousclutter.VariousClutter;
import com.balugaq.variousclutter.api.Tool;
import com.balugaq.variousclutter.api.display.BlockModelBuilder;
import com.balugaq.variousclutter.utils.Debug;
import com.balugaq.variousclutter.utils.ItemFilter;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Optional;

public class CamouflagePlate extends Tool {
    public static final String KEY = "camouflage_plate";
    public static final float FIXED_BLOCK_SIZE = 0.01f;
    private static final BlockModelBuilder model = new BlockModelBuilder().brightness(new Display.Brightness(0, 15));

    public CamouflagePlate(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    public CamouflagePlate(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, @Nullable ItemStack recipeOutput) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);
    }

    @Override
    public void preRegister() {
        super.preRegister();
        addItemHandler((ItemUseHandler) playerRightClickEvent -> {
            playerRightClickEvent.cancel();
            Optional<Block> o = playerRightClickEvent.getClickedBlock();
            if (o.isEmpty()) {
                return;
            }

            Block block = o.get();
            BlockFace clickedFace = playerRightClickEvent.getClickedFace();
            Location location = block.getLocation();
            ItemStack itemStack = playerRightClickEvent.getItem();
            Material material = itemStack.getType();
            if (material == Material.AIR) {
                return;
            }

            if (SlimefunItem.getByItem(itemStack) instanceof CamouflagePlate camouflagePlate) {
                Material blockType = block.getType();
                if (ItemFilter.isPortalMaterial(blockType) || !ItemFilter.isDisabledMaterial(blockType)) {
                    camouflagePlate.addDisplay(location, clickedFace, material);
                    itemStack.setAmount(itemStack.getAmount() - 1);
                }
            }
        });
    }

    private void addDisplay(Location location, BlockFace clickedFace, Material material) {
        Debug.debug("Adding display, details:");
        Debug.debug(" | Location: " + location);
        Debug.debug(" | Clicked face: " + clickedFace);
        Debug.debug(" | Material: " + material);
        BlockModelBuilder clone = model.clone();
        clone.block(material);
        Vector3f offset = new Vector3f(0f, 0f, 0f);
        float sizex = 0f;
        float sizey = 0f;
        float sizez = 0f;
        switch (clickedFace) {
            case UP -> {
                offset.y = 1f;
                sizex = 1f;
                sizez = 1f;
            }
            case DOWN -> {
                offset.y = -FIXED_BLOCK_SIZE;
                sizex = 1f;
                sizez = 1f;
            }
            case NORTH -> {
                offset.z = -FIXED_BLOCK_SIZE;
                sizex = 1f;
                sizey = 1f;
            }
            case SOUTH -> {
                offset.z = 1f;
                sizex = 1f;
                sizey = 1f;
            }
            case EAST -> {
                offset.x = 1f;
                sizey = 1f;
                sizez = 1f;
            }
            case WEST -> {
                offset.x = -FIXED_BLOCK_SIZE;
                sizey = 1f;
                sizez = 1f;
            }
        }
        Debug.debug(" | Offset x: " + offset.x);
        Debug.debug(" | Offset y: " + offset.y);
        Debug.debug(" | Offset z: " + offset.z);
        sizex += FIXED_BLOCK_SIZE * 2;
        sizey += FIXED_BLOCK_SIZE * 2;
        sizez += FIXED_BLOCK_SIZE * 2;
        clone.setTranslation(offset);
        clone.setSize(
                sizex,
                sizey,
                sizez
        );

        Debug.debug(" | Size x: " + sizex);
        Debug.debug(" | Size y: " + sizey);
        Debug.debug(" | Size z: " + sizez);

        clone.scoreboardTags(KEY);
        BlockDisplay blockDisplay = clone.buildAt(location);
        synchronized (VariousClutter.instance.camouflagePlates) {
            VariousClutter.instance.camouflagePlates.add(blockDisplay.getUniqueId());
        }
    }
}
