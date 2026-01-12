package sylvessa.plugin.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.plugin.Duels.DuelGame;
import sylvessa.plugin.Duels.DuelManager;
import sylvessa.plugin.Main;
import sylvessa.plugin.UserConfig;

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

        DuelGame fromGame = DuelManager.get(p);
        if (fromGame != null) {
            p.sendMessage("§cYou cannot use this command while in a duel!");
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