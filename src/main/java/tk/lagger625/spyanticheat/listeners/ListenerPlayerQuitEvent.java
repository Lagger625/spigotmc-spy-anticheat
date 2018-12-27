package tk.lagger625.spyanticheat.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import tk.lagger625.spyanticheat.util.SpiesManager;

/**
 *
 * @author Lagger625
 */
public class ListenerPlayerQuitEvent implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        SpiesManager.noLongerSpying(e.getPlayer());
    }
}
