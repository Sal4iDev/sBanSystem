package com.sal4i.sbansystem.utils;

import com.sal4i.sbansystem.SBanSystem;

import java.io.File;
import java.sql.*;

@SuppressWarnings({"CallToPrintStackTrace", "ResultOfMethodCallIgnored"})
public class Database {

    private final SBanSystem plugin;
    private Connection connection;

    public Database(SBanSystem plugin) {
        this.plugin = plugin;
        connect();
        createTables();
    }

    private void connect() {
        try {
            File dataFolder = plugin.getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder + "/bans.db");
        } catch (SQLException e) {
            e.printStackTrace();
            plugin.getLogger().severe(plugin.getConfigManager().getMessage("database_error"));
        }
    }

    private void createTables() {
        try (PreparedStatement stmt = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS bans (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "playerName TEXT NOT NULL," +
                "endTime LONG," +
                "reason TEXT" +
                ");")) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveBan(String playerName, long endTime, String reason) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO bans (playerName, endTime, reason) VALUES (?, ?, ?)")) {
            stmt.setString(1, playerName);
            stmt.setLong(2, endTime);
            stmt.setString(3, reason);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlayerBanned(String playerName) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM bans WHERE playerName = ? AND (endTime > ? OR endTime = -1)")) {
            stmt.setString(1, playerName);
            stmt.setLong(2, System.currentTimeMillis());
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public long getBanEndTime(String playerName) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT endTime FROM bans WHERE playerName = ? ORDER BY endTime DESC LIMIT 1")) {
            stmt.setString(1, playerName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("endTime");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String getBanReason(String playerName) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT reason FROM bans WHERE playerName = ? ORDER BY endTime DESC LIMIT 1")) {
            stmt.setString(1, playerName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("reason");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plugin.getConfigManager().getMessage("ban_no_reason");
    }
}
