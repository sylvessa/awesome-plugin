package sylvessa.spigot.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import sylvessa.spigot.Types.PluginCommand;
import org.bukkit.command.Command;

public class PingCommand implements PluginCommand {

    public String name() {
        return "ping";
    }

    public String description() {
        return "Shows your ping.";
    }

    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return;

        Player p = (Player) sender;
        int ping = ((CraftPlayer)p).getHandle().ping;

        p.sendMessage("§aPing: §f" + ping + "ms");
    }
}
