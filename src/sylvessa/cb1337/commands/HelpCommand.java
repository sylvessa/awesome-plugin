package sylvessa.cb1337.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import sylvessa.cb1337.Main;
import sylvessa.cb1337.Types.PluginCommand;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand implements PluginCommand {

    @Override
    public String name() { return "help"; }

    @Override
    public String description() { return "Show a list of commands"; }

    @Override
    public void execute(CommandSender sender, Command c, String label, String[] args) {
        int page = 1;
        int perPage = 8;

        if(args.length > 0) {
            try { page = Integer.parseInt(args[0]); if(page < 1) page = 1; }
            catch(NumberFormatException ignored) {}
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

        if(totalPages > 1)
            sender.sendMessage("§eUse §6/help <page> §eto view other pages.");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if(args.length == 1) {
            int totalPages = (int) Math.ceil(
                    Main.getCommands().values().stream().filter(c -> !c.hidden()).count() / 8.0
            );
            for(int i = 1; i <= totalPages; i++) suggestions.add(String.valueOf(i));
        }
        return suggestions;
    }
}
