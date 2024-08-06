/**
 *  ____  _   _  ____  _  ______    _        ____  _____  ____  _____ ____
 * / ___|| | | |/ ___|| |/ / ___|  / \      / ___|| ____|/ ___|| ____|  _ \
 * \___ \| |_| | |    | ' /\___ \ / _ \     \___ \|  _|  \___ \|  _| | |_) |
 *  ___) |  _  | |___ | . \ ___) / ___ \     ___) | |___  ___) | |___|  _ <
 * |____/|_| |_|\____||_|\_\____/_/   \_\   |____/|_____|____/|_____|_| \_\
 *
 * Credit: TheJokerDev / J0keer / Tony
 */

package com.j0keer.chestloot.cmds;

import com.j0keer.chestloot.ChestLoot;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class ChestLootCMD {
    private final ChestLoot mod;

    public ChestLootCMD(ChestLoot mod) {
        this.mod = mod;
    }

    // Register the command /chestloot and /chestloot reload in the server.
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("chestloot").requires(source -> source.hasPermissionLevel(2)).executes(context -> {
            sendMSG(context.getSource(), "", "ChestLoot v1.0", "", "Commands:", "/chestloot reload - Reload the config file", "");
            return 1;
        }).then(CommandManager.literal("reload").executes(context -> {
            mod.reloadConfig();
            sendMSG(context.getSource(), "Config reloaded");
            return 1;
        })));
        mod.console("Command registered: /chestloot.");
    }

    //
    public void sendMSG(ServerCommandSource source, String... msg) {
        for (String s : msg) {
            source.sendMessage(Text.of(s));
        }
    }
}
