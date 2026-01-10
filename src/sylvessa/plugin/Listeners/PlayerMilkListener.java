package sylvessa.plugin.Listeners;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerMilkListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Player)) return;

        Player p = event.getPlayer();
        ItemStack hand = p.getItemInHand();
        if (hand == null) return;

        if (hand.getType() != Material.BUCKET) return;



        //p.getInventory().addItem(new ItemStack(335, 1));
        p.sendMessage("owo");
    }
}
