package com.balugaq.variousclutter.api;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@Getter
public class SpecialPortalCreateEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Set<Location> portalLocations;

    public SpecialPortalCreateEvent(Set<Location> portalLocations, Player player) {
        super(player);
        this.portalLocations = new HashSet<>(portalLocations);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
