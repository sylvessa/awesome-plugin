package sylvessa.spigot.Listeners;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import sylvessa.spigot.Main;
import sylvessa.spigot.UserConfig;

public class FlightSoupListener implements Listener {

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        UserConfig config = Main.getInstance().getUserConfig(player.getName());

        long remaining = config.getLong("flight.remaining", 0L);
        World w = player.getWorld();

        if (remaining > 0) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> player.setAllowFlight(
                    w.getName().equalsIgnoreCase("world") ||
                            w.getName().equalsIgnoreCase("world_nether") ||
                            w.getName().equalsIgnoreCase("world_the_end") ||
                            player.getGameMode().equals(GameMode.CREATIVE)
            ), 8L);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UserConfig config = Main.getInstance().getUserConfig(player.getName());

        long lastCheck = System.currentTimeMillis();
        long remaining = config.getLong("flight.remaining", 0L);

        if (remaining <= 0) {
            player.setAllowFlight(false);
            config.set("flight.remaining", 0L);
            config.save();
            return;
        }

        long delta = 50L; // approx time per tick in ms
        remaining -= delta;
        config.set("flight.remaining", Math.max(remaining, 0L));
        config.save();

        if (remaining <= 0) player.setAllowFlight(false);
    }

    @EventHandler
    public void onRightClickUse(PlayerInteractEvent e) {
        if (e.getItem() == null) return;

        Player player = e.getPlayer();
        Action action = e.getAction();
        ItemStack item = e.getItem();
        int slot = player.getInventory().getHeldItemSlot();

        if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) &&
                item.getType() == Material.MUSHROOM_SOUP &&
                item.getItemMeta().getLore() != null &&
                !item.getItemMeta().getLore().isEmpty() &&
                item.getItemMeta().getLore().get(0).equals(ChatColor.RESET + "" + ChatColor.DARK_PURPLE + "Makes you fly for 2 hours")) {

            e.setCancelled(true);
            player.sendMessage(ChatColor.YELLOW + "You can now fly!");
            player.setAllowFlight(true);

            UserConfig config = Main.getInstance().getUserConfig(player.getName());
            long remaining = config.getLong("flight.remaining", 0L);

            remaining += 2 * 60L * 60L * 1000L;
            config.set("flight.remaining", remaining);
            config.save();

            long minutes = remaining / 60000;
            long seconds = (remaining % 60000) / 1000;
            player.sendMessage(ChatColor.GRAY + "(Flight remaining: " + minutes + "m " + seconds + "s)");

            if (item.getAmount() <= 1)
                player.getInventory().setItem(slot, null);
            else {
                item.setAmount(item.getAmount() - 1);
                player.getInventory().setItem(slot, item);
            }
        }
    }
}
