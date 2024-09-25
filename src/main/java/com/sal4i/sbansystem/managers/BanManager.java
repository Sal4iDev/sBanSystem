package com.sal4i.sbansystem.managers;

import com.sal4i.sbansystem.SBanSystem;
import com.sal4i.sbansystem.utils.Database;

public class BanManager {
    private final Database db;

    public BanManager(SBanSystem plugin) {
        this.db = new Database(plugin);
    }

    public void banPlayer(String playerName, long duration, String reason) {
        long endTime = duration == -1 ? -1 : System.currentTimeMillis() + duration;
        db.saveBan(playerName, endTime, reason);
    }

    public boolean isBanned(String playerName) {
        return db.isPlayerBanned(playerName);
    }

    public long getRemainingBanTime(String playerName) {
        long endTime = db.getBanEndTime(playerName);
        return endTime == -1 ? -1 : endTime - System.currentTimeMillis();
    }

    public String getBanReason(String playerName) {
        return db.getBanReason(playerName);
    }
}
