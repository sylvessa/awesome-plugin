package sylvessa.plugin.commands;

import org.bukkit.command.CommandSender;

public interface PluginCommand {
    String name();
    void execute(CommandSender sender, String[] args);
}
