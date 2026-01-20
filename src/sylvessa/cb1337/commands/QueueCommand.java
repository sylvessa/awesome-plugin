package sylvessa.cb1337.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.cb1337.Minigames.MinigameManager;
import sylvessa.cb1337.Minigames.MinigameType;
import sylvessa.cb1337.Types.PluginCommand;

public class QueueCommand implements PluginCommand {
    public String name() { return "queue"; }
    public String description() { return "Join a minigame queue"; }

    public boolean hidden() {
        return true;
    }

    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) return;
        Player p = (Player) sender;

        if (MinigameManager.get(p) != null) {
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
}
