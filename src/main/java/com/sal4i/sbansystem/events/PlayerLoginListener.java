package com.sal4i.sbansystem.events;

import com.sal4i.sbansystem.SBanSystem;
import com.sal4i.sbansystem.utils.TimeFormatter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {

    private final SBanSystem plugin;

    public PlayerLoginListener(SBanSystem plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        String playerName = event.getPlayer().getName();

        if (plugin.getBanManager().isBanned(playerName)) {
            long remainingTime = plugin.getBanManager().getRemainingBanTime(playerName);
            String banMessage = remainingTime == -1
                    ? plugin.getConfigManager().getMessage("ban_message_forever")
                    : plugin.getConfigManager().getMessage("ban_message")
                    .replace("%time%", new TimeFormatter(plugin).format(remainingTime))
                    .replace("%reason%", plugin.getBanManager().getBanReason(playerName));

            //noinspection deprecation
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, banMessage);
        }
    }
}
