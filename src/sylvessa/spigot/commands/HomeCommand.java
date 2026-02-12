package sylvessa.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.spigot.Duels.DuelManager;
import sylvessa.spigot.Main;
import sylvessa.spigot.Minigames.MinigameManager;
import sylvessa.spigot.Types.PluginCommand;
import sylvessa.spigot.UserConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeCommand implements PluginCommand {
    public String name() { return "home"; }

    public String description() { return "Teleport to your home or another player's public home"; }

    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return;
        }

        Player p = (Player) sender;

        if (DuelManager.get(p) != null) {
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

        Main plugin = Main.getInstance();

        // load configs if needed
        File usersFolder = new File(plugin.getDataFolder(), "users");
        if (usersFolder.exists() && usersFolder.isDirectory()) {
            for (File f : usersFolder.listFiles()) {
                if (!f.getName().endsWith(".yml")) continue;
                String name = f.getName().replace(".yml", "").toLowerCase();
                if (!plugin.getUserConfigs().containsKey(name)) {
                    UserConfig uc = new UserConfig(name, plugin);
                    plugin.getUserConfigs().put(name, uc);
                }
            }
        }

        if (args.length == 0) {
            UserConfig uc = plugin.getUserConfig(p.getName());
            if (uc == null) { p.sendMessage("§cConfig not loaded."); return; }
            teleportToHome(p, uc, "§aTeleported home.");
            return;
        }

        String input = args[0].toLowerCase();
        List<String> matches = new ArrayList<>();
        for (Map.Entry<String, UserConfig> entry : plugin.getUserConfigs().entrySet()) {
            String username = entry.getKey();
            UserConfig uc = entry.getValue();
            if (username.equalsIgnoreCase(p.getName())) continue;
            if (!uc.getBoolean("home.public", false)) continue;
            if (username.startsWith(input)) matches.add(username);
        }

        if (matches.isEmpty()) {
            p.sendMessage("§cNo matching player found.");
            return;
        }
        if (matches.size() > 1) {
            p.sendMessage("§cThat name matches multiple players.");
            return;
        }

        UserConfig targetUc = plugin.getUserConfig(matches.get(0));
        if (targetUc == null) { p.sendMessage("§cPlayer config not loaded."); return; }

        teleportToHome(p, targetUc, "§aTeleported to §" + targetUc.getString("color", "f") + matches.get(0) + "§a's home.");
    }

    private void teleportToHome(Player p, UserConfig uc, String msg) {
        String worldName = uc.getString("home.world", null);
        if (worldName == null) { p.sendMessage("§eHome not set."); return; }
        World w = Bukkit.getWorld(worldName);
        if (w == null) { p.sendMessage("§cHome world no longer exists."); return; }

        double x = uc.getDouble("home.x", 0);
        double y = uc.getDouble("home.y", 0);
        double z = uc.getDouble("home.z", 0);
        float yaw = (float) uc.getDouble("home.yaw", 0);
        float pitch = (float) uc.getDouble("home.pitch", 0);

        p.teleport(new Location(w, x, y, z, yaw, pitch));
        p.sendMessage(msg);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1 && sender instanceof Player) {
            String prefix = args[0].toLowerCase();
            String selfName = ((Player)sender).getName().toLowerCase();

            Main plugin = Main.getInstance();
            for (Map.Entry<String, UserConfig> entry : plugin.getUserConfigs().entrySet()) {
                String username = entry.getKey();
                UserConfig uc = entry.getValue();
                if (username.equalsIgnoreCase(selfName)) continue;
                if (!uc.getBoolean("home.public", false)) continue;
                if (username.startsWith(prefix)) suggestions.add(username);
            }
        }
        return suggestions;
    }
}
