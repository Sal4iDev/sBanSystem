package com.sal4i.sbansystem;

import com.sal4i.sbansystem.commands.IsBanCommand;
import com.sal4i.sbansystem.commands.TempBanCommand;
import com.sal4i.sbansystem.events.PlayerLoginListener;
import com.sal4i.sbansystem.managers.BanManager;
import com.sal4i.sbansystem.managers.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SBanSystem extends JavaPlugin {

    private ConfigManager configManager;
    private BanManager banManager;

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.configManager = new ConfigManager(this);
        this.banManager = new BanManager(this);

        getCommand("tempban").setExecutor(new TempBanCommand(this));
        getCommand("isban").setExecutor(new IsBanCommand(this));

        getServer().getPluginManager().registerEvents(new PlayerLoginListener(this), this);
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public BanManager getBanManager() {
        return banManager;
    }
}

