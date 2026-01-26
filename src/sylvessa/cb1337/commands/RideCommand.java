package sylvessa.cb1337.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.cb1337.Duels.DuelGame;
import sylvessa.cb1337.Duels.DuelManager;
import sylvessa.cb1337.Minigames.MinigameManager;
import sylvessa.cb1337.Types.PluginCommand;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class RideCommand implements PluginCommand {

    private static final Map<Player, Player> requests = new HashMap<>();

    public String name() {
        return "ride";
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;

        Player rider = (Player) sender;

        if (MinigameManager.get(rider) != null || MinigameManager.getQueued(rider) != null || DuelManager.get(rider) != null) {
            rider.sendMessage("§cYou cannot use this command right now!");
            return;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("accept")) {
            Player requester = requests.remove(rider);
            if (requester == null) {
                rider.sendMessage(ChatColor.RED + "You have no pending ride requests.");
                return;
            }

            rider.setPassenger(requester);
            requester.sendMessage(ChatColor.GREEN + "You are now riding " + ChatColor.YELLOW + rider.getName() + ChatColor.GREEN + ".");
            rider.sendMessage(ChatColor.AQUA + requester.getName() + ChatColor.GREEN + " is now riding you.");
            return;
        }

        if (args.length != 1) {
            rider.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/ride <player>");
            rider.sendMessage(ChatColor.GRAY + "Send a request to ride another player.");
            return;
        }

        DuelGame fromGame = DuelManager.get(rider);
        if (fromGame != null) {
            rider.sendMessage("§cYou cannot use this command while in a duel!");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            rider.sendMessage(ChatColor.RED + "That player is not online.");
            return;
        }

        if (target == rider) {
            rider.sendMessage(ChatColor.RED + "You cannot ride yourself.");
            return;
        }

        if (target.getWorld() != rider.getWorld()) {
            rider.sendMessage(ChatColor.RED + "You two are not in the same world.");
            return;
        }

        DuelGame toGame = DuelManager.get(target);
        if (toGame != null) {
            target.sendMessage("§cThe other user is in a duel, please wait!");
            return;
        }

        requests.put(target, rider);
        rider.sendMessage(ChatColor.GREEN + "Ride request sent to " + ChatColor.YELLOW + target.getName() + ChatColor.GREEN + ".");
        target.sendMessage(ChatColor.AQUA + rider.getName() + ChatColor.YELLOW + " wants to ride you.");
        target.sendMessage(ChatColor.GRAY + "Type " + ChatColor.GREEN + "/ride accept" + ChatColor.GRAY + " to accept it.");
    }
}

