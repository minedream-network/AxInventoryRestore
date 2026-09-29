package com.artillexstudios.axinventoryrestore.commands.subcommands;

import com.artillexstudios.axapi.scheduler.Scheduler;
import com.artillexstudios.axinventoryrestore.AxInventoryRestore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.artillexstudios.axinventoryrestore.AxInventoryRestore.MESSAGEUTILS;

public enum Cleanup {
    INSTANCE;

    public void execute(CommandSender sender) {
        AxInventoryRestore.getThreadedQueue().submit(() -> {
            boolean success = AxInventoryRestore.getDatabase().cleanup();
            Runnable sendResult = () -> MESSAGEUTILS.sendLang(sender, success ? "cleaned-up" : "cleanup-failed");
            if (sender instanceof Player player) {
                Scheduler.get().run(player, task -> sendResult.run(), () -> {});
            } else {
                Scheduler.get().run(sendResult);
            }
        });
    }
}
