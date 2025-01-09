package com.balugaq.variousclutter.utils;

import com.destroystokyo.paper.MaterialTags;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class ItemFilter {
    public boolean isDisabledMaterial(@NotNull Material material) {
        return
                // Items that can store items
                MaterialTags.SHULKER_BOXES.isTagged(material)
                        || material == Material.LECTERN
                        || material == Material.HOPPER
                        || material == materialValueOf("VAULT")

                        // Items that will take two blocks
                        || MaterialTags.BEDS.isTagged(material)
                        || MaterialTags.DOORS.isTagged(material)
                        || material == Material.TALL_GRASS
                        || material == Material.LARGE_FERN
                        || material == Material.TALL_SEAGRASS
                        || material == Material.SUNFLOWER
                        || material == Material.LILAC
                        || material == Material.ROSE_BUSH
                        || material == Material.PEONY
                        || material == Material.PITCHER_PLANT

                        // Items that can place much same block in a location
                        || material == Material.CANDLE
                        || material.name().endsWith("_CANDLE")
                        || material == Material.SEA_PICKLE

                        // Items that can be placed in a location
                        || material.isAir()
                        || !material.isBlock()
                        || !material.isItem()

                        // Items that is invalid
                        || material == Material.END_PORTAL_FRAME
                        || material == Material.STRUCTURE_VOID
                        || material == Material.LIGHT
                        || material == Material.SPAWNER
                        || material == materialValueOf("TRIAL_SPAWNER")
                        || material == Material.CHORUS_FLOWER
                        || material == Material.NETHER_WART

                        // Items that has gui
                        || material == Material.STONECUTTER
                        || material == Material.GRINDSTONE
                        || material == Material.CAMPFIRE
                        || material == Material.SOUL_CAMPFIRE
                        || material == Material.ANVIL
                        || material == Material.CHIPPED_ANVIL
                        || material == Material.DAMAGED_ANVIL
                        || material == Material.ENCHANTING_TABLE
                        || material == Material.BREWING_STAND
                        || material == Material.BEE_NEST
                        || material == Material.BEEHIVE
                        || material == Material.FLOWER_POT
                        || material == Material.DECORATED_POT
                        || MaterialTags.SIGNS.isTagged(material)

                        // Items that have different types
                        || material == Material.PLAYER_HEAD
                        || material == Material.PLAYER_WALL_HEAD
                        || material == Material.CAKE
                        || material.name().endsWith("_CAKE")
                        || material == Material.POINTED_DRIPSTONE
                        || material.name().endsWith("_BANNER")

                        // Haven't been divided into categories yet
                        || material == Material.LEVER
                        || material == Material.TORCH
                        || material == Material.REDSTONE_TORCH
                        || material == Material.SOUL_TORCH
                        || material == Material.LANTERN
                        || material == Material.SOUL_LANTERN
                        || material == Material.LADDER
                        || material == Material.REPEATER
                        || material == Material.COMPARATOR
                        || material == Material.VINE
                        || material == Material.GLOW_LICHEN
                        || material == Material.CAVE_VINES
                        || material == Material.CAVE_VINES_PLANT
                        || material == Material.SCULK_VEIN
                        || material.name().endsWith("_BUTTON")
                        || material == Material.RAIL
                        || material.name().endsWith("_RAIL")
                        || material.name().endsWith("_CORAL_FAN")
                        || material.name().endsWith("_CARPET")
                        || material == Material.TURTLE_EGG
                        || material == materialValueOf("FROGSPAWN")
                        || material == Material.HANGING_ROOTS
                        || material == Material.TRIPWIRE
                        || material == Material.TRIPWIRE_HOOK
                        || material == Material.DRAGON_EGG
                        || material == Material.BELL
                        || material == Material.BIG_DRIPLEAF_STEM
                        || material == Material.CHORUS_PLANT
                        || material == Material.REDSTONE_WIRE
                        || material.name().endsWith("_PRESSURE_PLATE")
                        || material == Material.MOSS_CARPET
                        || material == Material.SNOW
                        || material == Material.SMALL_AMETHYST_BUD
                        || material == Material.MEDIUM_AMETHYST_BUD
                        || material == Material.LARGE_AMETHYST_BUD
                        || material == Material.AMETHYST_CLUSTER
                        || material.name().endsWith("_SAPLING")
                        || material.name().startsWith("POTTED_")
                        || material == Material.AZALEA
                        || material == Material.FLOWERING_AZALEA
                        || material == Material.BROWN_MUSHROOM
                        || material == Material.RED_MUSHROOM
                        || material == Material.CRIMSON_FUNGUS
                        || material == Material.WARPED_FUNGUS
                        || material == materialValueOf("SHORT_GRASS")
                        || material == Material.FERN
                        || material == Material.DEAD_BUSH
                        || material == Material.DANDELION
                        || material == Material.POPPY
                        || material == Material.BLUE_ORCHID
                        || material == Material.ALLIUM
                        || material == Material.AZURE_BLUET
                        || material == Material.RED_TULIP
                        || material == Material.ORANGE_TULIP
                        || material == Material.WHITE_TULIP
                        || material == Material.PINK_TULIP
                        || material == Material.OXEYE_DAISY
                        || material == Material.CORNFLOWER
                        || material == Material.LILY_OF_THE_VALLEY
                        || material == Material.TORCHFLOWER
                        || material == Material.WITHER_ROSE
                        || material == Material.PINK_PETALS
                        || material == Material.SPORE_BLOSSOM
                        || material == Material.BAMBOO
                        || material == Material.SUGAR_CANE
                        || material == Material.CRIMSON_ROOTS
                        || material == Material.WARPED_ROOTS
                        || material == Material.NETHER_SPROUTS
                        || material == Material.WEEPING_VINES
                        || material == Material.TWISTING_VINES
                        || material == Material.WEEPING_VINES_PLANT
                        || material == Material.TWISTING_VINES_PLANT
                        || material == Material.COCOA
                        || material == Material.SWEET_BERRY_BUSH
                        || material == Material.TORCHFLOWER_CROP
                        || material == Material.WHEAT
                        || material == Material.MELON_STEM
                        || material == Material.PUMPKIN_STEM
                        || material == Material.POTATOES
                        || material == Material.CARROTS
                        || material == Material.BEETROOTS
                        || material == Material.KELP
                        || material == Material.KELP_PLANT
                        || material == Material.SEAGRASS
                        || material == Material.LILY_PAD
                        || material == materialValueOf("CREAKING_HEART")
                        || material == materialValueOf("OPEN_EYEBLOSSOM")
                        || material == materialValueOf("CLOSED_EYEBLOSSOM")
                        || material == materialValueOf("PALE_HANGING_MOSS")
                        || material == materialValueOf("RESIN_CLUMP")
                        || material == Material.FIRE
                        || material == Material.SOUL_FIRE
                        || material == Material.PISTON_HEAD
                        || material == Material.SUSPICIOUS_SAND
                        || material == Material.SUSPICIOUS_GRAVEL
                        || material == Material.BUBBLE_COLUMN
                        || material == Material.POWDER_SNOW
                        || material == Material.PISTON
                        || material == Material.STICKY_PISTON
                        || material == Material.CONDUIT
                        || material == Material.MANGROVE_PROPAGULE
                        || MaterialTags.FENCE_GATES.isTagged(material)
                        || material.name().endsWith("_SLAB")
                        || material.name().endsWith("_STAIRS")
                        || material.name().endsWith("_TRAPDOOR")
                        || material.name().endsWith("_HEAD")
                        || material.name().endsWith("_SKULL")
                        || material == Material.END_ROD
                        || material == Material.LIGHTNING_ROD
                        || material == Material.CHAIN
                        || material == Material.DAYLIGHT_DETECTOR
                        || material == Material.SCULK_SENSOR
                        || material == Material.SCULK_SHRIEKER
                        || material == Material.CALIBRATED_SCULK_SENSOR
                        || material == Material.SNIFFER_EGG
                        || material.name().endsWith("_STAINED_GLASS_PANE")
                        || material.name().endsWith("_WALL")
                        || MaterialTags.FENCES.isTagged(material)
                        || material == Material.GLASS_PANE
                        || material == Material.IRON_BARS
                        || material == Material.FARMLAND
                        || material == Material.DIRT_PATH
                        || material.name().endsWith("_WALL_FAN")
                        || material.name().endsWith("_WALL_BANNER")
                        || material == Material.BIG_DRIPLEAF
                        || material == Material.SMALL_DRIPLEAF
                        || material == Material.SCAFFOLDING
                        || material == materialValueOf("WILDFLOWERS")
                        || material == materialValueOf("LEAF_LITTER")
                        || material == materialValueOf("GRASS");
    }

    @NotNull
    private Material materialValueOf(String name) {
        try {
            return Material.valueOf(name);
        } catch (IllegalArgumentException | NullPointerException e) {
            return Material.AIR;
        }
    }
}
