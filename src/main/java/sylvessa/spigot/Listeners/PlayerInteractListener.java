package sylvessa.spigot.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import sylvessa.spigot.Util.ItemNBT;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void onRightClickUse(PlayerInteractEvent e) {
        if (e.getItem() == null) return;

        Player player = e.getPlayer();
        Action action = e.getAction();
        ItemStack item = e.getItem();
        int slot = player.getInventory().getHeldItemSlot();

        if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
            if (item.getType() == Material.MUSHROOM_SOUP && ItemNBT.has(item, "isFlightSoup")) {
                e.setCancelled(true);

                player.sendMessage(ChatColor.YELLOW + "You can now fly!");
                player.setAllowFlight(true);

                if (item.getAmount() <= 1)
                    player.getInventory().setItem(slot, null);
                else {
                    item.setAmount(item.getAmount() - 1);
                    player.getInventory().setItem(slot, item);
                }
            }
        }
    }
}
