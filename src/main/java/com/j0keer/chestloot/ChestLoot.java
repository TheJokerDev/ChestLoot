/**
 *  ____  _   _  ____  _  ______    _        ____  _____  ____  _____ ____
 * / ___|| | | |/ ___|| |/ / ___|  / \      / ___|| ____|/ ___|| ____|  _ \
 * \___ \| |_| | |    | ' /\___ \ / _ \     \___ \|  _|  \___ \|  _| | |_) |
 *  ___) |  _  | |___ | . \ ___) / ___ \     ___) | |___  ___) | |___|  _ <
 * |____/|_| |_|\____||_|\_\____/_/   \_\   |____/|_____|____/|_____|_| \_\
 *
 * Credit: TheJokerDev / J0keer / Tony
 */
package com.j0keer.chestloot;

import com.j0keer.chestloot.cmds.ChestLootCMD;
import com.j0keer.chestloot.utils.ChestUtils;
import com.j0keer.chestloot.utils.ModUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ChestLoot extends ModUtils implements ModInitializer {
    public static String MOD_ID = "chestloot";
    private static ChestLoot instance;

    private ChestUtils chestUtils;

    public static ChestLoot getInstance() {
        return instance;
    }

    @Override
    public void onInitialize() {
        instance = this; //Static access to the main class.

        setMod(this); //Essential to load and save config files.

        console("Initializing ChestLoot");
        double ms = System.currentTimeMillis(); //Time counter
        saveDefaultConfig();

        console("Registering commands");
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            new ChestLootCMD(this).register(dispatcher);
        });

        console("Loading loot tables");
        chestUtils = new ChestUtils(this);
        chestUtils.loadLootTables();

        ms = System.currentTimeMillis() - ms;
        console("ChestLoot initialized in " + ms + "ms.");
    }

    public ChestUtils getChestUtils() {
        return chestUtils;
    }
}
