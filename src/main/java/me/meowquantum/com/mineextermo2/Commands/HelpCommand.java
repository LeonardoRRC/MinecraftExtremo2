package me.meowquantum.com.mineextermo2.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class HelpCommand implements CommandExecutor {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ayuda")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                openHelpBook(player);
            } else {
                sender.sendMessage(ChatColor.RED + "Este comando solo puede ser usado por un jugador.");
            }
            return true;
        }
        return false;
    }

    private void openHelpBook(Player player) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        Component page1 = miniMessage.deserialize("<color:#ff312e><b>Vidas</b></color>\n\nUsted aparece con <color:#ff312e><b>3 Vidas</b></color> los cuales si los pierdes, perderas el juego y no podras respawnear.");
        Component page2 = miniMessage.deserialize("<color:#ff312e><b>Tiempos</b></color>\n\nSolo podras jugar 3 horas en esta modalidad, cada dia a la 1AM Hora meximo los tiempos seran reiniciados y tendras otras 3 horas.");
        Component page3 = miniMessage.deserialize("<color:#ff312e><b>Dropeos</b></color>\n\nLos mobs y bloques tienen algunos dropeos custom asi mismo los mobs como creepers tienen habilidades extras asi que anda con cuidado.");
        Component page4 = miniMessage.deserialize("<color:#ff312e><b>Hogares</b></color>\n\nLos usuarios comunes tienen acceso a crear 2 homes, Si nesecitas más homes puedes comprarlos desde la tienda oficial del servidor.");
        meta.addPages(page1, page2, page3, page4);
        meta.setAuthor("El Servidor");
        meta.setTitle("Guía del Usuario");
        book.setItemMeta(meta);
        player.openBook(book);
    }
}
