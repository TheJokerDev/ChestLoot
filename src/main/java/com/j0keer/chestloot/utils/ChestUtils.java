/**
 *  ____  _   _  ____  _  ______    _        ____  _____  ____  _____ ____
 * / ___|| | | |/ ___|| |/ / ___|  / \      / ___|| ____|/ ___|| ____|  _ \
 * \___ \| |_| | |    | ' /\___ \ / _ \     \___ \|  _|  \___ \|  _| | |_) |
 *  ___) |  _  | |___ | . \ ___) / ___ \     ___) | |___  ___) | |___|  _ <
 * |____/|_| |_|\____||_|\_\____/_/   \_\   |____/|_____|____/|_____|_| \_\
 *
 * Credit: TheJokerDev / J0keer / Tony
 */
package com.j0keer.chestloot.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.j0keer.chestloot.ChestLoot;
import com.j0keer.chestloot.type.ChestData;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChestUtils {
    private final HashMap<String, List<LootTable>> lootTables = new HashMap<>();
    private final ChestLoot mod;

    public ChestUtils(ChestLoot mod) {
        this.mod = mod;
    }

    public void applyLootTable(BlockPos pos, ChestBlockEntity chest) {
        ChestData data = ((ChestData) chest); // Get the chest data containing the used flag
        if (data.isUsed()) {
            return;
        }
        if (chest.getWorld() == null) return;
        RegistryEntry<Biome> biome = chest.getWorld().getBiome(pos); // Get the biome of the chest

        if (biome.getKey().isEmpty()) return;
        String name = biome.getKey().get().getValue().toString();

        LootTable table = getRandomLootTable(name); // Get a random loot table for the biome

        if (table != null) {
            // Clear inventory
            chest.clear();

            // Generate loot
            ServerWorld world = (ServerWorld) chest.getWorld(); // Get the world of the chest
            LootContext context = new LootContext.Builder(world).build(new LootContextType.Builder().build()); // Create a loot context

            if (context == null) {
                return;
            }

            table.supplyInventory(chest, context); // Supply the inventory with loot
            data.setUsed(true); // Set the used flag to true
        }
    }

    public LootTable getRandomLootTable(String name) {
        List<LootTable> list = lootTables.getOrDefault(name, lootTables.get("default")); // Get the list of loot tables for the biome. If the list is null, get the default list
        if (list == null || list.isEmpty()) return null;
        return list.get((int) (Math.random() * list.size())); // Return a random loot table from the list
    }

    public void loadLootTables() {
        lootTables.clear(); // Clear previous loot tables

        mod.getConfig().entrySet().forEach(entry -> {
            String name = entry.getKey();
            List<LootTable> list = new ArrayList<>(); // Create a new list for each biome

            mod.console("Loading loot table for biome: " + name);

            JsonArray array = entry.getValue().getAsJsonArray(); // Get the loot tables array
            for (JsonElement element : array) {
                if (!element.isJsonObject()) continue;
                JsonObject obj = element.getAsJsonObject();
                LootTable table = LootTableSerializer.deserializeFromJson(obj); // Deserialize the loot table
                if (table != null) {
                    list.add(table); // Add the loot table to the list
                }
            }

            if (list.isEmpty()) {
                mod.console("No loot tables found for biome: " + name);
            } else {
                mod.console("Loaded " + list.size() + " loot tables for biome: " + name);
            }

            lootTables.put(name, list); // Add the list to the loot tables map
        });
    }
}
