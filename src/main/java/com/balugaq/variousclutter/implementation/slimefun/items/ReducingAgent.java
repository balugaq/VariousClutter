package com.balugaq.variousclutter.implementation.slimefun.items;

import com.balugaq.variousclutter.api.slimefun.AbstractItem;
import com.google.common.collect.Multimap;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.Set;

public class ReducingAgent extends AbstractItem {
    public ReducingAgent(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    public ItemStack use(ItemStack agent, ItemStack item) {
        agent.setAmount(agent.getAmount() - 1);
        ItemStack clone = item.clone();
        ItemMeta meta = clone.getItemMeta();
        if (meta != null) {
            Set<ItemFlag> flags = meta.getItemFlags();
            for (ItemFlag flag : flags) {
                meta.removeItemFlags(flag);
            }

            Multimap<Attribute, AttributeModifier> attributes = meta.getAttributeModifiers();
            if (attributes != null) {
                for (Attribute attribute : attributes.keySet()) {
                    meta.removeAttributeModifier(attribute);
                }
            }

            Map<Enchantment, Integer> enchants = meta.getEnchants();
            for (Enchantment enchant : enchants.keySet()) {
                meta.removeEnchant(enchant);
            }

            clone.setItemMeta(meta);
            if (meta instanceof Damageable damageable) {
                damageable.setDamage(0);
            }
        }

        return clone;
    }
}
