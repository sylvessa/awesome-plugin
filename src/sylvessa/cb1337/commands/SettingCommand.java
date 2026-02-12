package sylvessa.cb1337.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.cb1337.Main;
import sylvessa.cb1337.SettingsRegistry;
import sylvessa.cb1337.Types.*;
import sylvessa.cb1337.UserConfig;
import org.bukkit.command.Command;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class SettingCommand implements PluginCommand {
    public String name() { return "setting"; }
    public String description() { return "Change a specific setting"; }

    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return;
        }

        if (args.length < 2) {
            sender.sendMessage("§7Usage: §e/setting <alias> <value>");
            return;
        }

        Player p = (Player) sender;
        UserConfig uc = Main.getInstance().getUserConfig(p.getName());
        if (uc == null) {
            sender.sendMessage("§cYour user config is not loaded.");
            return;
        }

        String alias = args[0].toLowerCase();
        String valueInput = args[1];

        Setting target = SettingsRegistry.getByAlias(alias);
        if (target == null) {
            p.sendMessage("§cUnknown setting: " + alias + ". View all settings with /settings");
            return;
        }

        try {
            switch (target.type) {
                case "boolean":
                    if (valueInput.equalsIgnoreCase("true") || valueInput.equals("1")) {
                        uc.set(target.key, true);
                    } else if (valueInput.equalsIgnoreCase("false") || valueInput.equals("0")) {
                        uc.set(target.key, false);
                    } else {
                        p.sendMessage("§cInvalid value. Use §etrue§c, §efalse§c, §e1§c, or §e0§c.");
                        return;
                    }
                    break;
                case "int":
                    try {
                        int val = Integer.parseInt(valueInput);
                        uc.set(target.key, val);
                    } catch (NumberFormatException e) {
                        p.sendMessage("§cInvalid integer value for " + target.alias + ": " + valueInput);
                        return;
                    }
                    break;
                case "double":
                    try {
                        double val = Double.parseDouble(valueInput);
                        uc.set(target.key, val);
                    } catch (NumberFormatException e) {
                        p.sendMessage("§cInvalid decimal value for " + target.alias + ": " + valueInput);
                        return;
                    }
                    break;
                case "string":
                    uc.set(target.key, valueInput);
                    break;
                default:
                    p.sendMessage("§cCannot set this type of setting.");
                    return;
            }


            uc.save();
            p.sendMessage("§aSet §e" + target.alias + " §ato §e" + valueInput + "§a.");
        } catch (Exception e) {
            p.sendMessage("§cError setting " + target.alias + ": " + e.getMessage());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (!(sender instanceof Player)) return suggestions;

        if (args.length == 1) {
            String prefix = args[0].toLowerCase();
            for (Setting s : SettingsRegistry.getAll()) {
                if (s.alias.toLowerCase().startsWith(prefix)) suggestions.add(s.alias);
            }
        } else if (args.length == 2) {
            String alias = args[0].toLowerCase();
            Setting target = SettingsRegistry.getByAlias(alias);
            if (target != null && target.type.equals("boolean")) {
                String prefix = args[1].toLowerCase();
                if ("true".startsWith(prefix)) suggestions.add("true");
                if ("false".startsWith(prefix)) suggestions.add("false");
            }
        }
        return suggestions;
    }
}
