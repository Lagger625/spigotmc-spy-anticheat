package tk.lagger625.spyanticheat.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import tk.lagger625.spyanticheat.SpyAnticheatPlugin;

/**
 *
 * @author Lagger625
 */
public class SpiesManager {
    // key is spy, value is target
    private static HashMap<Player, Player> spies = new HashMap<>();
    
    // constrain movement of spies every 5 ticks
    public static void initialize(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SpyAnticheatPlugin.getInstance(), new Runnable(){
            @Override
            public void run(){
                    for (Map.Entry<Player, Player> entry : spies.entrySet()){
                        Player spy = entry.getKey();
                        Player spiee = entry.getValue();
                        
                        if (!spiee.isOnline()){
                            noLongerSpying(spy);
                            spy.sendMessage("Your target went offline.");
                            continue;
                        }
                        
                        // make sure the spy keeps in spectator mode
                        spy.setGameMode(GameMode.SPECTATOR);
                        
                        Location spieeLoc = spiee.getLocation();
                        Location spyLoc = spy.getLocation();
                        float maxDistance = ConfigManager.getMaxDistanceFromTarget();
                        
                        boolean needTp = false;
                        
                        if (spieeLoc.getWorld() == spyLoc.getWorld()){
                            float xDiff = (float)(spyLoc.getX() - spieeLoc.getX());
                            if (Math.abs(xDiff) > maxDistance){
                                needTp = true;
                                spyLoc.setX(spieeLoc.getX() -  maxDistance * -Math.signum(xDiff));
                            }

                            float yDiff = (float)(spyLoc.getY() - spieeLoc.getY());
                            if (Math.abs(yDiff) > maxDistance){
                                needTp = true;
                                spyLoc.setY(spieeLoc.getY() - maxDistance * -Math.signum(yDiff));
                            }

                            float zDiff = (float)(spyLoc.getZ() - spieeLoc.getZ());
                            if (Math.abs(zDiff) > maxDistance){
                                needTp = true;
                                spyLoc.setZ(spieeLoc.getZ() - maxDistance * -Math.signum(zDiff));
                            }
                        }else{
                            needTp = true;
                            spyLoc = spieeLoc;
                        }
                        
                        if (needTp)
                            spy.teleport(spyLoc);
                        
                        if (spy.getSpectatorTarget() != null && spy.getSpectatorTarget() != spiee)
                            spy.setSpectatorTarget(null);
                    }
                }
            }, 0, 1);
    }
    
    public static Player getSpiee(Player spy) {
        return spies.get(spy);
    }
    
    public static boolean nowSpying(Player spy, Player spyee){ // sorry I'm no native English speaker!        
        String uuid = spy.getUniqueId().toString();
                
        ConfigManager.setData(uuid + ".target", spyee.getUniqueId().toString());
        ConfigManager.setData(uuid + ".gamemode", spy.getGameMode().toString());
        Location loc = spy.getLocation();
        ConfigManager.setData(uuid + ".location.world", loc.getWorld().getName());
        ConfigManager.setData(uuid + ".location.x", loc.getX());
        ConfigManager.setData(uuid + ".location.y", loc.getY());
        ConfigManager.setData(uuid + ".location.z", loc.getZ());
        
        for (Statistic s : Statistic.values()){
            if (s.getType() != Statistic.Type.UNTYPED)
                continue;
            int statValue = spy.getStatistic(s);
            ConfigManager.setData(uuid + "." + s, statValue);
        }
        
        try{
            ConfigManager.saveData();
        }catch(IOException e){
            SpyAnticheatPlugin.getPluginLogger().log(Level.SEVERE, "Could not save player info, aborting...");
            e.printStackTrace();
            return false;
        }
        spies.put(spy, spyee);
        return true;
    }
    
    public static boolean noLongerSpying(Player spy){
        if (!getIsPlayerSpyingFromYml(spy))
            return false;
        String uuid = spy.getUniqueId().toString();
        
        for (Statistic s : Statistic.values()){
            if (s.getType() != Statistic.Type.UNTYPED)
                continue;
            spy.setStatistic(s, ConfigManager.getDataInt(uuid + "." + s));
        }
        
        spy.teleport(new Location(SpyAnticheatPlugin.getInstance().getServer().getWorld(ConfigManager.getDataString(uuid + ".location.world")), ConfigManager.getDataFloat(uuid + ".location.x"), ConfigManager.getDataFloat(uuid + ".location.y"), ConfigManager.getDataFloat(uuid + ".location.z")));
        spy.setGameMode(GameMode.valueOf(ConfigManager.getDataString(uuid + ".gamemode")));
        
        // delete the data
        ConfigManager.setData(uuid, null);
        try {
            ConfigManager.saveData();
        } catch (IOException e) {
            SpyAnticheatPlugin.getPluginLogger().log(Level.SEVERE, "Could not clear saved temp player info!");
            e.printStackTrace();
        }
        spies.remove(spy);
        return true;
    }
    
    public static void restoreAllSpies(){
         for (Map.Entry<Player, Player> entry : spies.entrySet()){
             noLongerSpying(entry.getKey());
         }
    }
    
    public static boolean getIsPlayerSpyingFromYml(Player player){
        return ConfigManager.getDataString(player.getUniqueId().toString()) != null;
    }
    
    public static boolean getIsPlayerSpyingFromMemory(Player player){
        return spies.get(player) != null;
    }
}
