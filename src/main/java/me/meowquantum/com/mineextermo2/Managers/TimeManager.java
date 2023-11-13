package me.meowquantum.com.mineextermo2.Managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class TimeManager {
    private final JavaPlugin plugin;
    private FileConfiguration timeConfig;
    private File timeFile;

    public TimeManager(JavaPlugin plugin) {
        this.plugin = plugin;
        setup();
        loadData();
    }

    private void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        timeFile = new File(plugin.getDataFolder(), "time.yml");
        if (!timeFile.exists()) {
            try {
                timeFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        timeConfig = YamlConfiguration.loadConfiguration(timeFile);
    }

    public void saveData() {
        try {
            timeConfig.save(timeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData() {
        timeConfig = YamlConfiguration.loadConfiguration(timeFile);
    }

    public void setPlayTime(UUID playerId, long playTime) {
        timeConfig.set(playerId.toString(), playTime);
        saveData();
    }

    public long getPlayTime(UUID playerId) {
        return timeConfig.getLong(playerId.toString(), 0);
    }

    public FileConfiguration getTimeConfig() {
        return timeConfig;
    }
}
