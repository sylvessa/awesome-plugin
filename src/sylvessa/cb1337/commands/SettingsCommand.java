package sylvessa.cb1337.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.cb1337.Main;
import sylvessa.cb1337.SettingsRegistry;
import sylvessa.cb1337.Types.*;
import sylvessa.cb1337.UserConfig;

import java.util.List;

@SuppressWarnings("unused")
public class SettingsCommand implements PluginCommand {
    public String name() { return "settings"; }
    public String description() { return "View all settings"; }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return;
        }

        Player p = (Player) sender;
        UserConfig uc = Main.getInstance().getUserConfig(p.getName());
        if (uc == null) {
            p.sendMessage("§cYour user config is not loaded.");
            return;
        }

        List<Setting> settings = SettingsRegistry.getAll();

        int page = 1;
        if (args.length > 0) {
            try { page = Integer.parseInt(args[0]); } catch(Exception ignored) {}
        }

        int perPage = 8;
        int totalPages = (int) Math.ceil(settings.size() / (double) perPage);
        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        int start = (page - 1) * perPage;
        int end = Math.min(start + perPage, settings.size());

        p.sendMessage("§e--- Settings §7(Page " + page + "/" + totalPages + ") ---");
        for (int i = start; i < end; i++) {
            Setting s = settings.get(i);
            String value;
            switch (s.type) {
                case "boolean": value = String.valueOf(uc.getBoolean(s.key, false)); break;
                case "int": value = String.valueOf(uc.getInt(s.key, 0)); break;
                case "double": value = String.valueOf(uc.getDouble(s.key, 0)); break;
                case "string": value = uc.getString(s.key, ""); break;
                default: value = "unknown";
            }
            p.sendMessage("§6" + s.alias + " §f- " + s.description + " §7[§e" + value + "§7]");
        }

        if (totalPages > 1) {
            p.sendMessage("§7Type §e/settings <page> §7to view other pages.");
        }

        p.sendMessage("§7To change a setting: §e/setting <name> <value>");
    }
}