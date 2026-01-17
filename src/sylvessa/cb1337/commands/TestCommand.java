package sylvessa.cb1337.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sylvessa.cb1337.Main;
import sylvessa.cb1337.Types.FakePlayer;
import sylvessa.cb1337.Types.PluginCommand;

public class TestCommand implements PluginCommand {
    public String name() {
        return "testc";
    }

    public String description() {
        return "Makes the current item you're holding your helmet.";
    }

    public boolean hidden() {
        return true;
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;

        Player p = (Player) sender;

//        FakePlayer npc = new FakePlayer("gawg", p.getLocation());
//        npc.spawn();


        //Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), npc::lookAtNearestPlayer, 0L, 5L);
    }
}
