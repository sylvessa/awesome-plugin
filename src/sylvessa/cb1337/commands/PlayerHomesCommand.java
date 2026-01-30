package sylvessa.cb1337.commands;

import org.bukkit.command.CommandSender;
import sylvessa.cb1337.Main;
import sylvessa.cb1337.Types.PluginCommand;
import sylvessa.cb1337.UserConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class PlayerHomesCommand implements PluginCommand {
    public String name() {
        return "homes";
    }

    public String description() {
        return "View public player homes";
    }

    public void execute(CommandSender sender, String[] args) {
        int page = 1;
        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (Exception ignored) {}
        }

        Main plugin = Main.getInstance();

        // ensure all user configs are loaded from files
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

        List<String> entries = new ArrayList<>();

        for (Map.Entry<String, UserConfig> entry : plugin.getUserConfigs().entrySet()) {
            String username = entry.getKey();
            UserConfig uc = entry.getValue();

            if (!uc.getBoolean("home.public", false)) continue;

            String world = uc.getString("home.world", null);
            if (world == null) continue;

            double x = uc.getDouble("home.x", 0);
            double y = uc.getDouble("home.y", 0);
            double z = uc.getDouble("home.z", 0);

            String color = uc.getString("color", "f");
            String name = "§" + color + username + "§f";

            entries.add(name + " §7- §e" +
                    (int) x + "§7, §e" +
                    (int) y + "§7, §e" +
                    (int) z);
        }

        int perPage = 7;
        int totalPages = (int) Math.ceil(entries.size() / (double) perPage);
        if (totalPages == 0) totalPages = 1;

        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        int start = (page - 1) * perPage;
        int end = Math.min(start + perPage, entries.size());

        sender.sendMessage("§e--- Public Player Homes §7(Page " + page + "/" + totalPages + ") ---");

        if (entries.isEmpty()) {
            sender.sendMessage("§7No public homes available.");
            return;
        }

        for (int i = start; i < end; i++) {
            sender.sendMessage(entries.get(i));
        }

        if (totalPages > 1) {
            sender.sendMessage("§7Use §e/homes <page> §7to view more.");
        }
        sender.sendMessage("§7Teleport using §e/home <player>");
    }
}

