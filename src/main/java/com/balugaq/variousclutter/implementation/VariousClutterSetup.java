package com.balugaq.variousclutter.implementation;

import com.balugaq.variousclutter.api.BasePlugin;
import com.balugaq.variousclutter.utils.Debug;
import com.balugaq.variousclutter.utils.ItemFilter;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.groups.NestedItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.SubItemGroup;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import net.guizhanss.guizhanlib.minecraft.helper.inventory.ItemStackHelper;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class VariousClutterSetup {
    public static BasePlugin instance;
    public static NestedItemGroup nested;
    public static SubItemGroup clutter_1;
    public static SlimefunItemStack portalFrameItem;
    public static PortalFrame portalFrame;
    public static ItemStack[] portalFrameRecipe = {
            null, null, null,
            null, null, null,
            null, null, null
    };

    public static void setup(BasePlugin plugin) {
        instance = plugin;
        setupGroups();
        setupItems();
    }

    protected static void setupGroups() {
        nested = new NestedItemGroup(
                new NamespacedKey(instance, "various_clutter_main"),
                new CustomItemStack(
                        Material.GLASS_BOTTLE,
                        "&6Various Clutter"
                ));
        clutter_1 = new SubItemGroup(
                new NamespacedKey(instance, "various_clutter_1"),
                nested,
                new CustomItemStack(
                        Material.DIAMOND_BLOCK,
                        "&6Various Clutter"
                ));
    }

    protected static void setupItems() {
        portalFrameItem = new SlimefunItemStack("VARIOUS_CLUTTER_PORTAL_FRAME",
                Material.CRYING_OBSIDIAN,
                "Portal Frame",
                ""
        );
        portalFrame = (PortalFrame) new PortalFrame(clutter_1, portalFrameItem, RecipeType.NULL, portalFrameRecipe).register(instance);

        for (Material material : Material.values()) {
            if (ItemFilter.isDisabledMaterial(material)) {
                continue;
            }

            String name = ItemStackHelper.getDisplayName(new ItemStack(material));
            Debug.debug("Registering CamouflagePlate for " + name);
            Debug.debug(" | Material: " + material.name());
            try {
                new CamouflagePlate(clutter_1,
                        new SlimefunItemStack("VARIOUS_CLUTTER_CAMOUFLAGE_PLATE_" + material.name().toUpperCase(),
                                material,
                                "&x&0&0&D&2&C&F伪装板 (" + name + ")",
                                ""
                        ), RecipeType.NULL, new ItemStack[]{
                        null, null, null,
                        null, null, null,
                        null, null, null
                }).register(instance);
            } catch (Throwable e) {
                Debug.log("Failed to register CamouflagePlate for " + material.name());
                Debug.log(e);
            }
        }
    }
}
