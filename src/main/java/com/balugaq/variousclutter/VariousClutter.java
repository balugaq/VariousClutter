package com.balugaq.variousclutter;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VariousClutter extends JavaPlugin implements SlimefunAddon {
    public static VariousClutter instance = new VariousClutter();

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

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
        return false;
    }
}
