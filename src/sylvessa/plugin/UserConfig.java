package sylvessa.plugin;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UserConfig {
    private final Configuration config;

    public UserConfig(Player player, JavaPlugin plugin) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("joins", 0);
        defaults.put("color", "f");
        defaults.put("home.world", null);
        defaults.put("tpa.enabled", true);
        defaults.put("poop.last", 0);

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

    public double getDouble(String path, double def) {
        Object val = get(path, def);
        if(val instanceof Double) return (double) val;
        if(val instanceof Integer) return ((Integer) val).doubleValue();
        try { return Double.parseDouble(val.toString()); }
        catch(Exception e) { return def; }
    }

    public float getFloat(String path, float def) {
        Object val = get(path, def);
        if(val instanceof Float) return (float) val;
        if(val instanceof Double) return ((Double) val).floatValue();
        if(val instanceof Integer) return ((Integer) val).floatValue();
        try { return Float.parseFloat(val.toString()); }
        catch(Exception e) { return def; }
    }

    public long getLong(String path, long def) {
        Object val = get(path, def);
        if(val instanceof Long) return (long) val;
        if(val instanceof Integer) return ((Integer) val).longValue();
        if(val instanceof Double) return ((Double) val).longValue();
        try { return Long.parseLong(val.toString()); }
        catch(Exception e) { return def; }
    }

    public String getString(String path, String def) {
        Object val = get(path, def);
        if(val == null) return def;
        return val.toString();
    }

    public boolean getBoolean(String path, boolean def) {
        Object val = get(path, def);
        if(val instanceof Boolean) return (boolean) val;
        if(val instanceof String) return Boolean.parseBoolean((String) val);
        if(val instanceof Integer) return ((Integer) val) != 0;
        return def;
    }

    public void save() {
        config.save();
    }
}
