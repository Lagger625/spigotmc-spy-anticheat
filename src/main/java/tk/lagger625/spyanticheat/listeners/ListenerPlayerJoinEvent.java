package tk.lagger625.spyanticheat.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import tk.lagger625.spyanticheat.util.SpiesManager;

/**
 *
 * @author Lagger625
 */
public class ListenerPlayerJoinEvent implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e){
        SpiesManager.noLongerSpying(e.getPlayer());
    }
}
