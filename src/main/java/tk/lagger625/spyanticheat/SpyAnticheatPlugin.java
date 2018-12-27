package tk.lagger625.spyanticheat;

import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import tk.lagger625.spyanticheat.commands.CommandReload;
import tk.lagger625.spyanticheat.commands.CommandSpy;
import tk.lagger625.spyanticheat.commands.CommandUnspy;
import tk.lagger625.spyanticheat.listeners.ListenerPlayerDamageEvent;
import tk.lagger625.spyanticheat.listeners.ListenerPlayerJoinEvent;
import tk.lagger625.spyanticheat.listeners.ListenerPlayerQuitEvent;
import tk.lagger625.spyanticheat.util.ConfigManager;
import tk.lagger625.spyanticheat.util.SpiesManager;

/**
 *
 * @author Lagger625
 */
public class SpyAnticheatPlugin extends JavaPlugin {
    private static Logger logger;
    private static SpyAnticheatPlugin instance;
    
    public static Logger getPluginLogger(){
        return logger;
    }
    
    public static SpyAnticheatPlugin getInstance(){
        return instance;
    }    
    
    @Override
    public void onEnable(){
        instance = this;
        logger = getServer().getLogger();
        ConfigManager.loadConfig();
        
        this.getCommand("spy").setExecutor(new CommandSpy());
        this.getCommand("unspy").setExecutor(new CommandUnspy());
        this.getCommand("spareload").setExecutor(new CommandReload());
        getServer().getPluginManager().registerEvents(new ListenerPlayerJoinEvent(), this);
        getServer().getPluginManager().registerEvents(new ListenerPlayerQuitEvent(), this);
        getServer().getPluginManager().registerEvents(new ListenerPlayerDamageEvent(), this);
        SpiesManager.initialize();
    }
    
    @Override
    public void onDisable(){
        SpiesManager.restoreAllSpies();
    }
}
