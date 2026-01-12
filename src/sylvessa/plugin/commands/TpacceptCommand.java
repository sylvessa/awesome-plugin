package sylvessa.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.plugin.Main;
import sylvessa.plugin.Spleef.SpleefGame;
import sylvessa.plugin.Spleef.SpleefManager;
import sylvessa.plugin.TpaRequest;

@SuppressWarnings("unused")
public class TpacceptCommand implements PluginCommand {
    public String name() { return "tpaccept"; }
    public String description() { return "Accept a teleport request"; }

    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) return;

        Player target = (Player) sender;

        SpleefGame fromGame = SpleefManager.get(target);
        if (fromGame != null) {
            target.sendMessage("§cYou cannot use this command while in a Spleef game!");
            return;
        }

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