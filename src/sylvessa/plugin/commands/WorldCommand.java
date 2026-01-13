package sylvessa.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

@SuppressWarnings("unused")
public class WorldCommand implements PluginCommand {
    public String name() {
        return "gwsdf";
    }

    public String description() {
        return "Goes to a world";
    }

    @Override
    public boolean hidden() {
        return true;
    }

    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("players only");
            return;
        }

        if(args.length != 1) {
            sender.sendMessage("usage: /world <name>");
            return;
        }

        if (Objects.equals(args[0], "world") || Objects.equals(args[0], "world_nether")) {
            sender.sendMessage("world not found");
            return;
        }

        World world = Bukkit.getWorld(args[0]);

        if(world == null) {
            sender.sendMessage("world not found");
            return;
        }

        Player player = (Player)sender;
        player.teleport(world.getSpawnLocation());
        player.sendMessage("teleported to " + world.getName());
    }
}
