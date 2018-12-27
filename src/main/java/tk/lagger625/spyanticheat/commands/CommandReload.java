package tk.lagger625.spyanticheat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tk.lagger625.spyanticheat.util.ConfigManager;

/**
 *
 * @author Lagger625
 */
public class CommandReload implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        ConfigManager.loadConfig();
        return true;
    }
    
}
