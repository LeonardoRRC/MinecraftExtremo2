package me.meowquantum.com.mineextermo2.Listeners;

import me.meowquantum.com.mineextermo2.Managers.LivesSystem;
import me.meowquantum.com.mineextermo2.Managers.PluginConfig;
import me.meowquantum.com.mineextermo2.MineExtermo2;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class PlayerEvents implements Listener {

    private final MineExtermo2 plugin;
    private final LivesSystem livesSystem;
    private final PluginConfig pluginConfig;
    private final MiniMessage miniMessage;

    public PlayerEvents(MineExtermo2 plugin, LivesSystem livesSystem, PluginConfig pluginConfig) {
        this.plugin = plugin;
        this.livesSystem = livesSystem;
        this.pluginConfig = pluginConfig;
        this.miniMessage = MiniMessage.builder().tags(TagResolver.builder().resolver(TagResolver.standard()).build()).build();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        livesSystem.removeLife(player);
        int lives = livesSystem.getLives(player);

        if (lives > 0) {
            sendMultiLineMessage(pluginConfig.getDeathMessage().replace("<player>", player.getName()));
            Component titleComponent = miniMessage.deserialize(pluginConfig.getLostLifeTitle());
            Component subtitleComponent = miniMessage.deserialize(pluginConfig.getLostLifeSubTitle());
            Title title = Title.title(titleComponent, subtitleComponent, Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(3500), Duration.ofMillis(1000)));
            player.showTitle(title);
        } else {
            banPlayer(player, ChatColor.RED + "Te has quedado sin vidas.");
            sendMultiLineMessage(pluginConfig.getOutOfLivesMessage().replace("<player>", player.getName()));
            Component titleComponent = miniMessage.deserialize(pluginConfig.getOutOfLivesTitle());
            Component subtitleComponent = miniMessage.deserialize(pluginConfig.getOutOfLivesSubtitle());
            Title title = Title.title(titleComponent, subtitleComponent, Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(3500), Duration.ofMillis(1000)));
            player.showTitle(title);
        }
    }

    private void banPlayer(Player player, String reason) {
        Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), reason, null, null);
        player.kick(Component.text(reason));
    }

    private void sendMultiLineMessage(String message) {
        Arrays.stream(message.split("\n")).forEach(line -> {
            Component component = miniMessage.deserialize(line);
            Bukkit.getServer().sendMessage(component);
        });
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getPlayTimeLimiter().onPlayerJoin(event.getPlayer());
        Player player = event.getPlayer();
        event.setJoinMessage("");
        if (!player.hasPlayedBefore()) {
            player.setGameMode(GameMode.SPECTATOR);
            player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6&lGenerando ubicación"), "Por favor, espera...", 10, 1800, 20);

            CompletableFuture<Location> futureLocation = generateSafeRandomLocation(player.getWorld());

            futureLocation.thenAccept(location -> {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    if (location != null) {
                        player.teleport(location);
                        player.setGameMode(GameMode.SURVIVAL);
                        player.resetTitle();
                    } else {
                        player.teleport(player.getWorld().getSpawnLocation());
                        player.setGameMode(GameMode.SURVIVAL);
                        player.resetTitle();
                        player.sendMessage(ChatColor.RED + "No se pudo encontrar una ubicación segura. Has sido teletransportado al spawn.");
                    }
                });
            });
        }

        if (!livesSystem.hasLives(player)) {
            livesSystem.setLives(player, 3);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage("");
        plugin.getPlayTimeLimiter().onPlayerQuit(event.getPlayer());
    }

    private CompletableFuture<Location> generateSafeRandomLocation(World world) {
        Random random = new Random();
        int maxTries = 100; // Número máximo de intentos para encontrar una ubicación segura

        CompletableFuture<Location> locationFuture = CompletableFuture.supplyAsync(() -> {
            for (int i = 0; i < maxTries; i++) {
                int x = random.nextInt(10001) - 5000; // Genera un número aleatorio entre -5000 y 5000
                int z = random.nextInt(10001) - 5000;
                int chunkX = x >> 4;
                int chunkZ = z >> 4;
                Chunk chunk = world.getChunkAtAsync(chunkX, chunkZ, true).join();

                int y = world.getHighestBlockYAt(x, z);
                Location loc = new Location(world, x + 0.5, y, z + 0.5);

                if (isLocationSafe(loc)) {
                    return loc;
                }
            }
            return world.getSpawnLocation();
        });

        return locationFuture;
    }

    private boolean isLocationSafe(Location location) {
        Block feet = location.getBlock();
        Block head = feet.getRelative(BlockFace.UP);
        Block ground = feet.getRelative(BlockFace.DOWN);
        return !feet.getType().isSolid() && !head.getType().isSolid() && ground.getType().isSolid();
    }



}
