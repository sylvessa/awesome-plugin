package sylvessa.cb1337.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.cb1337.Types.PluginCommand;

import java.util.ArrayList;
import java.util.List;

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

    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return;

        Player p = (Player) sender;
        p.sendMessage("hi");

        // nbt demo
//        ItemStack inHand = p.getItemInHand();
//
//        if (inHand == null || inHand.getType() == Material.AIR) {
//            ItemStack stick = new ItemStack(Material.STICK, 1);
//            stick = ItemNBT.setInt(stick, "cooltag", 195);
//            stick = ItemNBT.setString(stick, "owner", p.getName());
//            p.setItemInHand(stick);
//            return;
//        }
//
//        Integer tag = ItemNBT.getInt(inHand, "cooltag");
//
//        if (tag != null) {
//            p.sendMessage("cooltag = " + tag);
//        } else {
//            p.sendMessage("no cooltag");
//        }
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
