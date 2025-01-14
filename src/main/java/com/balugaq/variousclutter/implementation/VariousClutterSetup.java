package com.balugaq.variousclutter.implementation;

import com.balugaq.variousclutter.api.plugin.BasePlugin;
import com.balugaq.variousclutter.implementation.slimefun.tools.CamouflagePlate;
import com.balugaq.variousclutter.implementation.slimefun.items.PortalFrame;
import com.balugaq.variousclutter.utils.Debug;
import com.balugaq.variousclutter.utils.ItemFilter;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.groups.NestedItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.SubItemGroup;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import net.guizhanss.guizhanlib.minecraft.helper.inventory.ItemStackHelper;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class VariousClutterSetup {
    public static final ItemStack Obsidian = new ItemStack(Material.OBSIDIAN);
    public static final ItemStack ItemFrame = new ItemStack(Material.ITEM_FRAME);
    public static final ItemStack[] portalFrameRecipe = {
            Obsidian, SlimefunItems.COMPRESSED_CARBON, Obsidian,
            SlimefunItems.COMPRESSED_CARBON, SlimefunItems.ENRICHED_NETHER_ICE, SlimefunItems.COMPRESSED_CARBON,
            Obsidian, SlimefunItems.COMPRESSED_CARBON, Obsidian
    };
    public static BasePlugin instance;
    public static NestedItemGroup nested;
    public static SubItemGroup clutter;
    public static SubItemGroup camouflage_plates;
    public static SlimefunItemStack portalFrameItem;
    public static PortalFrame portalFrame;

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
        clutter = new SubItemGroup(
                new NamespacedKey(instance, "various_clutter_clutter"),
                nested,
                new CustomItemStack(
                        Material.DIAMOND_BLOCK,
                        "&6杂项"
                ));
        camouflage_plates = new SubItemGroup(
                new NamespacedKey(instance, "various_clutter_camouflage_plates"),
                nested,
                new CustomItemStack(
                        Material.IRON_BLOCK,
                        "&6伪装板"
                ));
    }

    protected static void setupItems() {
        portalFrameItem = new SlimefunItemStack("VARIOUS_CLUTTER_PORTAL_FRAME",
                Material.RED_TERRACOTTA,
                "&e奇异的传送门框架",
                "",
                "&4&ka &r&c你似乎可以用它搭建不规则的下界传送门..."
        );
        portalFrame = (PortalFrame) new PortalFrame(clutter, portalFrameItem, RecipeType.MAGIC_WORKBENCH, portalFrameRecipe).register(instance);

        // Register CamouflagePlates for all whole blocks
        for (Material material : Material.values()) {
            if (material == Material.AIR || ItemFilter.isDisabledMaterial(material)) {
                continue;
            }

            if (!material.isBlock() || !material.isSolid() || !material.isItem()) {
                continue;
            }

            String name = ItemStackHelper.getDisplayName(new ItemStack(material));
            Debug.debug("Registering CamouflagePlate for " + name);
            Debug.debug(" | Material: " + material.name());
            try {
                new CamouflagePlate(camouflage_plates,
                        new SlimefunItemStack("VARIOUS_CLUTTER_CAMOUFLAGE_PLATE_" + material.name().toUpperCase(),
                                material,
                                "&x&0&0&D&2&C&F伪装板 (" + name + ")",
                                ""
                        ), RecipeType.MAGIC_WORKBENCH, new ItemStack[]{
                        SlimefunItems.MAGIC_LUMP_2, ItemFrame, SlimefunItems.MAGIC_LUMP_2,
                        SlimefunItems.MAGICAL_GLASS, new ItemStack(material), SlimefunItems.MAGICAL_GLASS,
                        SlimefunItems.MAGIC_LUMP_2, SlimefunItems.MAGICAL_GLASS, SlimefunItems.MAGIC_LUMP_2
                }).register(instance);
            } catch (Throwable e) {
                Debug.log("Failed to register CamouflagePlate for " + material.name());
                Debug.log(e);
            }
        }
    }
}
