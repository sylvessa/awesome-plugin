package sylvessa.cb1337.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;
import sylvessa.cb1337.Types.PluginCommand;

public class SummonCommand implements PluginCommand {

    public String name() { return "summon"; }
    public String description() { return "Summon a mob"; }
    public boolean hidden() { return true; }

    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            sender.sendMessage("§cThis command can only be run from the console.");
            return;
        }

        if (args.length < 1) {
            sender.sendMessage("§7Usage: /summon <mob> [player|x y z]");
            return;
        }

        CreatureType type = null;

        for (CreatureType ct : CreatureType.values()) {
            if (ct.name().equalsIgnoreCase(args[0])) {
                type = ct;
                break;
            }
        }

        if (type == null) {
            sender.sendMessage("§cUnknown mob type.");
            return;
        }

        Location loc;

        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cConsole must specify a location.");
                return;
            }
            loc = ((Player) sender).getLocation();
        } else if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage("§cPlayer not found.");
                return;
            }
            loc = target.getLocation();
        } else if (args.length == 4) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cConsole must specify a world.");
                return;
            }

            World w = ((Player) sender).getWorld();

            double x;
            double y;
            double z;

            try {
                x = Double.parseDouble(args[1]);
                y = Double.parseDouble(args[2]);
                z = Double.parseDouble(args[3]);
            } catch (Exception e) {
                sender.sendMessage("§cInvalid coordinates.");
                return;
            }

            loc = new Location(w, x, y, z);
        } else if (args.length == 5) {
            World w = Bukkit.getWorld(args[1]);
            if (w == null) {
                sender.sendMessage("§cWorld not found.");
                return;
            }

            double x;
            double y;
            double z;

            try {
                x = Double.parseDouble(args[2]);
                y = Double.parseDouble(args[3]);
                z = Double.parseDouble(args[4]);
            } catch (Exception e) {
                sender.sendMessage("§cInvalid coordinates.");
                return;
            }

            loc = new Location(w, x, y, z);
        } else {
            sender.sendMessage("§7Usage: /summon <mob> [player|x y z]");
            return;
        }

        loc.getWorld().spawnCreature(loc, type);
        sender.sendMessage("§aSummoned §e" + type.name().toLowerCase());
    }
}
