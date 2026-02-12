package sylvessa.spigot.Discord;

import sylvessa.spigot.Log;
import sylvessa.spigot.Discord.Types.SlashCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class CommandManager extends ListenerAdapter {

    private final Map<String, SlashCommand> commands = new HashMap<>();

    public CommandManager() {
        registerCommands();
    }

    private void registerCommands() {
        try {
            URL jarURL = getClass().getProtectionDomain().getCodeSource().getLocation();

            try (JarFile jar = new JarFile(new File(jarURL.toURI()))) {
                Enumeration<JarEntry> entries = jar.entries();

                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();

                    if (!name.startsWith("sylvessa/spigot/Discord/Commands/")) continue;
                    if (!name.endsWith(".class")) continue;
                    if (name.contains("$")) continue;

                    String className = name.replace('/', '.').replace(".class", "");
                    Class<?> clazz = Class.forName(className);

                    if (!SlashCommand.class.isAssignableFrom(clazz)) continue;
                    if (clazz.isInterface()) continue;

                    SlashCommand cmd = (SlashCommand) clazz.getDeclaredConstructor().newInstance();
                    commands.put(cmd.getName(), cmd);

                    Log.info("Registered Discord command: " + cmd.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerToDiscord(JDA jda) {
        List<net.dv8tion.jda.api.interactions.commands.build.CommandData> data = new ArrayList<>();
        for (SlashCommand cmd : commands.values()) {
            data.add(cmd.getCommandData());
        }
        jda.updateCommands().addCommands(data).queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        SlashCommand cmd = commands.get(event.getName());
        if (cmd != null) {
            cmd.execute(event);
        }
    }

    private List<Class<?>> getClasses(String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        try {
            String path = packageName.replace('.', '/');
            Enumeration<java.net.URL> resources =
                    Thread.currentThread().getContextClassLoader().getResources(path);

            while (resources.hasMoreElements()) {
                java.net.URL resource = resources.nextElement();
                java.io.File dir = new java.io.File(resource.toURI());

                for (String file : dir.list()) {
                    if (file.endsWith(".class")) {
                        String className = packageName + "." + file.substring(0, file.length() - 6);
                        classes.add(Class.forName(className));
                    }
                }
            }
        } catch (Exception ignored) {}
        return classes;
    }
}
