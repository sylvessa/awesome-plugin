package sylvessa.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import sylvessa.spigot.Types.PluginCommand;
import sylvessa.spigot.Util.ItemNBT;

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

        Player player = (Player) sender;
        player.sendMessage("hi");

//        ItemStack item = new ItemStack(Material.STICK);
//        ItemMeta meta = item.getItemMeta();
//        meta.setDisplayName("whatever");
//        ArrayList<String> description = new ArrayList<String>();
//        description.add("YAS");
//        meta.setLore(description);
//        item.setItemMeta(meta);
//        player.getInventory().addItem(item);

        // nbt demo
//        ItemStack inHand = p.getItemInHand();

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
