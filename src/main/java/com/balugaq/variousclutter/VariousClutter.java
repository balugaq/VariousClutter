package com.balugaq.variousclutter;

import com.balugaq.variousclutter.api.plugin.BasePlugin;
import com.balugaq.variousclutter.core.managers.ConfigManager;
import com.balugaq.variousclutter.core.managers.ListenerManager;
import com.balugaq.variousclutter.implementation.VariousClutterSetup;
import com.balugaq.variousclutter.implementation.slimefun.items.InfiniteBlock;
import com.balugaq.variousclutter.implementation.slimefun.tools.CamouflagePlate;
import com.balugaq.variousclutter.utils.Debug;
import com.balugaq.variousclutter.utils.ItemFilter;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class VariousClutter extends BasePlugin {
    public static VariousClutter instance;
    public final Set<UUID> camouflagePlates = new HashSet<>();
    public final Map<Location, Material> infiniteBlocks = new ConcurrentHashMap<>();
    public ConfigManager configManager;
    public ListenerManager listenerManager;
    public Runnable camouflagePlateCheckTask;
    public Runnable rollbackInfiniteBlocksTask;

    @Override
    public void onEnable() {
        instance = this;
        Debug.log("VariousClutter Is Enabling...");

        Debug.log("Loading Config...");
        instance.configManager = new ConfigManager(instance);
        instance.configManager.setup();

        Debug.log("Setting Up Items...");
        VariousClutterSetup.setup(instance);

        Debug.log("Registering Listeners...");
        listenerManager = new ListenerManager(instance);
        listenerManager.setup();

        Debug.log("Loading Tasks...");
        startTasks();

        Debug.log("VariousClutter Is Enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Debug.log("VariousClutter Is Disabling...");

        // TODO: Add code to unregister listeners and items.
        // Not finished yet.

        Debug.log("VariousClutter Is Disabled!");
    }

    @NotNull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Nullable
    @Override
    public String getBugTrackerURL() {
        return "https://github.com/balugaq/VariousClutter/issues";
    }

    public boolean isDebug() {
        return instance.configManager.isDebug();
    }

    private void startTasks() {
        Bukkit.getScheduler().runTaskTimer(instance, getCamouflagePlateCheckTask(), 20L, 60L);
        Bukkit.getScheduler().runTaskTimer(instance, getRollbackInfiniteBlocks(), 20L, 1L);
    }

    protected Runnable getCamouflagePlateCheckTask() {
        if (camouflagePlateCheckTask == null) {
            camouflagePlateCheckTask = () -> {
                Debug.debug("Running Camouflage Plate Check Task...");
                Set<UUID> invalidUUIDs = new HashSet<>();
                synchronized (camouflagePlates) {
                    if (camouflagePlates.isEmpty()) {
                        return;
                    }

                    for (UUID uuid : camouflagePlates) {
                        Entity entity = Bukkit.getEntity(uuid);
                        if (entity == null) {
                            invalidUUIDs.add(uuid);
                        }
                        if (entity instanceof BlockDisplay blockDisplay) {
                            Location location = blockDisplay.getLocation();
                            Material material = location.getBlock().getType();
                            if (material == Material.AIR || ItemFilter.isDisabledMaterial(material)) {
                                Set<String> tags = blockDisplay.getScoreboardTags();
                                if (tags.contains(CamouflagePlate.KEY)) {
                                    String sfid = "VARIOUS_CLUTTER_CAMOUFLAGE_PLATE_" + blockDisplay.getBlock().getMaterial().name().toUpperCase();
                                    SlimefunItem item = SlimefunItem.getById(sfid);
                                    if (item != null) {
                                        location.getWorld().dropItemNaturally(location, item.getItem());
                                    }

                                    blockDisplay.remove();
                                } else {
                                    invalidUUIDs.add(uuid);
                                }
                            }
                        }
                    }
                }

                if (invalidUUIDs.isEmpty()) {
                    return;
                }

                synchronized (camouflagePlates) {
                    for (UUID uuid : invalidUUIDs) {
                        camouflagePlates.remove(uuid);
                    }
                }
            };
        }

        return camouflagePlateCheckTask;
    }

    protected Runnable getRollbackInfiniteBlocks() {
        if (rollbackInfiniteBlocksTask == null) {
            rollbackInfiniteBlocksTask = () -> {
                Debug.debug("Running Rollback Infinite Blocks Task...");
                Set<Location> invalidLocations = new HashSet<>();
                synchronized (infiniteBlocks) {
                    if (infiniteBlocks.isEmpty()) {
                        return;
                    }
                    for (Map.Entry<Location, Material> entry : infiniteBlocks.entrySet()) {
                        Location location = entry.getKey();
                        Material material = entry.getValue();
                        if (location.getBlock().getType() != material) {
                            if (StorageCacheUtils.getSfItem(location) instanceof InfiniteBlock) {
                                location.getBlock().setType(material);
                            } else {
                                invalidLocations.add(location);
                            }
                        }
                    }

                    if (invalidLocations.isEmpty()) {
                        return;
                    }
                }

                synchronized (infiniteBlocks) {
                    for (Location location : invalidLocations) {
                        infiniteBlocks.remove(location);
                    }
                }
            };
        }

        return rollbackInfiniteBlocksTask;
    }
}
