package sylvessa.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sylvessa.spigot.Types.PluginCommand;
import org.bukkit.command.Command;

@SuppressWarnings("unused")
public class GiveCommand implements PluginCommand {
    public String name() {
        return "give2";
    }

    public String description() {
        return "Give items with id and data";
    }

    public boolean hidden() {
        return true;
    }

    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /give2 <player> <id> [amount] [data]");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found.");
            return;
        }

        int id;
        int amount = 1;
        short data = 0;

        try {
            id = Integer.parseInt(args[1]);
            if (args.length >= 3) amount = Integer.parseInt(args[2]);
            if (args.length >= 4) data = Short.parseShort(args[3]);
        } catch (Exception e) {
            sender.sendMessage("§cInvalid number.");
            return;
        }

        //if (amount < -127) amount = -127;
        if (amount > 127) amount = 127;

        ItemStack item = new ItemStack(id, amount, data);
        target.getInventory().addItem(item);

//        sender.sendMessage("§aGave §e" + amount + " §aof §e" + id + ":" + data + " §ato §e" + target.getName());
//        if (!sender.equals(target)) {
//            target.sendMessage("§aYou received §e" + amount + " §aof §e" + id + ":" + data);
//        }
    }
}

