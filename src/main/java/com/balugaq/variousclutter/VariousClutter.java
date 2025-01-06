package com.balugaq.variousclutter;

import com.balugaq.variousclutter.api.BasePlugin;
import com.balugaq.variousclutter.core.listeners.CamouflagePlateBreakListener;
import com.balugaq.variousclutter.core.listeners.SpecialPortalCreateListener;
import com.balugaq.variousclutter.core.managers.ConfigManager;
import com.balugaq.variousclutter.implementation.VariousClutterSetup;
import com.balugaq.variousclutter.utils.Debug;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class VariousClutter extends BasePlugin {
    public static VariousClutter instance;
    public ConfigManager configManager;

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
}
