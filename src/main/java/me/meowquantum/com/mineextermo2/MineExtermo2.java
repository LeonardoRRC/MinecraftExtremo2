package me.meowquantum.com.mineextermo2;

import me.meowquantum.com.mineextermo2.Commands.AddLifeCommand;
import me.meowquantum.com.mineextermo2.Commands.HelpCommand;
import me.meowquantum.com.mineextermo2.Commands.NextResetCommand;
import me.meowquantum.com.mineextermo2.Commands.ReviveCommand;
import me.meowquantum.com.mineextermo2.Handlers.ActionBarUpdater;
import me.meowquantum.com.mineextermo2.Handlers.LivesPlaceholderExpansion;
import me.meowquantum.com.mineextermo2.Handlers.PlayTimeLimiter;
import me.meowquantum.com.mineextermo2.Handlers.PvPToggleTask;
import me.meowquantum.com.mineextermo2.Listeners.CustomDropListener;
import me.meowquantum.com.mineextermo2.Listeners.PlayerEvents;
import me.meowquantum.com.mineextermo2.Listeners.PvPListener;
import me.meowquantum.com.mineextermo2.Managers.LivesSystem;
import me.meowquantum.com.mineextermo2.Managers.PluginConfig;
import me.meowquantum.com.mineextermo2.Managers.TimeManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MineExtermo2 extends JavaPlugin {
    public LivesSystem livesSystem;
    public PluginConfig pluginConfig;
    public PlayTimeLimiter playTimeLimiter;
    public TimeManager timeManager;
    private ActionBarUpdater actionBarUpdater;
    private PvPListener pvpListener;


    @Override
    public void onEnable() {
        livesSystem = new LivesSystem(this);
        pluginConfig = new PluginConfig(this);
        timeManager = new TimeManager(this);
        timeManager.loadData();

        playTimeLimiter = new PlayTimeLimiter(this, timeManager);
        playTimeLimiter.runTaskTimer(this, 20L, 20L);
        playTimeLimiter.startResetTask();
        getServer().getPluginManager().registerEvents(new PlayerEvents(this, livesSystem, pluginConfig), this);

        actionBarUpdater = new ActionBarUpdater(livesSystem, pluginConfig);
        actionBarUpdater.runTaskTimer(this, 0, 20);
        new PvPToggleTask(actionBarUpdater).runTaskTimer(this, 0, 12000);

        pvpListener = new PvPListener(actionBarUpdater);
        getServer().getPluginManager().registerEvents(pvpListener, this);
        getServer().getPluginManager().registerEvents(new CustomDropListener(), this);
        this.getCommand("addlife").setExecutor(new AddLifeCommand(livesSystem));
        /*this.getCommand("revive").setExecutor(new ReviveCommand(livesSystem, this));*/
        this.getCommand("nextreset").setExecutor(new NextResetCommand());
        this.getCommand("ayuda").setExecutor(new HelpCommand());
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new LivesPlaceholderExpansion(this, livesSystem).register();
        }

    }

    @Override
    public void onDisable() {
        getPlayTimeLimiter().onServerShutdown();
    }

    public PlayTimeLimiter getPlayTimeLimiter() {
        return playTimeLimiter;
    }
}
