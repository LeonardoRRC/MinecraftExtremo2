package me.meowquantum.com.mineextermo2.Commands;

import me.meowquantum.com.mineextermo2.Managers.LivesSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddLifeCommand implements CommandExecutor {

    private LivesSystem livesSystem;

    public AddLifeCommand(LivesSystem livesSystem) {
        this.livesSystem = livesSystem;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("Uso: /addlife <player>");
            return true;
        }
        if (!sender.hasPermission("mcextremo.addlife")) {
            sender.sendMessage("No tienes permisos para ejecutar ese comando.");
            return true;
        }

        Player target = sender.getServer().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Jugador no encontrado!");
            return true;
        }

        livesSystem.addLife(target);
        sender.sendMessage("Has agregado corazones al jugador " + target.getName());
        return true;
    }
}
