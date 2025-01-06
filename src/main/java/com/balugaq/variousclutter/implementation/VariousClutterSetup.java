package com.balugaq.variousclutter.implementation;

import com.balugaq.variousclutter.VariousClutter;
import com.balugaq.variousclutter.api.BasePlugin;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.groups.NestedItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.SubItemGroup;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class VariousClutterSetup {
    public static BasePlugin instance;
    public static NestedItemGroup nested;
    public static SubItemGroup clutter_1;
    public static SlimefunItemStack portalFrameItem;
    public static SlimefunItemStack camouflagePlateStoneItem;
    public static PortalFrame portalFrame;
    public static CamouflagePlate camouflagePlateStone;
    public static ItemStack[] portalFrameRecipe = {
            null, null, null,
            null, null, null,
            null, null, null
    };
    public static ItemStack[] camouflagePlateStoneRecipe = {
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

        camouflagePlateStoneItem = new SlimefunItemStack("VARIOUS_CLUTTER_CAMOUFLAGE_PLATE_STONE",
                Material.STONE,
                "Camouflage Plate (Stone)",
                ""
        );
        camouflagePlateStone = (CamouflagePlate) new CamouflagePlate(clutter_1, camouflagePlateStoneItem, RecipeType.NULL, camouflagePlateStoneRecipe).register(instance);
    }
}
