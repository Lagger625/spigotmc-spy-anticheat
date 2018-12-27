package tk.lagger625.spyanticheat.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import tk.lagger625.spyanticheat.SpyAnticheatPlugin;

/**
 *
 * @author Lagger625
 */
public class ConfigManager {
    private static File dataFile;
    private static FileConfiguration dataFc;
    
    private static float maxDistanceFromTarget;
    private static long combatCooldown;
    
    public static void loadConfig() {
        SpyAnticheatPlugin instance = SpyAnticheatPlugin.getInstance();
        FileConfiguration config = instance.getConfig();
        config.addDefault("max-distance-from-target", 3d);
        config.addDefault("combat-cooldown", 30d);
        config.options().copyDefaults(true);
        instance.saveConfig();
        
        dataFile = new File(instance.getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            try {
                dataFile.createNewFile();
            }catch (IOException e){
                instance.getLogger().log(Level.SEVERE, "Could not create data file. Disabling plugin!");
                e.printStackTrace();
                instance.getPluginLoader().disablePlugin(instance);
                return;
            }
         }

        dataFc = new YamlConfiguration();
        try {
            dataFc.load(dataFile);
        } catch (IOException | InvalidConfigurationException e) {
            instance.getLogger().log(Level.SEVERE, "Could not load data file. Disabling plugin!");
            e.printStackTrace();
            instance.getPluginLoader().disablePlugin(instance);
        }
        
        maxDistanceFromTarget = (float)config.getDouble("max-distance-from-target");
        combatCooldown = (long)(config.getDouble("combat-cooldown") * 1000f);    // store this in millis
    }
    
    public static void setData(String path, Object value){
        dataFc.set(path, value);
    }
    
    public static int getDataInt(String path){
        return dataFc.getInt(path);
    }
    
    public static float getDataFloat(String path){
        return (float)dataFc.getDouble(path);
    }
    
    public static String getDataString(String path){
        return dataFc.getString(path);
    }
    
    public static float getMaxDistanceFromTarget(){
        return maxDistanceFromTarget;
    }
    
    public static long getCombatCooldown(){
        return combatCooldown;
    }
    
    public static void saveData() throws IOException{
        dataFc.save(dataFile);
    }
    
    public static void saveConfig(){
        SpyAnticheatPlugin.getInstance().saveConfig();
    }
}
