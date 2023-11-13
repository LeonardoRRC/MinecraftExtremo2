package me.meowquantum.com.mineextermo2.Managers;

import me.meowquantum.com.mineextermo2.MineExtermo2;
import org.bukkit.configuration.file.FileConfiguration;

public class PluginConfig {
    private final MineExtermo2 plugin;
    private FileConfiguration config;
    private String bossBarMessage;

    public PluginConfig(MineExtermo2 plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        config = plugin.getConfig();
        bossBarMessage = plugin.getConfig().getString("bossbar-message", "<gradient:red:blue>Tiempo Restante: {hours}H {minutes}M {seconds}S</gradient>");
    }

    public String getActionBarFormat() {
        // Obtén el formato del ActionBar desde la configuración
        return config.getString("actionbar-format", "<color:#e02b2e><b>Combate</b><gray>:</gray></color> <color:#e02b2e><statuscombat>    <color:#e02b2e><b>Vidas</b><gray>:</gray></color> <color:#e02b2e><3>");
    }

    public String getDeathMessage() {
        return config.getString("death-message", "<player> lost a life!");
    }

    public String getOutOfLivesMessage() {
        return config.getString("out-of-lives-message", "<player> is out of lives!");
    }

    public String getLostLifeTitle() {
        return config.getString("lost-life-title", "DEAD");
    }
    public String getLostLifeSubTitle() {
        return config.getString("lost-life-subtitle", "You lost a life!");
    }

    public String getOutOfLivesTitle() {
        return config.getString("out-of-lives-title", "Game Over!");
    }

    public String getOutOfLivesSubtitle() {
        return config.getString("out-of-lives-subtitle", "You are out of lives!");
    }

    public String getString(String path) {
        return config.getString(path, "Default message if path not found");
    }

    public String getBossBarMessage() {
        return bossBarMessage;
    }

    public String getPvpEnabledMessage() {
        return config.getString("pvp-enabled-message", "<green>PvP Activado</green>");
    }

    public String getPvpDisabledMessage() {
        return config.getString("pvp-disabled-message", "<red>PvP Desactivado</red>");
    }
}
