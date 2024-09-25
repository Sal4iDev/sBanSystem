package com.sal4i.sbansystem.commands;

import com.sal4i.sbansystem.SBanSystem;
import com.sal4i.sbansystem.utils.TimeFormatter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TempBanCommand implements CommandExecutor {

    private final SBanSystem plugin;

    public TempBanCommand(SBanSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(plugin.getConfigManager().getMessage("invalid_command"));
            return false;
        }

        String playerName = args[0];
        long duration;
        try {
            duration = parseDuration(args[1]);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(plugin.getConfigManager().getMessage(""));
            return true;
        }

        StringBuilder reasonBuilder = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            reasonBuilder.append(args[i]).append(" ");
        }
        String reason = reasonBuilder.toString().trim();

        plugin.getBanManager().banPlayer(playerName, duration, reason);
        Player playerToBan = Bukkit.getPlayer(playerName);
        if (playerToBan != null) {
            long remainingTime = plugin.getBanManager().getRemainingBanTime(playerName);
            String banMessage = remainingTime == -1
                    ? plugin.getConfigManager().getMessage("ban_message_forever")
                    : plugin.getConfigManager().getMessage("ban_message")
                    .replace("%time%", new TimeFormatter(plugin).format(remainingTime))
                    .replace("%reason%", plugin.getBanManager().getBanReason(playerName));

            playerToBan.kickPlayer(banMessage);
        }

        sender.sendMessage(plugin.getConfigManager().getMessage("player_banned").replace("%player%", playerName));
        return true;
    }

    private long parseDuration(String input) {
        long duration = 0;
        StringBuilder numberBuilder = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (Character.isDigit(c)) {
                numberBuilder.append(c);
            } else {
                if (!numberBuilder.isEmpty()) {
                    int value = Integer.parseInt(numberBuilder.toString());
                    numberBuilder.setLength(0);

                    switch (c) {
                        case 'y':
                            duration += value * 365L * 24L * 60L * 60L * 1000L;
                            break;
                        case 'M':
                            duration += value * 30L * 24L * 60L * 60L * 1000L;
                            break;
                        case 'w':
                            duration += value * 7L * 24L * 60L * 60L * 1000L;
                            break;
                        case 'd':
                            duration += value * 24L * 60L * 60L * 1000L;
                            break;
                        case 'h':
                            duration += value * 60L * 60L * 1000L;
                            break;
                        case 'm':
                            duration += value * 60L * 1000L;
                            break;
                        case 's':
                            duration += value * 1000L;
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid time format: " + c);
                    }
                }
            }
        }

        return duration;
    }
}
