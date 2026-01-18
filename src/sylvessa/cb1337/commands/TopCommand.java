package sylvessa.cb1337.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.cb1337.Types.PluginCommand;

public class TopCommand implements PluginCommand {
    public String name() { return "top"; }
    public String description() { return "Teleports you to the surface"; }

    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) return;

        Player p = (Player)sender;
        World w = p.getWorld();

        int x = p.getLocation().getBlockX();
        int z = p.getLocation().getBlockZ();
        int y = w.getHighestBlockYAt(x, z);

        p.teleport(new Location(w, x, y + 1, z));

    }
}
