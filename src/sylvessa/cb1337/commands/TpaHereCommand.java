package sylvessa.cb1337.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.cb1337.Duels.DuelGame;
import sylvessa.cb1337.Duels.DuelManager;
import sylvessa.cb1337.Main;
import sylvessa.cb1337.Types.*;
import sylvessa.cb1337.UserConfig;

@SuppressWarnings("unused")
public class TpaHereCommand implements PluginCommand {
    public String name() { return "tpahere"; }
    public String description() { return "Request player to teleport to you"; }

    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) return;
        if(args.length != 1) {
            sender.sendMessage("Usage: /tpahere <player>");
            return;
        }

        Player from = (Player) sender;

        DuelGame fromGame = DuelManager.get(from);
        if (fromGame != null) {
            from.sendMessage("§cYou cannot use this command while in a duel!");
            return;
        }

        Player to = Bukkit.getPlayer(args[0]);
        if(to == null || from == to) {
            sender.sendMessage("§cPlayer not found.");
            return;
        }

        UserConfig uc = Main.getInstance().getUserConfig(to.getName());
        if(!uc.getBoolean("tpa.enabled", true)) {
            sender.sendMessage("§cThat player has tpa disabled.");
            return;
        }

        DuelGame toGame = DuelManager.get(to);
        if (toGame != null) {
            from.sendMessage("§cThat player is in a duel and cannot receive TPA requests.");
            return;
        }

        Main.getInstance().getTpaRequests()
                .put(to.getName().toLowerCase(), new TpaRequest(from.getName(), true));

        UserConfig fromUc = Main.getInstance().getUserConfig(from.getName());
        String color = "f";
        if(fromUc != null) {
            color = fromUc.getString("color", "f");
        }

        String displayName = "§" + color + from.getName() + "§f";

        from.sendMessage("§aTeleport request sent.");
        to.sendMessage("§e" + displayName + " §fwants you to teleport to them.");
        to.sendMessage("§7Type §a/tpaccept §7or §c/tpdeny§7.");

    }
}