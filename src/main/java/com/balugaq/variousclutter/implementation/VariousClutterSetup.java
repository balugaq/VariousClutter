package com.balugaq.variousclutter.implementation;

import com.balugaq.variousclutter.api.plugin.BasePlugin;
import com.balugaq.variousclutter.implementation.slimefun.items.InfiniteBlock;
import com.balugaq.variousclutter.implementation.slimefun.items.PortalFrame;
import com.balugaq.variousclutter.implementation.slimefun.items.ReducingAgent;
import com.balugaq.variousclutter.implementation.slimefun.items.machines.PressureGenerator;
import com.balugaq.variousclutter.implementation.slimefun.tools.CamouflagePlate;
import com.balugaq.variousclutter.implementation.slimefun.tools.InfiniteBlockBreaker;
import com.balugaq.variousclutter.implementation.slimefun.tools.SlimefunBlockRestorer;
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
    public static final ItemStack Stone = new ItemStack(Material.STONE);
    public static final ItemStack StonePressurePlate = new ItemStack(Material.STONE_PRESSURE_PLATE);
    public static final ItemStack[] portalFrameRecipe = {
            Obsidian, SlimefunItems.COMPRESSED_CARBON, Obsidian,
            SlimefunItems.COMPRESSED_CARBON, SlimefunItems.ENRICHED_NETHER_ICE, SlimefunItems.COMPRESSED_CARBON,
            Obsidian, SlimefunItems.COMPRESSED_CARBON, Obsidian
    };
    public static final ItemStack[] pressureGeneratorRecipe = {
            StonePressurePlate, StonePressurePlate, StonePressurePlate,
            Stone, SlimefunItems.ENERGY_CONNECTOR, Stone,
            SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ELECTRIC_MOTOR,
    };
    public static BasePlugin instance;
    public static NestedItemGroup nested;
    public static SubItemGroup clutter;
    public static SubItemGroup camouflage_plates;
    public static SlimefunItemStack portalFrameItem;
    public static SlimefunItemStack infiniteBlockItem;
    public static SlimefunItemStack infiniteBlockBreakerItem;
    public static SlimefunItemStack slimefunBlockRestorerItem;
    public static SlimefunItemStack reducingAgentItem;
    public static SlimefunItemStack pressureGeneratorItem;
    public static PortalFrame portalFrame;
    public static InfiniteBlock infiniteBlock;
    public static InfiniteBlockBreaker infiniteBlockBreaker;
    public static SlimefunBlockRestorer slimefunBlockRestorer;
    public static ReducingAgent reducingAgent;
    public static PressureGenerator pressureGenerator;

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

        infiniteBlockItem = new SlimefunItemStack("VARIOUS_CLUTTER_INFINITE_BLOCK",
                Material.IRON_BLOCK,
                "&e无限方块",
                "",
                "&4&ka &r&c你似乎可以用它创造无限方块...",
                "&4&ka &r&c手持物品对此方块右键可设置方块类型",
                "&4仅 OP 权限可用"
        );

        infiniteBlock = (InfiniteBlock) new InfiniteBlock(clutter, infiniteBlockItem, RecipeType.NULL, new ItemStack[]{}).register(instance);
        infiniteBlockBreakerItem = new SlimefunItemStack("VARIOUS_CLUTTER_INFINITE_BLOCK_BREAKER",
                Material.GOLDEN_PICKAXE,
                "&e无限方块破坏器",
                "",
                "&4&ka &r&c你可以用它破坏无限方块...",
                "&4&ka &r&c仅 OP 权限可用"
        );
        infiniteBlockBreaker = (InfiniteBlockBreaker) new InfiniteBlockBreaker(clutter, infiniteBlockBreakerItem, RecipeType.NULL, new ItemStack[]{}).register(instance);

        slimefunBlockRestorerItem = new SlimefunItemStack("VARIOUS_CLUTTER_SLIMEFUN_BLOCK_RESTORER",
                Material.BLAZE_ROD,
                "&eSlimefun 方块恢复器",
                "",
                "&4&ka &r&c你可以用它恢复丢失方块类型的 Slimefun 方块...",
                "&4&ka &r&c仅 OP 权限可用"
        );
        slimefunBlockRestorer = (SlimefunBlockRestorer) new SlimefunBlockRestorer(clutter, slimefunBlockRestorerItem, RecipeType.NULL, new ItemStack[]{}).register(instance);
        reducingAgentItem = new SlimefunItemStack("VARIOUS_CLUTTER_REDUCING_AGENT",
                Material.GUNPOWDER,
                "&e还原剂",
                "",
                "&4&ka &r&c你可以用它还原一个物品..."
        );
        reducingAgent = (ReducingAgent) new ReducingAgent(clutter, reducingAgentItem, RecipeType.NULL, new ItemStack[]{}).register(instance);
        pressureGeneratorItem = new SlimefunItemStack("VARIOUS_CLUTTER_PRESSURE_GENERATOR",
                Material.STONE_PRESSURE_PLATE,
                "&3踩踏式发电机",
                "",
                "&4&ka &r&d通过踩踏产生能源..."
        );
        pressureGenerator = (PressureGenerator) new PressureGenerator(clutter, pressureGeneratorItem, RecipeType.ENHANCED_CRAFTING_TABLE, pressureGeneratorRecipe).register(instance);
    }
}
