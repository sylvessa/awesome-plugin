package sylvessa.cb1337.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import sylvessa.cb1337.Types.PluginCommand;

public class SummonCommand implements PluginCommand {

    public String name() { return "summon"; }
    public String description() { return "Summon an entity"; }
    public boolean hidden() { return true; }

    public void execute(CommandSender sender, String[] args) {
        boolean isScorner = sender instanceof Player && sender.getName().equals("Scorner");
        boolean isConsole = !(sender instanceof Player);

        if (!isScorner && !isConsole) {
            sender.sendMessage("§cThis command can only be run from the console or by Scorner.");
            return;
        }

        if (args.length < 1) {
            sender.sendMessage("§7Usage: /summon <entity> [player|x y z|world x y z] [count]");
            return;
        }

        EntityType type;

        try {
            type = EntityType.valueOf(args[0].toUpperCase().replace("-", "_"));
        } catch (Exception e) {
            sender.sendMessage("§cUnknown entity type.");
            return;
        }

        int count = 1;
        int lastArgIndex = args.length - 1;

        // check if last arg is a number for count
        try {
            count = Integer.parseInt(args[lastArgIndex]);
            lastArgIndex--;
            if (count < 1) count = 1;
        } catch (Exception ignored) {}

        Location loc = null;

        if (lastArgIndex == 0) { // only entity
            if (isScorner) {
                loc = ((Player)sender).getLocation();
            } else {
                sender.sendMessage("§cConsole must specify a location.");
                return;
            }
        } else if (lastArgIndex == 1) { // /summon <entity> <player>
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage("§cPlayer not found.");
                return;
            }
            loc = target.getLocation();
        } else if (lastArgIndex == 3) { // /summon <entity> x y z
            sender.sendMessage("§cConsole must specify a world.");
            return;
        } else if (lastArgIndex == 4) { // /summon <entity> <world> x y z
            World w = Bukkit.getWorld(args[1]);
            if (w == null) {
                sender.sendMessage("§cWorld not found.");
                return;
            }

            double x, y, z;
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
            sender.sendMessage("§7Usage: /summon <entity> [player|x y z|world x y z] [count]");
            return;
        }

        for (int i = 0; i < count; i++) {
            loc.getWorld().spawn(loc, type.getEntityClass());
        }

        sender.sendMessage("§aSummoned §e" + type.name().toLowerCase() + " §ax" + count);
    }
}
