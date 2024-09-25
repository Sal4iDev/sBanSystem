package com.sal4i.sbansystem.commands;

import com.sal4i.sbansystem.SBanSystem;
import com.sal4i.sbansystem.utils.TimeFormatter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class IsBanCommand implements CommandExecutor {

    private final SBanSystem plugin;

    public IsBanCommand(SBanSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(plugin.getConfigManager().getMessage("invalid_command"));
            return false;
        }

        String playerName = args[0];
        boolean isBanned = plugin.getBanManager().isBanned(playerName);

        if (isBanned) {
            long remainingTime = plugin.getBanManager().getRemainingBanTime(playerName);
            String message = remainingTime == -1
                    ? plugin.getConfigManager().getMessage("player_banned_forever")
                    : plugin.getConfigManager().getMessage("player_banned")
                    .replace("%time%", new TimeFormatter(plugin).format(remainingTime));
            sender.sendMessage(message);
        } else {
            sender.sendMessage(plugin.getConfigManager().getMessage("player_not_banned").replace("%player%", playerName));
        }

        return true;
    }
}
