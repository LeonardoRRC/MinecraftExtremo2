package me.meowquantum.com.mineextermo2.Handlers;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.meowquantum.com.mineextermo2.Managers.LivesSystem;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class LivesPlaceholderExpansion extends PlaceholderExpansion {

    private JavaPlugin plugin;
    private LivesSystem livesSystem;

    public LivesPlaceholderExpansion(JavaPlugin plugin, LivesSystem livesSystem) {
        this.plugin = plugin;
        this.livesSystem = livesSystem;
    }

    @Override
    public String getIdentifier() {
        return "mcextre2";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }

        if (identifier.equals("lives")) {
            int lives = livesSystem.getLives(player);
            return getHearts(lives);
        }

        return null; // Placeholder is unknown by the expansion
    }

    private String getHearts(int lives) {
        StringBuilder hearts = new StringBuilder();
        for (int i = 0; i < lives; i++) {
            hearts.append("❤");
        }
        return hearts.toString();
    }
}
