package sylvessa.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.plugin.Main;
import sylvessa.plugin.UserConfig;

public class HomeCommand implements PluginCommand {
    public String name() {
        return "home";
    }

    public String description() {
        return "Teleport to your home";
    }

    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return;
        }

        Player p = (Player) sender;
        UserConfig uc = Main.getInstance().getUserConfig(p.getName());
        if(uc == null) {
            sender.sendMessage("Config not loaded.");
            return;
        }

        String worldName = uc.getString("home.world", null);
        if(worldName == null) {
            sender.sendMessage("You do not have a home set.");
            return;
        }

        World w = Bukkit.getWorld(worldName);
        if(w == null) {
            sender.sendMessage("Home world no longer exists.");
            return;
        }

        double x = uc.getDouble("home.x", 0);
        double y = uc.getDouble("home.y", 0);
        double z = uc.getDouble("home.z", 0);
        float yaw = (float) uc.getDouble("home.yaw", 0);
        float pitch = (float) uc.getDouble("home.pitch", 0);

        Location loc = new Location(w, x, y, z, yaw, pitch);
        p.teleport(loc);

        sender.sendMessage("Teleported home.");
    }
}
