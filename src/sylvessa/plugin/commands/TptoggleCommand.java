package sylvessa.plugin.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.plugin.Main;
import sylvessa.plugin.UserConfig;

@SuppressWarnings("unused")
public class TptoggleCommand implements PluginCommand {
    public String name() { return "tptoggle"; }
    public String description() { return "Toggle teleport requests"; }

    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) return;

        Player p = (Player) sender;
        UserConfig uc = Main.getInstance().getUserConfig(p.getName());

        boolean enabled = uc.getBoolean("tpa.enabled", true);
        uc.set("tpa.enabled", !enabled);
        uc.save();

        p.sendMessage("§eTPA is now " + (!enabled ? "§aenabled" : "§cdisabled") + "§e.");
    }
}
