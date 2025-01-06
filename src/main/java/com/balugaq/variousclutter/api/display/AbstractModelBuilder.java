package com.balugaq.variousclutter.api.display;

import org.bukkit.Location;
import org.bukkit.entity.Display;

public abstract class AbstractModelBuilder {
    public abstract Display buildAt(Location location);
}
