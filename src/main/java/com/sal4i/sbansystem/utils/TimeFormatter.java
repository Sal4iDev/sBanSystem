package com.sal4i.sbansystem.utils;

import com.sal4i.sbansystem.SBanSystem;
import com.sal4i.sbansystem.managers.ConfigManager;

public class TimeFormatter {

    private final ConfigManager configManager;

    public TimeFormatter(SBanSystem plugin) {
        this.configManager = plugin.getConfigManager();
    }

    public String format(long millis) {
        long seconds = millis / 1000 % 60;
        long minutes = millis / (1000 * 60) % 60;
        long hours = millis / (1000 * 60 * 60) % 24;
        long days = millis / (1000 * 60 * 60 * 24);

        StringBuilder sb = new StringBuilder();
        String space = " ";
        if (days > 0) {
            sb.append(days)
                    .append(space)
                    .append(configManager.getTimeUnit("days"))
                    .append(space);
        }
        if (hours > 0) {
            sb.append(hours)
                    .append(space)
                    .append(configManager.getTimeUnit("hours"))
                    .append(space);
        }
        if (minutes > 0) {
            sb.append(minutes)
                    .append(space)
                    .append(configManager.getTimeUnit("minutes"))
                    .append(space);
        }
        if (seconds > 0) {
            sb.append(seconds)
                    .append(space)
                    .append(configManager.getTimeUnit("seconds"));
        }

        return sb.toString().trim();
    }
}
