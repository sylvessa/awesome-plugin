package sylvessa.plugin.commands;

import org.bukkit.command.CommandSender;
import sylvessa.plugin.Main;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class HelpCommand implements PluginCommand {
    public String name() {
        return "help";
    }

    public String description() {
        return "Show a list of commands";
    }

    public void execute(CommandSender sender, String[] args) {
        int page = 1;
        int perPage = 8;

        if(args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
                if(page < 1) page = 1;
            } catch(NumberFormatException ignored) {}
        }

        List<PluginCommand> visibleCommands = new ArrayList<>();
        Main.getCommands().values().forEach(cmd -> {
            if(!cmd.hidden()) visibleCommands.add(cmd);
        });

        int totalPages = (int) Math.ceil(visibleCommands.size() / (double) perPage);
        if(page > totalPages) page = totalPages;

        sender.sendMessage("§e--- Commands (Page " + page + " of " + totalPages + ") ---");

        int start = (page - 1) * perPage;
        int end = Math.min(start + perPage, visibleCommands.size());

        for(int i = start; i < end; i++) {
            PluginCommand cmd = visibleCommands.get(i);
            sender.sendMessage("§6/" + cmd.name() + " §f- " + cmd.description());
        }

        if(totalPages > 1) {
            sender.sendMessage("§eUse §6/help <page> §eto view other pages.");
        }
    }
}
