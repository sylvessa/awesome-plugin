package sylvessa.cb1337.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.cb1337.Main;
import sylvessa.cb1337.Types.*;

@SuppressWarnings("unused")
public class TpacceptCommand implements PluginCommand {
    public String name() { return "tpaccept"; }
    public String description() { return "Accept a teleport request"; }

    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) return;

        Player target = (Player) sender;
        TpaRequest req = Main.getInstance().getTpaRequests()
                .remove(target.getName().toLowerCase());

        if(req == null) {
            sender.sendMessage("§7No pending teleport requests.");
            return;
        }

        Player from = Bukkit.getPlayer(req.from);
        if(from == null) {
            sender.sendMessage("§cRequesting player is offline.");
            return;
        }

        if(req.here) {
            target.teleport(from);
        } else {
            from.teleport(target);
        }

        sender.sendMessage("§aTeleport accepted.");
        from.sendMessage("§aTeleport accepted.");
    }
}