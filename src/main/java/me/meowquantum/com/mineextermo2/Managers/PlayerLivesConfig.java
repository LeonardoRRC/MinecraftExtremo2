package me.meowquantum.com.mineextermo2.Managers;

import me.meowquantum.com.mineextermo2.MineExtermo2;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerLivesConfig {
    private final MineExtermo2 plugin;
    private FileConfiguration config;
    private File configFile;

    public PlayerLivesConfig(MineExtermo2 plugin) {
        this.plugin = plugin;
        setupConfig();
    }

    private void setupConfig() {
        configFile = new File(plugin.getDataFolder(), "data.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource("data.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public int getLives(UUID playerId) {
        return config.getInt("players." + playerId.toString(), 3);
    }

    public void setLives(UUID playerId, int lives) {
        config.set("players." + playerId.toString(), lives);
        saveConfig();
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save data.yml!");
            e.printStackTrace();
        }
    }
}
