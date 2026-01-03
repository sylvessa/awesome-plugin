package sylvessa.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import sylvessa.plugin.commands.PluginCommand;

import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@SuppressWarnings("unused")
public class Main extends JavaPlugin {
    private final HashMap<String, PluginCommand> commands = new HashMap<>();

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


    public void onEnable() {
        Log.info("Initializing!");

        try {
            autoRegisterCommands();
        } catch (Exception ignored) {
            // bleh bleh bleh
            Log.info("FAILED TO REGISTER COMMANDS");
        }

        ChatListener chatListener = new ChatListener();
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(
                org.bukkit.event.Event.Type.PLAYER_CHAT,
                chatListener,
                org.bukkit.event.Event.Priority.Normal,
                this
        );

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
        Log.info("Lol Bye");
    }
}