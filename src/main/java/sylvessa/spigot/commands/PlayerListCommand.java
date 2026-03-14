package sylvessa.spigot.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.spigot.Types.PluginCommand;
import sylvessa.spigot.Main;
import sylvessa.spigot.UserConfig;
import org.bukkit.command.Command;

@SuppressWarnings("unused")
public class PlayerListCommand implements PluginCommand {
    public String name() {
        return "playerlist";
    }

    public String description() {
        return "Shows the list of players online.";
    }

    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        Player[] players = Main.getInstance().getServer().getOnlinePlayers().toArray(new Player[0]);

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

