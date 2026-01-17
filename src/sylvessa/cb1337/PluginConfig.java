package sylvessa.cb1337;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PluginConfig {
    private final FileConfiguration config;
    private final File file;
    private final Map<String, Object> defaults = new HashMap<>();

    public PluginConfig(Main plugin) {
        defaults.put("discord.webhook-url", "");
        defaults.put("discord.bot-token", "");
        defaults.put("discord.channel-id", "");

        File dataFolder = plugin.getDataFolder();
        if(!dataFolder.exists()) dataFolder.mkdirs();

        file = new File(dataFolder, "config.yml");
        config = YamlConfiguration.loadConfiguration(file);

        for(Map.Entry<String, Object> entry : defaults.entrySet()) {
            config.addDefault(entry.getKey(), entry.getValue());
        }

        config.options().copyDefaults(true);
        save();
    }

    public boolean getBoolean(String path, boolean def) {
        return config.getBoolean(path, def);
    }

    public int getInt(String path, int def) {
        return config.getInt(path, def);
    }

    public String getString(String path, String def) {
        return config.getString(path, def);
    }

    public Object get(String path) {
        return config.get(path);
    }

    public void set(String path, Object value) {
        config.set(path, value);
    }

    public void save() {
        try {
            config.save(file);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
