package sylvessa.cb1337.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.cb1337.Duels.DuelGame;
import sylvessa.cb1337.Duels.DuelManager;
import sylvessa.cb1337.Main;
import sylvessa.cb1337.Minigames.MinigameManager;
import sylvessa.cb1337.Types.PluginCommand;
import sylvessa.cb1337.UserConfig;

public class HomeCommand implements PluginCommand {

    public String name() {
        return "home";
    }

    public String description() {
        return "Teleport to your home or another players public home";
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return;
        }

        Player p = (Player) sender;

        DuelGame fromGame = DuelManager.get(p);
        if (fromGame != null) {
            p.sendMessage("§cYou cannot use this command while in a duel!");
            return;
        }

        if (MinigameManager.get(p) != null) {
            p.sendMessage("§cYou cannot use this command while in a minigame!");
            return;
        }

        if (p.getWorld().getName().equals("creative")) {
            p.sendMessage(ChatColor.RED + "Run /creative");
            return;
        }

        // /home -> own home
        if (args.length == 0) {
            UserConfig uc = Main.getInstance().getUserConfig(p.getName());
            if (uc == null) {
                p.sendMessage("§cConfig not loaded.");
                return;
            }
            teleportToHome(p, uc, "§aTeleported home.");
            return;
        }

        // /home <username>
        String targetName = args[0];
        UserConfig targetUc = Main.getInstance().getUserConfig(targetName);

        if (targetUc == null) {
            p.sendMessage("§cThat player does not exist or is not loaded.");
            return;
        }

        if (!targetUc.getBoolean("home.public", false)) {
            p.sendMessage("§cThat player's home is not public.");
            return;
        }

        String worldName = targetUc.getString("home.world", null);
        if (worldName == null) {
            p.sendMessage("§eThat player does not have a home set.");
            return;
        }

        World w = Bukkit.getWorld(worldName);
        if (w == null) {
            p.sendMessage("§cThat home world no longer exists.");
            return;
        }

        double x = targetUc.getDouble("home.x", 0);
        double y = targetUc.getDouble("home.y", 0);
        double z = targetUc.getDouble("home.z", 0);
        float yaw = (float) targetUc.getDouble("home.yaw", 0);
        float pitch = (float) targetUc.getDouble("home.pitch", 0);

        String color = targetUc.getString("color", "f");
        String name = "§" + color + targetName + "§f";

        p.teleport(new Location(w, x, y, z, yaw, pitch));
        p.sendMessage("§aTeleported to " + name + "§a's home.");
    }

    private void teleportToHome(Player p, UserConfig uc, String successMsg) {
        String worldName = uc.getString("home.world", null);
        if (worldName == null) {
            p.sendMessage("§eYou do not have a home set.");
            return;
        }

        World w = Bukkit.getWorld(worldName);
        if (w == null) {
            p.sendMessage("§cHome world no longer exists.");
            return;
        }

        double x = uc.getDouble("home.x", 0);
        double y = uc.getDouble("home.y", 0);
        double z = uc.getDouble("home.z", 0);
        float yaw = (float) uc.getDouble("home.yaw", 0);
        float pitch = (float) uc.getDouble("home.pitch", 0);

        p.teleport(new Location(w, x, y, z, yaw, pitch));
        p.sendMessage(successMsg);
    }
}