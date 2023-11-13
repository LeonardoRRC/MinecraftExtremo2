package me.meowquantum.com.mineextermo2.Handlers;

import me.meowquantum.com.mineextermo2.Managers.LivesSystem;
import me.meowquantum.com.mineextermo2.Managers.PluginConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ActionBarUpdater extends BukkitRunnable {
    private final LivesSystem livesSystem;
    private final PluginConfig pluginConfig;
    private final MiniMessage miniMessage;
    private boolean pvpEnabled;

    public ActionBarUpdater(LivesSystem livesSystem, PluginConfig pluginConfig) {
        this.livesSystem = livesSystem;
        this.pluginConfig = pluginConfig;
        this.miniMessage = MiniMessage.miniMessage();
        this.pvpEnabled = false;
    }

    public boolean isPvpEnabled() {
        return pvpEnabled;
    }

    public void togglePvP() {
        this.pvpEnabled = !this.pvpEnabled;

        String messageKey = this.pvpEnabled ? "pvp-enabled" : "pvp-disabled";
        String message = pluginConfig.getString(messageKey);
        Component component = miniMessage.deserialize(message);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(component);
        }
    }


    /*@Override
    public void run() {
        String pvpStatus = pvpEnabled ? pluginConfig.getPvpEnabledMessage() : pluginConfig.getPvpDisabledMessage();
        for (Player player : Bukkit.getOnlinePlayers()) {
            int lives = livesSystem.getLives(player);
            StringBuilder heartsBuilder = new StringBuilder();
            for (int i = 0; i < lives; i++) {
                heartsBuilder.append("❤");
            }
            String hearts = heartsBuilder.toString();
            String livesMessage = pluginConfig.getLivesMessage().replace("<3>", hearts);
            if (lives <= 0) {
                livesMessage = "Has perdido";
            }
            // Combina el mensaje de las vidas con el estado del PvP
            String actionBarMessage = livesMessage + " " + pvpStatus;
            Component component = miniMessage.deserialize(actionBarMessage);
            player.sendActionBar(component);
        }
    }*/

    @Override
    public void run() {
        String pvpStatus = pvpEnabled ? pluginConfig.getPvpEnabledMessage() : pluginConfig.getPvpDisabledMessage();
        for (Player player : Bukkit.getOnlinePlayers()) {
            int lives = livesSystem.getLives(player);
            StringBuilder heartsBuilder = new StringBuilder();
            for (int i = 0; i < lives; i++) {
                heartsBuilder.append("❤");
            }
            String hearts = heartsBuilder.toString();
            if (lives <= 0) {
                hearts = "Has perdido";
            }

            // Obtén el formato del ActionBar desde la configuración y reemplaza las etiquetas
            String actionBarFormat = pluginConfig.getActionBarFormat();
            String actionBarMessage = actionBarFormat
                    .replace("<statuscombat>", pvpStatus)
                    .replace("<3>", hearts);

            // Deserializa el mensaje y envíalo al ActionBar
            Component component = miniMessage.deserialize(actionBarMessage);
            player.sendActionBar(component);
        }
    }
}
