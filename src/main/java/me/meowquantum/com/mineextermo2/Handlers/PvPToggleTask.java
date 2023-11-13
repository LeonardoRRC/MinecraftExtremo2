package me.meowquantum.com.mineextermo2.Handlers;

import org.bukkit.scheduler.BukkitRunnable;

public class PvPToggleTask extends BukkitRunnable {
    private final ActionBarUpdater actionBarUpdater;

    public PvPToggleTask(ActionBarUpdater actionBarUpdater) {
        this.actionBarUpdater = actionBarUpdater;
    }

    @Override
    public void run() {
        actionBarUpdater.togglePvP();
    }
}
