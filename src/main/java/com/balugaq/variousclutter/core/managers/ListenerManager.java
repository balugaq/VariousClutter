package com.balugaq.variousclutter.core.managers;

import com.balugaq.variousclutter.core.listeners.CamouflagePlateBreakListener;
import com.balugaq.variousclutter.core.listeners.InfiniteBlockListener;
import com.balugaq.variousclutter.core.listeners.PressureGeneratorListener;
import com.balugaq.variousclutter.core.listeners.ReducingAgentUseListener;
import com.balugaq.variousclutter.core.listeners.SpecialPortalCreateListener;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ListenerManager {
    private final JavaPlugin plugin;
    private final List<Listener> listeners = new ArrayList<>();

    public ListenerManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        listeners.add(new SpecialPortalCreateListener());
        listeners.add(new CamouflagePlateBreakListener());
        listeners.add(new InfiniteBlockListener());
        listeners.add(new ReducingAgentUseListener());
        listeners.add(new PressureGeneratorListener());
        registerListeners();
    }

    public void registerListeners() {
        for (Listener listener : listeners) {
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }
}
