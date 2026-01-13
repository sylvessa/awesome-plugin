package sylvessa.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class RideCommand implements PluginCommand {

    private static final Map<Player, Player> requests = new HashMap<>();

    public String name() {
        return "ride";
    }

    public boolean hidden() {
        return false;
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;

        Player rider = (Player) sender;

        if (args.length == 1 && args[0].equalsIgnoreCase("accept")) {
            Player requester = requests.remove(rider);
            if (requester == null) {
                rider.sendMessage("no ride request");
                return;
            }

            rider.setPassenger(requester);
            requester.sendMessage("you are now riding " + rider.getName());
            rider.sendMessage("you are being ridden");
            return;
        }

        if (args.length != 1) {
            rider.sendMessage("/ride <player>");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            rider.sendMessage("player not found");
            return;
        }

        if (target == rider) {
            rider.sendMessage("no");
            return;
        }

        requests.put(target, rider);
        rider.sendMessage("ride request sent to " + target.getName());
        target.sendMessage(rider.getName() + " wants to ride you. /ride accept");
    }
}
