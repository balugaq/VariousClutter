package com.balugaq.variousclutter;

import com.balugaq.variousclutter.api.BasePlugin;
import com.balugaq.variousclutter.core.listeners.CamouflagePlateBreakListener;
import com.balugaq.variousclutter.core.listeners.SpecialPortalCreateListener;
import com.balugaq.variousclutter.core.managers.ConfigManager;
import com.balugaq.variousclutter.implementation.CamouflagePlate;
import com.balugaq.variousclutter.implementation.VariousClutterSetup;
import com.balugaq.variousclutter.utils.Debug;
import com.balugaq.variousclutter.utils.ItemFilter;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
public class VariousClutter extends BasePlugin {
    public static VariousClutter instance;
    public ConfigManager configManager;
    public final Set<UUID> camouflagePlates = new HashSet<>();
    public Runnable camouflagePlateCheckTask;

    @Override
    public void onEnable() {
        instance = this;
        Debug.log("VariousClutter Is Enabling...");

        Debug.log("Loading Config...");
        instance.configManager = new ConfigManager(instance);
        instance.configManager.setup();

        Debug.log("Setting Up Items...");
        VariousClutterSetup.setup(instance);
        Debug.log("Set Up Items Successfully!");

        Debug.log("Registering Listeners...");
        Bukkit.getPluginManager().registerEvents(new SpecialPortalCreateListener(), instance);
        Bukkit.getPluginManager().registerEvents(new CamouflagePlateBreakListener(), instance);
        Debug.log("Listeners Registered!");

        Debug.log("Loading Tasks...");
        startTasks();
        Debug.log("Tasks Loaded!");

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
                }

                synchronized (camouflagePlates) {
                    for (UUID uuid : camouflagePlates) {
                        Entity entity = Bukkit.getEntity(uuid);
                        if (entity == null) {
                            invalidUUIDs.add(uuid);
                        }
                        if (entity instanceof BlockDisplay blockDisplay) {
                            Location location = blockDisplay.getLocation();
                            Material material = location.getBlock().getType();
                            if (ItemFilter.isDisabledMaterial(material)) {
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

                for (UUID uuid : invalidUUIDs) {
                    synchronized (camouflagePlates) {
                        camouflagePlates.remove(uuid);
                    }
                }
            };
        }

        return camouflagePlateCheckTask;
    }
}
