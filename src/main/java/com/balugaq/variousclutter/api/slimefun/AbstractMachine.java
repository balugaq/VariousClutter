package com.balugaq.variousclutter.api.slimefun;

import com.balugaq.variousclutter.api.plugin.BasePlugin;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class AbstractMachine extends AContainer implements RecipeDisplayItem {
    protected AbstractMachine(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    protected AbstractMachine(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);
    }

    @Override
    @NotNull
    public List<ItemStack> getDisplayRecipes() {
        return super.getDisplayRecipes();
    }

    @Nonnull
    public String getMachineIdentifier() {
        return getId();
    }

    @Override
    public int getSpeed() {
        return 1;
    }

    public abstract int getEnergyConsumption();

    public abstract int getCapacity();

    public AbstractMachine register(BasePlugin plugin) {
        super.register(plugin);
        return this;
    }
}
