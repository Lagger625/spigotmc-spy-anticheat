package tk.lagger625.spyanticheat.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import tk.lagger625.spyanticheat.commands.CommandSpy;

/**
 *
 * @author Lagger625
 */
public class ListenerPlayerDamageEvent implements Listener {
    @EventHandler
    public void onPlayerDamageReceived(EntityDamageEvent e){
        if (!(e.getEntity() instanceof Player))
            return;
        CommandSpy.playerInCombat((Player)e.getEntity());
    }
    
    @EventHandler
    public void onPlayerDamageDealt(EntityDamageByEntityEvent e){
        if (!(e.getDamager() instanceof Player))
            return;
        CommandSpy.playerInCombat((Player)e.getDamager());
    }
}
