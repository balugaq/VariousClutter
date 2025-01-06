package com.balugaq.variousclutter.implementation;

import com.balugaq.variousclutter.api.Tool;
import com.balugaq.variousclutter.api.display.BlockModelBuilder;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Display;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Optional;

public class CamouflagePlate extends Tool {
    private static final BlockModelBuilder model = new BlockModelBuilder().brightness(new Display.Brightness(15, 15));

    public CamouflagePlate(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    public CamouflagePlate(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, @Nullable ItemStack recipeOutput) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);
    }

    @Override
    public void preRegister() {
        super.preRegister();
        addItemHandler(new ItemUseHandler() {
            @Override
            public void onRightClick(PlayerRightClickEvent playerRightClickEvent) {
                playerRightClickEvent.cancel();
                Optional<Block> o = playerRightClickEvent.getClickedBlock();
                if (!o.isPresent()) {
                    return;
                }

                Block block = o.get();
                BlockFace clickedFace = playerRightClickEvent.getClickedFace();
                Location location = block.getLocation();
                Player player = playerRightClickEvent.getPlayer();
                ItemStack itemStack = playerRightClickEvent.getItem();
                Material material = block.getType();
                if (material == Material.AIR) {
                    return;
                }

                if (SlimefunItem.getByItem(itemStack) instanceof CamouflagePlate camouflagePlate) {
                    camouflagePlate.addDisplay(location, clickedFace, material);
                    itemStack.setAmount(itemStack.getAmount() - 1);
                }
            }
        });
    }

    private void addDisplay(Location location, BlockFace clickedFace, Material material) {
        BlockModelBuilder clone = model.clone();
        clone.block(material);
        Vector3f offset = new Vector3f(0f, 0f, 0f);
        switch (clickedFace) {
            case UP -> {
                offset.y = 1f;
            }
            case DOWN -> {
                offset.y = 0f;
            }
            case NORTH -> {
                offset.z = 0f;
            }
            case SOUTH -> {
                offset.z = 1f;
            }
            case EAST -> {
                offset.x = 1f;
            }
            case WEST -> {
                offset.x = 0f;
            }
        }
        clone.setTranslation(offset);
        clone.setSize(
                offset.x == 1f ? 0.01f : 1f,
                offset.y == 1f ? 0.01f : 1f,
                offset.z == 1f ? 0.01f : 1f
        );

        clone.fixedMetaData(getAddon().getJavaPlugin(), "camouflage_plate", true);
        clone.buildAt(location);
    }
}
