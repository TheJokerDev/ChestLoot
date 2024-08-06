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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.loot.LootGsons;
import net.minecraft.loot.LootTable;

public class LootTableSerializer {
    private static final Gson GSON = LootGsons.getTableGsonBuilder().create();

    //Convert JSON input and get a LootTable object
    public static LootTable deserializeFromJson(JsonObject json) {
        if (!json.isJsonObject()) {
            return null;
        }

        LootTable table;

        try {
            table = GSON.fromJson(json, LootTable.class); //Using GSON to convert JSON to LootTable provided by Minecraft.
            return table;
        } catch (JsonSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    //Convert LootTable object to JSON
    public static JsonElement serializeToJson(LootTable table) {
        JsonObject json = new JsonObject();

        try {
            GSON.toJsonTree(table, LootTable.class).getAsJsonObject().entrySet().forEach(entry -> {
                json.add(entry.getKey(), entry.getValue());
            }); //Using GSON to convert LootTable to JSON provided by Minecraft.
        } catch (JsonSyntaxException e) {
            throw new RuntimeException(e);
        }

        return json;
    }
}
