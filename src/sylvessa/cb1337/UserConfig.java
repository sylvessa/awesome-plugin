package sylvessa.cb1337;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UserConfig {
    private final FileConfiguration config;
    private final File file;

    public UserConfig(Player player, JavaPlugin plugin) {
        this(player.getName(), plugin);
    }

    public UserConfig(String playerName, JavaPlugin plugin) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("joins", 0);
        defaults.put("color", "f");
        defaults.put("home.world", null);
        defaults.put("home.public", false);
        defaults.put("tpa.enabled", true);
        defaults.put("poop.last", 0);

        File folder = new File(plugin.getDataFolder(), "users");
        if(!folder.exists()) folder.mkdirs();

        file = new File(folder, playerName + ".yml");
        config = YamlConfiguration.loadConfiguration(file);

        for(Map.Entry<String, Object> entry : defaults.entrySet()) {
            config.addDefault(entry.getKey(), entry.getValue());
        }

        config.options().copyDefaults(true);
        save();
    }

    public void set(String path, Object value) {
        config.set(path, value);
    }

    public Object get(String path, Object def) {
        return config.contains(path) ? config.get(path) : def;
    }

    public int getInt(String path, int def) {
        return config.getInt(path, def);
    }

    public double getDouble(String path, double def) {
        return config.getDouble(path, def);
    }

    public float getFloat(String path, float def) {
        return (float) config.getDouble(path, def);
    }

    public long getLong(String path, long def) {
        return config.getLong(path, def);
    }

    public String getString(String path, String def) {
        return config.getString(path, def);
    }

    public boolean getBoolean(String path, boolean def) {
        return config.getBoolean(path, def);
    }

    public void save() {
        try {
            config.save(file);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}