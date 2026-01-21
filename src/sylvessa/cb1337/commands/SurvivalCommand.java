package sylvessa.cb1337.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.cb1337.Types.PluginCommand;
import sylvessa.cb1337.Util.SurvivalHelper;

public class SurvivalCommand implements PluginCommand {

    public String name() {
        return "survival";
    }

    public String description() {
        return "Send player to survival world";
    }

    public boolean hidden() {
        return false;
    }

    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) return;

        Player p = (Player) sender;

        if(SurvivalHelper.isSurvivalWorld(p.getWorld())) {
            return;
        }

        World target = Bukkit.getWorld("world");
        if(target == null) return;

        SurvivalHelper.ignoreNextWorldChange(p);
        p.teleport(target.getSpawnLocation());
        SurvivalHelper.enterSurvival(p);
    }

}
