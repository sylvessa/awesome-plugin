package sylvessa.spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.spigot.Types.PluginCommand;
import sylvessa.spigot.Main;
import sylvessa.spigot.UserConfig;

import static sylvessa.spigot.Util.Helpers.buildDisplayName;

@SuppressWarnings("unused")
public class ChatColorCommand implements PluginCommand {
    public String name() {
        return "chatcolor";
    }

    public String description() {
        return "Change your chat color";
    }

    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return;
        }

        if (args.length != 1) {
            sender.sendMessage("Usage: /chatcolor <0-9, a-f>");
            return;
        }

        String code = args[0].toLowerCase();

        if (!code.matches("[0-9a-f]")) {
            sender.sendMessage("Invalid color! Only 0-9, a-f are allowed.");
            return;
        }

        Player player = (Player) sender;
        Main plugin = Main.getInstance();

        UserConfig uc = plugin.getUserConfig(player.getName());
        if (uc == null) {
            sender.sendMessage("Error: player config not loaded.");
            return;
        }

        uc.set("color", code);
        uc.save();


        String displayName2 = buildDisplayName(player.getName(), false);
        if(displayName2.length() > 16) displayName2 = displayName2.substring(0, 16);

        if(!player.getPlayerListName().equals(displayName2))
            player.setPlayerListName(displayName2);

        sender.sendMessage("Your chat color has been set to §" + code + "this color§f!");
    }
}
