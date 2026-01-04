package sylvessa.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import sylvessa.plugin.Listeners.ChatListener;
import sylvessa.plugin.Listeners.JoinListener;
import sylvessa.plugin.Listeners.SignColorListener;
import sylvessa.plugin.Listeners.TreeMobSpawnListener;
import sylvessa.plugin.commands.PluginCommand;
import sylvessa.plugin.Discord.Bot;

import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Main extends JavaPlugin {
    private static Main instance;
    private PluginConfig pluginConfig;
    private Bot discordBot;
    private static final HashMap<String, PluginCommand> commands = new HashMap<>();
    private final HashMap<String, UserConfig> userConfigs = new HashMap<>();
    private final HashMap<String, TpaRequest> tpaRequests = new HashMap<>();


    // bad bad bad!!!
    // should I move to manual registration?
    // maybe I should. Come later tho
    private void autoRegisterCommands() throws Exception {
        URL jarURL = getClass().getProtectionDomain().getCodeSource().getLocation();

        try (JarFile jar = new JarFile(new File(jarURL.toURI()))) {
            Enumeration<JarEntry> entries = jar.entries();

            while(entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();

                if(!name.startsWith("sylvessa/plugin/commands")) continue;
                if(!name.endsWith(".class")) continue;
                if(name.contains("$")) continue;
                if(name.endsWith("PluginCommand.class")) continue;

                String className = name.replace('/', '.').replace(".class", "");
                Class<?> cls = Class.forName(className);

                if(!PluginCommand.class.isAssignableFrom(cls)) continue;

                PluginCommand cmd = (PluginCommand) cls.getDeclaredConstructor().newInstance();
                commands.put(cmd.name().toLowerCase(), cmd);
                Log.info("Registered command: " + cmd.name());
            }
        }
    }

    // helpers
    public HashMap<String, UserConfig> getUserConfigs() {
        return userConfigs;
    }

    public UserConfig getUserConfig(String playerName) {
        return userConfigs.get(playerName.toLowerCase());
    }

    public static Main getInstance() {
        return instance;
    }

    public static HashMap<String, PluginCommand> getCommands() {
        return commands;
    }

    public PluginConfig getPluginConfig() {
        return pluginConfig;
    }

    public HashMap<String, TpaRequest> getTpaRequests() {
        return tpaRequests;
    }


    // binds
    public void onEnable() {
        Log.info("Initializing!");

        instance = this;
        pluginConfig = new PluginConfig(this);

        try {
            autoRegisterCommands();
        } catch (Exception ignored) {
            // bleh bleh bleh
            Log.info("FAILED TO REGISTER COMMANDS");
        }

        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_CHAT, new ChatListener(), Event.Priority.Normal,this);
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, new JoinListener(this), Event.Priority.Normal, this);
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_QUIT, new JoinListener(this), Event.Priority.Normal, this);
        getServer().getPluginManager().registerEvent(Event.Type.SIGN_CHANGE, new SignColorListener(), Event.Priority.Normal, this);
        getServer().getPluginManager().registerEvents(new TreeMobSpawnListener(), this);

        discordBot = new Bot(this);
        discordBot.start();

        Log.info("Done!");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        PluginCommand command = commands.get(cmd.getName().toLowerCase());
        if(command != null) {
            command.execute(sender, args);
            return true;
        }

        return false;
    }

    public void onDisable() {
        if(discordBot != null) {
            discordBot.stop();
        }

        Log.info("Lol Bye");
    }
}