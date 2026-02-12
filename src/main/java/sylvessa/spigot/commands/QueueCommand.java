package sylvessa.spigot.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.spigot.Duels.DuelManager;
import sylvessa.spigot.Minigames.MinigameManager;
import sylvessa.spigot.Minigames.MinigameType;
import sylvessa.spigot.Types.PluginCommand;

import java.util.ArrayList;
import java.util.List;

public class QueueCommand implements PluginCommand {
    public String name() { return "queue"; }

    public String description() { return "Join a minigame queue"; }

    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) return;
        Player p = (Player) sender;

        if (p.getWorld().getName().equals("creative")) {
            p.sendMessage(ChatColor.RED + "Exit creative first");
            return;
        }

        if (MinigameManager.get(p) != null || DuelManager.get(p) != null) {
            p.sendMessage(ChatColor.RED + "You cannot run this command at this time.");
            return;
        }

        if(args.length == 0) {
            if(MinigameManager.isQueued(p)) {
                MinigameManager.remove(p);
                p.sendMessage(ChatColor.RED + "You left the queue.");
                return;
            }

            p.sendMessage(ChatColor.AQUA + "Available minigames:");
            for(MinigameType t : MinigameType.values()) {
                p.sendMessage(ChatColor.YELLOW + "- " + t.name().toLowerCase());
            }
            p.sendMessage(ChatColor.GRAY + "Usage: /queue <game>");
            return;
        }

        String gameArg = args[0].toUpperCase();
        MinigameType type;
        try {
            type = MinigameType.valueOf(gameArg);
        } catch(Exception e) {
            p.sendMessage(ChatColor.RED + "Unknown minigame!");
            return;
        }

        MinigameManager.queue(p, type);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if(args.length == 1) {
            for(MinigameType t : MinigameType.values()) {
                String name = t.name().toLowerCase();
                if(name.startsWith(args[0].toLowerCase())) suggestions.add(name);
            }
        }
        return suggestions;
    }
}
