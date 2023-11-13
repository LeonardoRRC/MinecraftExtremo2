package me.meowquantum.com.mineextermo2.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class NextResetCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("nextreset")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                ZonedDateTime now = ZonedDateTime.now();
                ZonedDateTime nextReset = now.withZoneSameInstant(ZoneId.of("America/Lima")).withHour(1).withMinute(0).withSecond(0);

                if (now.compareTo(nextReset) > 0) {
                    nextReset = nextReset.plusDays(1);
                }

                Duration duration = Duration.between(now, nextReset);
                long hours = duration.toHours();
                long minutes = duration.minusHours(hours).toMinutes();

                player.sendMessage("El próximo reinicio será en " + hours + " horas y " + minutes + " minutos.");
            } else {
                sender.sendMessage("Este comando solo puede ser usado por un jugador.");
            }
            return true;
        }
        return false;
    }
}
