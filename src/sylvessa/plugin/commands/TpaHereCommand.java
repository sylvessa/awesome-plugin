package sylvessa.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.plugin.Main;
import sylvessa.plugin.TpaRequest;
import sylvessa.plugin.UserConfig;

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
