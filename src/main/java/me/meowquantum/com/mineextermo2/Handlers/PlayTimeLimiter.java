package me.meowquantum.com.mineextermo2.Handlers;

import me.meowquantum.com.mineextermo2.Managers.PluginConfig;
import me.meowquantum.com.mineextermo2.Managers.TimeManager;
import me.meowquantum.com.mineextermo2.MineExtermo2;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayTimeLimiter extends BukkitRunnable {
    private final MineExtermo2 plugin;
    private final TimeManager timeManager;
    private final Map<UUID, LocalDateTime> loginTimes = new HashMap<>();
    private final Map<UUID, BossBar> playerBossBars = new HashMap<>();
    private static final long MAX_PLAY_TIME = Duration.ofHours(3).toMillis();

    public PlayTimeLimiter(MineExtermo2 plugin, TimeManager timeManager) {
        this.plugin = plugin;
        this.timeManager = timeManager;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID playerId = player.getUniqueId();
            LocalDateTime loginTime = loginTimes.computeIfAbsent(playerId, k -> LocalDateTime.now());
            Duration sessionTime = Duration.between(loginTime, LocalDateTime.now());
            long playTimeToday = timeManager.getPlayTime(playerId) + sessionTime.toMillis();

            if (playTimeToday >= MAX_PLAY_TIME) {
                player.kickPlayer(ChatColor.RED + "Has excedido el tiempo máximo de juego diario.");
                removeBossBar(player);
            } else {
                updateBossBar(player, MAX_PLAY_TIME - playTimeToday);
            }
        }
    }

    private void updateBossBar(Player player, long timeLeft) {
        BossBar bossBar = playerBossBars.computeIfAbsent(player.getUniqueId(), k -> createBossBar());
        int hours = (int) (timeLeft / (1000 * 60 * 60));
        int minutes = (int) ((timeLeft / (1000 * 60)) % 60);
        int seconds = (int) ((timeLeft / 1000) % 60);

        String message = plugin.pluginConfig.getBossBarMessage()
                .replace("{hours}", String.valueOf(hours))
                .replace("{minutes}", String.valueOf(minutes))
                .replace("{seconds}", String.valueOf(seconds));

        Component component = MiniMessage.miniMessage().deserialize(message);
        String legacyText = LegacyComponentSerializer.legacySection().serialize(component);
        bossBar.setTitle(legacyText);
        bossBar.setProgress(timeLeft / (double) MAX_PLAY_TIME);
        bossBar.addPlayer(player);
        bossBar.setVisible(true);
    }

    private BossBar createBossBar() {
        return Bukkit.createBossBar("Tiempo Restante", BarColor.RED, BarStyle.SEGMENTED_10);
    }

    private void removeBossBar(Player player) {
        BossBar bossBar = playerBossBars.remove(player.getUniqueId());
        if (bossBar != null) {
            bossBar.removePlayer(player);
            bossBar.setVisible(false);
        }
    }

    public void onPlayerJoin(Player player) {
        UUID playerId = player.getUniqueId();
        loginTimes.put(playerId, LocalDateTime.now());
        BossBar bossBar = createBossBar();
        playerBossBars.put(playerId, bossBar);
        bossBar.addPlayer(player);
        bossBar.setVisible(true);
    }

    /*public void onPlayerQuit(Player player) {
        UUID playerId = player.getUniqueId();
        LocalDateTime loginTime = loginTimes.get(playerId);
        Duration sessionTime = Duration.between(loginTime, LocalDateTime.now());
        long playTimeToday = timeManager.getPlayTime(playerId) + sessionTime.toMillis();
        timeManager.setPlayTime(playerId, playTimeToday);
        loginTimes.remove(playerId);
        removeBossBar(player);
    }*/

    public void onPlayerQuit(Player player) {
        UUID playerId = player.getUniqueId();
        LocalDateTime loginTime = loginTimes.get(playerId);
        if (loginTime != null) {
            Duration sessionTime = Duration.between(loginTime, LocalDateTime.now());
            long playTime = timeManager.getPlayTime(playerId) + sessionTime.toMillis();
            timeManager.setPlayTime(playerId, playTime);
            loginTimes.remove(playerId);
            removeBossBar(player);
        }
    }

    public void onServerShutdown() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            onPlayerQuit(player);
        }
    }

    public void startResetTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                ZonedDateTime nowInPeru = ZonedDateTime.now(ZoneId.of("America/Lima"));
                if (nowInPeru.getHour() == 1 && nowInPeru.getMinute() == 0) {
                    resetPlayTimes();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 20L, 20L);
    }

    public void resetPlayTimes() {
        for (String key : timeManager.getTimeConfig().getKeys(false)) {
            timeManager.setPlayTime(UUID.fromString(key), 0);
        }
    }

}
