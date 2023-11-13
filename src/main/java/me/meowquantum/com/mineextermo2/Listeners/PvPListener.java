package me.meowquantum.com.mineextermo2.Listeners;

import me.meowquantum.com.mineextermo2.Handlers.ActionBarUpdater;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvPListener implements Listener {
    private final ActionBarUpdater actionBarUpdater;

    public PvPListener(ActionBarUpdater actionBarUpdater) {
        this.actionBarUpdater = actionBarUpdater;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            if (!actionBarUpdater.isPvpEnabled()) {
                event.setCancelled(true);
            }
        }
    }
}
