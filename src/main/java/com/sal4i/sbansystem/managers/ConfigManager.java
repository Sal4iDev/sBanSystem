package com.sal4i.sbansystem.managers;

import com.sal4i.sbansystem.SBanSystem;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final FileConfiguration config;

    public ConfigManager(SBanSystem plugin) {
        this.config = plugin.getConfig();
    }

    public String getMessage(String key) {
        return config.getString("messages." + key);
    }

    public String getTimeUnit(String key) {
        return config.getString("time_units." + key);
    }
}
