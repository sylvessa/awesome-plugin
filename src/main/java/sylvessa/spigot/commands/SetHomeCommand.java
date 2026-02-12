package sylvessa.spigot.commands;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.spigot.Types.PluginCommand;
import sylvessa.spigot.Main;
import sylvessa.spigot.UserConfig;
import org.bukkit.command.Command;

@SuppressWarnings("unused")
public class SetHomeCommand implements PluginCommand {
    public String name() {
        return "sethome";
    }

    public String description() {
        return "Sets your home location";
    }

    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return;
        }

        Player p = (Player)sender;
        World w = p.getWorld();

        if(!w.getName().equals("world") && !w.getName().equals("world_nether") && !w.getName().equals("world_the_end")) {
            sender.sendMessage("§cYou can only set your home in the overworld or nether.");
            return;
        }

        UserConfig uc = Main.getInstance().getUserConfig(p.getName());
        if(uc == null) {
            sender.sendMessage("§cConfig not loaded.");
            return;
        }

        uc.set("home.world", p.getWorld().getName());
        uc.set("home.x", p.getLocation().getX());
        uc.set("home.y", p.getLocation().getY());
        uc.set("home.z", p.getLocation().getZ());
        uc.set("home.yaw", p.getLocation().getYaw());
        uc.set("home.pitch", p.getLocation().getPitch());
        uc.save();

        sender.sendMessage("§aHome set.");
    }
}