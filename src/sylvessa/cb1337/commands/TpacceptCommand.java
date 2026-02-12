package sylvessa.cb1337.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.cb1337.Duels.DuelGame;
import sylvessa.cb1337.Duels.DuelManager;
import sylvessa.cb1337.Main;
import sylvessa.cb1337.Minigames.MinigameManager;
import sylvessa.cb1337.Types.*;
import org.bukkit.command.Command;

@SuppressWarnings("unused")
public class TpacceptCommand implements PluginCommand {
    public String name() { return "tpaccept"; }
    public String description() { return "Accept a teleport request"; }

    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) return;

        Player target = (Player) sender;

        DuelGame fromGame = DuelManager.get(target);
        if (fromGame != null) {
            target.sendMessage("§cYou cannot use this command while in a duel!");
            return;
        }

        if (MinigameManager.get(target) != null || MinigameManager.getQueued(target) != null) {
            target.sendMessage("§cYou cannot use this command while in a minigame!");
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