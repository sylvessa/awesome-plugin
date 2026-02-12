package sylvessa.cb1337.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.cb1337.Main;
import sylvessa.cb1337.Types.PluginCommand;

@SuppressWarnings("unused")
public class TpdenyCommand implements PluginCommand {
    public String name() { return "tpdeny"; }
    public String description() { return "Deny a teleport request"; }

    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) return;

        Player p = (Player) sender;
        if(Main.getInstance().getTpaRequests()
                .remove(p.getName().toLowerCase()) != null) {
            p.sendMessage("§cTeleport request denied.");
        } else {
            p.sendMessage("§7No pending teleport requests.");
        }
    }
}
