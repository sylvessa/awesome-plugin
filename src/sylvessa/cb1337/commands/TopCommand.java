package sylvessa.cb1337.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.cb1337.Duels.DuelManager;
import sylvessa.cb1337.Minigames.MinigameManager;
import sylvessa.cb1337.Types.PluginCommand;
import org.bukkit.command.Command;

public class TopCommand implements PluginCommand {
    public String name() { return "top"; }
    public String description() { return "Teleports you to the surface"; }

    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) return;

        Player p = (Player)sender;

        if (MinigameManager.get(p) != null || MinigameManager.getQueued(p) != null || DuelManager.get(p) != null) {
            p.sendMessage("§cYou cannot use this command right now!");
            return;
        }

        World w = p.getWorld();

        int x = p.getLocation().getBlockX();
        int z = p.getLocation().getBlockZ();
        int y = w.getHighestBlockYAt(x, z);

        p.teleport(new Location(w, x, y + 1, z));

    }
}
