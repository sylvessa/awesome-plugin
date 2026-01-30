package sylvessa.cb1337.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.cb1337.Types.PluginCommand;
import org.bukkit.craftbukkit.entity.CraftPlayer;

public class PingCommand implements PluginCommand {

    public String name() {
        return "ping";
    }

    public String description() {
        return "Shows your ping.";
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;

        Player p = (Player) sender;
        int ping = ((CraftPlayer)p).getHandle().ping;

        p.sendMessage("§aPing: §f" + ping + "ms");
    }
}
