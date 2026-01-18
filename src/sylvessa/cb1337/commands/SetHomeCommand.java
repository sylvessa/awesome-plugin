package sylvessa.cb1337.commands;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.cb1337.Types.PluginCommand;
import sylvessa.cb1337.Main;
import sylvessa.cb1337.UserConfig;

@SuppressWarnings("unused")
public class SetHomeCommand implements PluginCommand {
    public String name() {
        return "sethome";
    }

    public String description() {
        return "Sets your home location";
    }

    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return;
        }

        Player p = (Player)sender;
        World w = p.getWorld();

        if(!w.getName().equals("world") && !w.getName().equals("world_nether")) {
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