package me.meowquantum.com.mineextermo2.Commands;

import me.meowquantum.com.mineextermo2.Managers.LivesSystem;
import me.meowquantum.com.mineextermo2.MineExtermo2;
import org.bukkit.BanList;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReviveCommand implements CommandExecutor {
    private final LivesSystem livesSystem;
    private final MineExtermo2 plugin;

    public ReviveCommand(LivesSystem livesSystem, MineExtermo2 plugin) {
        this.livesSystem = livesSystem;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("Uso correcto: /revive <jugador>");
            return true;
        }

        if (!sender.hasPermission("mcextremo.revive")) {
            sender.sendMessage("No tienes permiso para ejecutar este comando.");
            return true;
        }

        OfflinePlayer target = plugin.getServer().getOfflinePlayer(args[0]);
        if (target == null || !target.hasPlayedBefore()) {
            sender.sendMessage("Jugador no encontrado o nunca se ha conectado.");
            return true;
        }

        livesSystem.reviveOfflinePlayer(target.getUniqueId());

        if (plugin.getServer().getBanList(BanList.Type.NAME).isBanned(target.getName())) {
            plugin.getServer().getBanList(BanList.Type.NAME).pardon(target.getName());
            sender.sendMessage("El jugador " + target.getName() + " ha sido revivido.");
        } else {
            sender.sendMessage("El jugador " + target.getName() + " ha sido revivido.");
        }

        return true;
    }
}

