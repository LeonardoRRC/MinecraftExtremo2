package me.meowquantum.com.mineextermo2.Managers;

import me.meowquantum.com.mineextermo2.MineExtermo2;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class LivesSystem {
    private final PlayerLivesConfig livesConfig;

    public LivesSystem(MineExtermo2 plugin) {
        this.livesConfig = new PlayerLivesConfig(plugin);
    }

    public void setLives(Player player, int lives) {
        livesConfig.setLives(player.getUniqueId(), lives);
    }

    public int getLives(Player player) {
        return livesConfig.getLives(player.getUniqueId());
    }

    public void removeLife(Player player) {
        int currentLives = getLives(player);
        if (currentLives > 0) {
            setLives(player, currentLives - 1);
        }
    }

    public void addLife(Player player) {
        int currentLives = getLives(player);
        if (currentLives < 3) {
            setLives(player, currentLives + 1);
        }
    }

    public boolean hasLives(Player player) {
        return getLives(player) >= 0;
    }

    public void reviveOfflinePlayer(UUID playerUUID) {
        int maxLives = 3;
        livesConfig.setLives(playerUUID, maxLives);
        livesConfig.saveConfig();
    }


}
