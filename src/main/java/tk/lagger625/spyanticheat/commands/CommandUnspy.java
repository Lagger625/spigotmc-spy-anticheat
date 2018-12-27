package tk.lagger625.spyanticheat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tk.lagger625.spyanticheat.util.SpiesManager;

/**
 *
 * @author Lagger625
 */
public class CommandUnspy implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmnd, String label, String[] args) {
        if (sender instanceof Player){
            if (SpiesManager.noLongerSpying((Player)sender))
                sender.sendMessage("You are no longer spectating.");
            else
                sender.sendMessage("You aren't currently spectating.");
        }else{
            sender.sendMessage("Console can't spy.");
        }
        return true;
    }
    
}
