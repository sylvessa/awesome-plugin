package sylvessa.cb1337.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sylvessa.cb1337.Types.PluginCommand;

@SuppressWarnings("unused")
public class HatCommand implements PluginCommand {
    public String name() {
        return "hat";
    }

    public String description() {
        return "Makes the current item you're holding your helmet.";
    }

    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return;
        }

        Player player = (Player) sender;
        ItemStack hand = player.getItemInHand();

        if (hand == null || hand.getTypeId() == 0) {
            player.sendMessage("§cYou must be holding an item.");
            return;
        }

        ItemStack helmet = player.getInventory().getHelmet();

        player.getInventory().setHelmet(hand);

        if(helmet != null && helmet.getTypeId() != 0) {
            player.setItemInHand(helmet);
        } else {
            player.setItemInHand(null);
        }
    }
}