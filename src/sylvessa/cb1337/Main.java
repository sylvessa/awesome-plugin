package sylvessa.cb1337;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import sylvessa.cb1337.Discord.Bot;
import sylvessa.cb1337.Duels.DuelListener;
import sylvessa.cb1337.Listeners.*;
import sylvessa.cb1337.Minigames.MinigameListener;
import sylvessa.cb1337.Teams.TeamManager;
import sylvessa.cb1337.Types.PluginCommand;
import sylvessa.cb1337.Types.TpaRequest;

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
    private TeamManager teamManager;


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

                if(!name.startsWith("sylvessa/cb1337/commands")) continue;
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
        String key = playerName.toLowerCase();
        UserConfig uc = userConfigs.get(key);
        if (uc != null) return uc;

        File file = new File(getDataFolder(), "users" + File.separator + key + ".yml");
        if (!file.exists()) return null;

        uc = new UserConfig(playerName, this);
        userConfigs.put(key, uc);
        return uc;
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
    public TeamManager getTeamManager() { return teamManager; }


    // binds

    @Override
    public void onEnable() {
        Log.info("Initializing!");

        instance = this;
        pluginConfig = new PluginConfig(this);
        teamManager = new TeamManager(this);

        try {
            autoRegisterCommands();
        } catch (Exception ignored) {
            // bleh bleh bleh
            Log.info("FAILED TO REGISTER COMMANDS");
        }

        getServer().getPluginManager().registerEvents(new TestListener(), this);
        getServer().getPluginManager().registerEvents(new SignColorListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new EntitySpawnListener(), this);
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new LeafDecayListener(), this);
        getServer().getPluginManager().registerEvents(new PortalListener(), this);
        getServer().getPluginManager().registerEvents(new CustomPVPListener(), this);
        getServer().getPluginManager().registerEvents(new MinigameListener(), this);
        getServer().getPluginManager().registerEvents(new DuelListener(), this);
        getServer().getPluginManager().registerEvents(new WeatherListener(), this);
        getServer().getPluginManager().registerEvents(new BlockIceListener(), this);
        getServer().getPluginManager().registerEvents(new SleepListener(), this);
        getServer().getPluginManager().registerEvents(new AFKListener(), this);


        CustomRecipes.registerAll();

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

    @Override
    public void onDisable() {
        if(discordBot != null) {
            discordBot.stop();
        }

        for (Player p : getServer().getOnlinePlayers()) {
            JoinListener.handleLeave(p, "left (server stopping)");
            p.kickPlayer(ChatColor.RED + "Server is shutting down");
        }

        Log.info("Lol Bye");
    }
}
