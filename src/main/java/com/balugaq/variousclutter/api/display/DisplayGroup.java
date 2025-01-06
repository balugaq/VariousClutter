package com.balugaq.variousclutter.api.display;

import com.balugaq.variousclutter.utils.KeyUtil;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DisplayGroup {
    private static final String DISPLAY_GROUP_METADATA_KEY = KeyUtil.newKey("display_group").toString();
    private final @NotNull SlimefunAddon addon;
    private final List<Display> displays = new ArrayList<>();
    private Location center;

    public DisplayGroup(@NotNull SlimefunAddon addon) {
        if (!addon.getJavaPlugin().isEnabled()) {
            throw new UnsupportedOperationException("Plugin is not enabled");
        }
        this.addon = addon;
    }

    public @NotNull DisplayGroup center(Location location) {
        this.center = location.clone().add(0.5D, 0.5D, 0.5D);
        return this;
    }

    public @NotNull DisplayGroup add(Display display) {
        return add(display, 0.0D, 0.0D, 0.0D);
    }

    public @NotNull DisplayGroup add(Display display, double x, double y, double z) {
        return add(display, new Vector(x, y, z));
    }

    public @NotNull DisplayGroup add(Display display, Vector offset) {
        if (center == null) {
            throw new UnsupportedOperationException("Center location is not set");
        }

        display.teleport(center.clone().add(offset.getX(), offset.getY(), offset.getZ()));
        // When add display to a display group, set metadata to the display to identify its addons
        display.setMetadata(DISPLAY_GROUP_METADATA_KEY, new FixedMetadataValue(addon.getJavaPlugin(), addon.getJavaPlugin().getName()));
        return this;
    }

    public @NotNull DisplayGroup add(AbstractModelBuilder modelBuilder) {
        return add(modelBuilder, 0.0D, 0.0D, 0.0D);
    }

    public @NotNull DisplayGroup add(AbstractModelBuilder modelBuilder, double x, double y, double z) {
        return add(modelBuilder, new Vector(x, y, z));
    }

    public @NotNull DisplayGroup add(AbstractModelBuilder modelBuilder, Vector offset) {
        if (center == null) {
            throw new UnsupportedOperationException("Center location is not set");
        }

        Display display = modelBuilder.buildAt(center.clone().add(offset.getX(), offset.getY(), offset.getZ()));
        return add(display);
    }

    public @NotNull DisplayGroup hide() {
        for (Display display : displays) {
            display.setInvisible(true);
        }
        return this;
    }

    public @NotNull DisplayGroup show() {
        for (Display display : displays) {
            display.setInvisible(false);
        }
        return this;
    }

    public @NotNull DisplayGroup remove() {
        for (Display display : displays) {
            display.remove();
        }
        displays.clear();
        return this;
    }
}
