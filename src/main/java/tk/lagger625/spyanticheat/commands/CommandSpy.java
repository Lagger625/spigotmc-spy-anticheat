package tk.lagger625.spyanticheat.commands;

import java.util.HashMap;
import static org.bukkit.Bukkit.getServer;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tk.lagger625.spyanticheat.util.ConfigManager;
import tk.lagger625.spyanticheat.util.SpiesManager;

/**
 *
 * @author Lagger625
 */
public class CommandSpy implements CommandExecutor {
    private static HashMap<Player, Long> playerCombatCounters = new HashMap<Player, Long>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmnd, String label, String[] args) {
        if (sender instanceof Player){
            if (args.length != 1)
                return false;
            Player player = (Player)sender;
            
            if (Math.abs(player.getVelocity().lengthSquared()) >= 0.01){
                sender.sendMessage("You must be standing still in order to spy another player.");
                return true;
            }
            if (playerCombatCounters.containsKey(player)){
                float cooldownLeft = (playerCombatCounters.get(player) - System.currentTimeMillis()) / 1000f;
                if (cooldownLeft > 0){
                    sender.sendMessage("You must be " + String.format("%.2f", cooldownLeft) + " more seconds out of combat to spectate." );
                    return true;
                }
            }
            Player target = getServer().getPlayer(args[0]);
            if (target == null || !target.isOnline()){
                sender.sendMessage("This player is not currently online.");
                return true;
            }
            if (target == player){
                sender.sendMessage("You cannot spy on yourself!");
                return true;
            }
            if (SpiesManager.getIsPlayerSpyingFromMemory(target)){
                sender.sendMessage("This player is currently spying another one.");
                return true;
            }
            if (!SpiesManager.nowSpying(player, target)){
                sender.sendMessage("Could not save your current location and stats! Aborting...");
                return true;
            }
            SpiesManager.nowSpying(player, target);
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(target);
            return true;
        }else{
            sender.sendMessage("Console can't spy.");
            return true;
        }
    }
 
    public static void playerInCombat(Player player){
        playerCombatCounters.put(player, System.currentTimeMillis() + ConfigManager.getCombatCooldown());
    }
}
