package sylvessa.plugin.commands;

import org.bukkit.command.CommandSender;
import sylvessa.plugin.Main;

@SuppressWarnings("unused")
public class HelpCommand implements PluginCommand {
    public String name() {
        return "help";
    }

    public String description() {
        return "Show a list of commands";
    }

    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("§e--- Commands ---");
        Main.getCommands().values().forEach(cmd -> {
            if (!cmd.hidden()) {
                sender.sendMessage("§6/" + cmd.name() + " §f- " + cmd.description());
            }
        });
    }
}
