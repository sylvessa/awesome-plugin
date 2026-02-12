package sylvessa.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.spigot.Duels.DuelManager;
import sylvessa.spigot.Duels.DuelType;
import sylvessa.spigot.Minigames.MinigameManager;
import sylvessa.spigot.Types.PluginCommand;
import org.bukkit.command.Command;

import java.util.ArrayList;
import java.util.List;

public class DuelCommand implements PluginCommand {
    public String name() { return "duel"; }
    public String description() { return "challenge someone to a duel"; }

    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) return;
        Player p = (Player)sender;

        if (p.getWorld().getName().equals("creative")) {
            p.sendMessage(ChatColor.RED + "Exit creative first");
            return;
        }

        if (MinigameManager.get(p) != null || MinigameManager.getQueued(p) != null || DuelManager.get(p) != null) {
            p.sendMessage(ChatColor.RED + "You cannot use this command right now!");
            return;
        }

        if(args.length == 0) {
            p.sendMessage(ChatColor.AQUA + "Available duel modes:");
            for(DuelType t : DuelType.values()) {
                p.sendMessage(ChatColor.YELLOW + "- " + t.name().toLowerCase());
            }
            p.sendMessage(ChatColor.GRAY + "Usage: /duel <mode> <player>");
            return;
        }

        if(args.length == 1) {

            if(args[0].equalsIgnoreCase("accept")) {
                Player challenger = DuelManager.getChallenger(p);
                if(challenger != null) {
                    p.sendMessage(ChatColor.GREEN + "You accepted the duel!");
                    challenger.sendMessage(ChatColor.YELLOW + p.getName() + " accepted your duel!");
                    Bukkit.broadcastMessage(ChatColor.AQUA + "Setting up duel...");
                    DuelManager.accept(p);
                } else {
                    p.sendMessage(ChatColor.RED + "No one challenged you!");
                }
                return;
            }

            if(args[0].equalsIgnoreCase("deny")) {
                Player challenger = DuelManager.getChallenger(p);
                if(challenger != null) {
                    p.sendMessage(ChatColor.RED + "You denied the duel.");
                    challenger.sendMessage(ChatColor.RED + p.getName() + " denied your duel.");
                    DuelManager.deny(p);
                } else {
                    p.sendMessage(ChatColor.RED + "No one challenged you!");
                }
                return;
            }

            p.sendMessage(ChatColor.RED + "Usage: /duel <mode> <player>");
            return;
        }

        if(args.length != 2) {
            p.sendMessage(ChatColor.RED + "Usage: /duel <mode> <player>");
            return;
        }

        DuelType type;
        try {
            type = DuelType.valueOf(args[0].toUpperCase());
        } catch(Exception e) {
            p.sendMessage(ChatColor.RED + "Unknown duel mode!");
            return;
        }

        Player t = Bukkit.getPlayer(args[1]);
        if(t == null || t == p) {
            p.sendMessage(ChatColor.RED + "Player not found!");
            return;
        }

        if(DuelManager.isChallenged(p, t)) {
            p.sendMessage(ChatColor.RED + "You already challenged " + t.getName() + "!");
            return;
        }

        DuelManager.challenge(p, t, type);

        p.sendMessage(ChatColor.YELLOW + "You challenged " + t.getName() + " to a " + type.name().toLowerCase() + " duel!");
        t.sendMessage(ChatColor.AQUA + p.getName() + " has challenged you to a " + type.name().toLowerCase() + " duel!");
        t.sendMessage(ChatColor.GRAY + "Type " + ChatColor.GREEN + "/duel accept" + ChatColor.GRAY + " or " + ChatColor.RED + "/duel deny" + ChatColor.GRAY + " to respond.");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if(args.length == 1) {
            for(DuelType t : DuelType.values()) {
                String name = t.name().toLowerCase();
                if(name.startsWith(args[0].toLowerCase())) suggestions.add(name);
            }
            if("accept".startsWith(args[0].toLowerCase())) suggestions.add("accept");
            if("deny".startsWith(args[0].toLowerCase())) suggestions.add("deny");
        } else if(args.length == 2) {
            String prefix = args[1].toLowerCase();
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!p.equals(sender) && p.getName().toLowerCase().startsWith(prefix)) {
                    suggestions.add(p.getName());
                }
            }
        }

        return suggestions;
    }
}
