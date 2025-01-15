package com.balugaq.variousclutter.implementation.slimefun.tools;

import com.balugaq.variousclutter.VariousClutter;
import com.balugaq.variousclutter.api.slimefun.AbstractTool;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerHead;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerSkin;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SlimefunBlockRestorer extends AbstractTool {
    private static final long COOLDOWN = 1000 * 5;
    private static final Map<UUID, Long> cooldown_cache = new HashMap<>();
    public SlimefunBlockRestorer(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public void preRegister() {
        addItemHandler(new ItemUseHandler() {
            @Override
            public void onRightClick(PlayerRightClickEvent playerRightClickEvent) {
                Player player = playerRightClickEvent.getPlayer();
                if (!player.isOp()) {
                    return;
                }

                long time = System.currentTimeMillis();
                if (cooldown_cache.containsKey(player.getUniqueId()) && time - cooldown_cache.get(player.getUniqueId()) < COOLDOWN) {
                    player.sendMessage("§cYou cannot use this tool again for " + (COOLDOWN - (time - cooldown_cache.get(player.getUniqueId()))) + " seconds.");
                    return;
                }

                cooldown_cache.put(player.getUniqueId(), time);

                Chunk current = player.getLocation().getChunk();
                World world = current.getWorld();
                List<Chunk> chunks = new ArrayList<>();
                for (int x = current.getX() - 1; x <= current.getX() + 1; x++) {
                    for (int z = current.getZ() - 1; z <= current.getZ() + 1; z++) {
                        chunks.add(world.getChunkAt(x, z));
                    }
                }

                for (Chunk chunk : chunks) {
                    Bukkit.getScheduler().runTask(VariousClutter.instance, () -> {
                        for (int x = 0; x < 16; x++) {
                            for (int y = world.getMinHeight(); y < world.getMaxHeight(); y++) {
                                for (int z = 0; z < 16; z++) {
                                    Block block = chunk.getBlock(x, y, z);
                                    Location location = block.getLocation();
                                    SlimefunItem item = StorageCacheUtils.getSfItem(location);
                                    if (item != null) {
                                        ItemStack itemStack = item.getItem();
                                        Material itemType = itemStack.getType();
                                        Material blockType = block.getType();
                                        if (itemType != blockType) {
                                            block.setType(itemType);
                                            PlayerSkin skin = null;
                                            boolean isHead = false;
                                            if (itemType == Material.PLAYER_HEAD || itemType == Material.PLAYER_WALL_HEAD) {
                                                if (itemStack instanceof SlimefunItemStack sfis) {
                                                    Optional<String> texture = sfis.getSkullTexture();
                                                    if (texture.isPresent()) {
                                                        skin = PlayerSkin.fromBase64(texture.get());
                                                        isHead = true;
                                                    }
                                                }
                                            }

                                            if (isHead) {
                                                PlayerHead.setSkin(block, skin, false);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
    }
}
