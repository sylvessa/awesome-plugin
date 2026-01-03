package sylvessa.plugin;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UserConfig {
    private final Configuration config;
    private final Map<String, Object> defaults = new HashMap<>();

    public UserConfig(Player player, JavaPlugin plugin) {
        defaults.put("joins", 0);
        defaults.put("color", "f");

        File folder = new File(plugin.getDataFolder(), "users");
        if(!folder.exists()) folder.mkdirs();

        File file = new File(folder, player.getName() + ".yml");
        config = new Configuration(file);
        config.load();

        for(Map.Entry<String, Object> entry : defaults.entrySet()) {
            if(!config.getAll().containsKey(entry.getKey())) {
                config.setProperty(entry.getKey(), entry.getValue());
            }
        }

        config.save();
    }


    public void set(String path, Object value) {
        config.setProperty(path, value);
    }

    public Object get(String path, Object def) {
        if(config.getAll().containsKey(path)) {
            return config.getProperty(path);
        }
        return def;
    }

    public int getInt(String path, int def) {
        Object val = get(path, def);
        if(val instanceof Integer) return (int) val;
        try { return Integer.parseInt(val.toString()); }
        catch(Exception e) { return def; }
    }

    public String getString(String path, String def) {
        Object val = get(path, def);
        return val.toString();
    }

    public void save() {
        config.save();
    }
}
