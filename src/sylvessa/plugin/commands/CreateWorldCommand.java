package sylvessa.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

@SuppressWarnings("unused")
public class CreateWorldCommand implements PluginCommand {
    public String name() {
        return "createworld";
    }

    public String description() {
        return "Creates a world";
    }

    @Override
    public boolean hidden() {
        return true;
    }

    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof org.bukkit.entity.Player) {
            sender.sendMessage("Cant do that");
            return;
        }

        if(args.length != 2) {
            sender.sendMessage("usage: createworld <name> <normal|nether|skylands>");
            return;
        }

        String name = args[0];
        String envArg = args[1].toLowerCase();
        World.Environment env;

        if(Bukkit.getWorld(name) != null) {
            sender.sendMessage("world already exists");
            return;
        }

        if(envArg.equals("normal")) {
            env = World.Environment.NORMAL;
        } else if(envArg.equals("nether")) {
            env = World.Environment.NETHER;
        } else if(envArg.equals("skylands")) {
            env = World.Environment.SKYLANDS;
        } else {
            sender.sendMessage("invalid environment");
            return;
        }

        World world = Bukkit.getServer().createWorld(name, env);

        if(world == null) {
            sender.sendMessage("failed to create world");
            return;
        }

        sender.sendMessage("world created: " + name + " (" + env.name().toLowerCase() + ")");
    }
}
