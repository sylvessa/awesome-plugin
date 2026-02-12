package sylvessa.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.spigot.Duels.DuelGame;
import sylvessa.spigot.Duels.DuelManager;
import sylvessa.spigot.Main;
import sylvessa.spigot.Minigames.MinigameManager;
import sylvessa.spigot.Types.*;
import sylvessa.spigot.UserConfig;
import org.bukkit.command.Command;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class TpaHereCommand implements PluginCommand {
    public String name() { return "tpahere"; }
    public String description() { return "Request player to teleport to you"; }

    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
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

        if (MinigameManager.get(from) != null || MinigameManager.getQueued(from) != null) {
            from.sendMessage("§cYou cannot use this command while in a minigame!");
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

        if (MinigameManager.get(to) != null || MinigameManager.getQueued(to) != null) {
            from.sendMessage("§cThat player is in a minigame and cannot receive TPA requests.");
            return;
        }

        World fw = from.getWorld();
        World tw = to.getWorld();

        boolean sameWorld = fw.equals(tw);
        boolean bothAllowed = (
                (fw.getName().equals("world") || fw.getName().equals("world_nether") || fw.getName().equals("world_the_end")) &&
                        (tw.getName().equals("world") || tw.getName().equals("world_nether") || tw.getName().equals("world_the_end"))
        );

        if(!sameWorld && !bothAllowed) {
            from.sendMessage("§cYou cannot send TPA requests across these worlds.");
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

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (!(sender instanceof Player)) return suggestions;
        if (args.length == 1) {
            String prefix = args[0].toLowerCase();
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(prefix)) suggestions.add(p.getName());
            }
        }
        return suggestions;
    }
}