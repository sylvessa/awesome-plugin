package sylvessa.plugin;

import org.bukkit.util.config.Configuration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PluginConfig {
    private final Configuration config;
    private final Map<String, Object> defaults = new HashMap<>();

    public PluginConfig(Main plugin) {
        defaults.put("discord.webhook-url", "");
        defaults.put("discord.bot-token", "");
        defaults.put("discord.channel-id", "");

        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) dataFolder.mkdirs();

        File file = new File(dataFolder, "config.yml");
        config = new Configuration(file);
        config.load();

        for(Map.Entry<String, Object> entry : defaults.entrySet()) {
            if(!config.getAll().containsKey(entry.getKey())) {
                config.setProperty(entry.getKey(), entry.getValue());
            }
        }

        config.save();
    }

    public Object get(String path, Object def) {
        if(config.getAll().containsKey(path)) {
            return config.getProperty(path);
        }
        return def;
    }

    public boolean getBoolean(String path, boolean def) {
        Object val = get(path, def);
        if(val instanceof Boolean) return (boolean) val;
        return Boolean.parseBoolean(val.toString());
    }

    public int getInt(String path, int def) {
        Object val = get(path, def);
        try { return Integer.parseInt(val.toString()); }
        catch(Exception e) { return def; }
    }

    public String getString(String path, String def) {
        Object val = get(path, def);
        return val.toString();
    }

    public void set(String path, Object value) {
        config.setProperty(path, value);
    }

    public void save() {
        config.save();
    }
}
