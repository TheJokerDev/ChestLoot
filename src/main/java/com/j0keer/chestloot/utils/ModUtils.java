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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.j0keer.chestloot.ChestLoot;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModUtils {
    private ChestLoot mod;
    public static Logger LOGGER = LogManager.getLogger(ChestLoot.MOD_ID);
    private JsonObject config;

    public void setMod(ChestLoot mod) {
        this.mod = mod;
    }

    public void reloadConfig() {
        saveDefaultConfig();
        mod.getChestUtils().loadLootTables(); //Reload loot tables after config reload by /chestloot reload command.
    }

    //Load the default config json file.
    public void saveDefaultConfig() {
        File file = new File(getDataFolder(), "loot_tables.json");
        if (!file.exists()) {
            copy(getResource("config/loot_tables.json"), file);
        }
        //Read json file from file and set into local config json.
        try {
            JsonReader reader = new JsonReader(Files.newBufferedReader(file.toPath()));
            config = JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException e) {
            console("{error}Failed to load config file.");
            e.printStackTrace();
        }
    }

    //Save the config json to file.
    public void saveConfig() {
        File file = new File(getDataFolder(), "loot_tables.json");
        try {
            Files.write(file.toPath(), config.toString().getBytes());
        } catch (IOException e) {
            console("{error}Failed to save config file.");
            e.printStackTrace();
        }
    }

    //Get the mod json config.
    public JsonObject getConfig() {
        return config;
    }

    //Obtain the data folder of the mod.
    public File getDataFolder() {
        Path folder = FabricLoader.getInstance().getConfigDir().resolve(ChestLoot.MOD_ID);
        if (!folder.toFile().exists()) {
            try {
                Files.createDirectory(folder);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return folder.toFile();
    }

    //Print messages to console.
    public void console(String... msg) {
        for (String s : msg) {
            if (s.contains("{error}")) {
                LOGGER.error(s.replace("{error}", ""));
            } else if (s.contains("{warn}")) {
                LOGGER.warn(s.replace("{warn}", ""));
            } else if (s.contains("{info}")) {
                LOGGER.info(s.replace("{info}", ""));
            } else if (s.contains("{debug}")) {
                LOGGER.debug(s.replace("{debug}", ""));
            } else {
                LOGGER.info(s);
            }
        }
    }

    //Get resource file from jar.
    @Nullable
    public InputStream getResource(@NotNull String filename) {
        try {
            URL url = getClass().getClassLoader().getResource(filename);
            if (url == null) {
                return null;
            } else {
                URLConnection connection = url.openConnection();
                connection.setUseCaches(false);
                return connection.getInputStream();
            }
        } catch (IOException var4) {
            return null;
        }
    }

    //Copy file from input stream to file.
    public void copy(InputStream in, File file) {
        try {
            if (in == null) {
                throw new IllegalArgumentException("Input stream is null");
            }

            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];

            int len;
            while((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
