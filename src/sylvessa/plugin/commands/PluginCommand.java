package sylvessa.plugin.commands;

import org.bukkit.command.CommandSender;

public interface PluginCommand {
    String name();
    void execute(CommandSender sender, String[] args);
    default String description() { return "No description provided"; }
    // if true, command is hidden from /help
    default boolean hidden() { return false; }
}
