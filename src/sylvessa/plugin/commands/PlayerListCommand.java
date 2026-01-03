package sylvessa.plugin.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.plugin.Main;
import sylvessa.plugin.UserConfig;

public class PlayerListCommand implements PluginCommand {
    public String name() {
        return "playerlist";
    }

    public String description() {
        return "Shows the list of players online.";
    }

    public void execute(CommandSender sender, String[] args) {
        Player[] players = Main.getInstance().getServer().getOnlinePlayers();

        sender.sendMessage("§eOnline Players (§f" + players.length + "§e):");

        StringBuilder line = new StringBuilder();

        for (int i = 0; i < players.length; i++) {
            Player p = players[i];

            UserConfig uc = Main.getInstance().getUserConfig(p.getName());
            String color = "f";
            if (uc != null) color = uc.getString("color", "f");

            line.append("§").append(color).append(p.getName()).append("§f");

            if (i < players.length - 1) line.append(", ");
        }

        sender.sendMessage(line.toString());
    }
}
