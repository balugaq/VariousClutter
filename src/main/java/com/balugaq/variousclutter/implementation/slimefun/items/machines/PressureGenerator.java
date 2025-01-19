package com.balugaq.variousclutter.implementation.slimefun.items.machines;

import com.balugaq.variousclutter.api.slimefun.AbstractMachine;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class PressureGenerator extends AbstractMachine {

    public PressureGenerator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    public void generateEnergyAt(Location location) {
        if (StorageCacheUtils.getSfItem(location) instanceof EnergyNetComponent component) {
            component.addCharge(location, getEnergyConsumption());
        }
    }

    // Pressure Generator has no menu
    @Override
    public void createPreset(final SlimefunItem item, String title, final Consumer<BlockMenuPreset> setup) {

    }

    @Override
    public ItemStack getProgressBar() {
        return null;
    }

    @Override
    public int getEnergyConsumption() {
        return 10;
    }

    @Override
    public int getCapacity() {
        return 1024;
    }

    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CAPACITOR;
    }
    @Override
    public void preRegister() {

    }
}
