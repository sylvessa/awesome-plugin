package sylvessa.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.plugin.Spleef.SpleefManager;

public class SpleefCommand implements PluginCommand {

    public String name() { return "spleef"; }
    public String description() { return "challenge someone to spleef"; }

    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) return;
        Player p = (Player)sender;

        if(args.length == 1) {

            if(args[0].equalsIgnoreCase("accept")) {
                Player challenger = SpleefManager.getChallenger(p);
                if(challenger != null) {
                    p.sendMessage(ChatColor.GREEN + "You accepted the spleef challenge!");
                    challenger.sendMessage(ChatColor.YELLOW + p.getName() + " accepted your spleef challenge!");
                    Bukkit.broadcastMessage(ChatColor.AQUA + "Setting up spleef arena...");
                    SpleefManager.accept(p);
                } else {
                    p.sendMessage(ChatColor.RED + "No one challenged you!");
                }
                return;
            }

            if(args[0].equalsIgnoreCase("deny")) {
                Player challenger = SpleefManager.getChallenger(p);
                if(challenger != null) {
                    p.sendMessage(ChatColor.RED + "You denied the spleef challenge.");
                    challenger.sendMessage(ChatColor.RED + p.getName() + " denied your spleef challenge.");
                    SpleefManager.deny(p);
                } else {
                    p.sendMessage(ChatColor.RED + "No one challenged you!");
                }
                return;
            }

            Player t = Bukkit.getPlayer(args[0]);
            if(t == null || t == p) {
                p.sendMessage(ChatColor.RED + "Player not found!");
                return;
            }

            if(SpleefManager.isChallenged(p, t)) {
                p.sendMessage(ChatColor.RED + "You already challenged " + t.getName() + "!");
                return;
            }

            SpleefManager.challenge(p, t);
            p.sendMessage(ChatColor.YELLOW + "You challenged " + t.getName() + " to a spleef duel!");
            t.sendMessage(ChatColor.AQUA + p.getName() + " has challenged you to a spleef duel!");
            t.sendMessage(ChatColor.GRAY + "Type " + ChatColor.GREEN + "/spleef accept" + ChatColor.GRAY + " or " + ChatColor.RED + "/spleef deny" + ChatColor.GRAY + " to respond.");
        }
    }
}
