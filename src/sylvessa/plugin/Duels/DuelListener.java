package sylvessa.plugin.Duels;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import sylvessa.plugin.Log;
import sylvessa.plugin.Main;

public class DuelListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        DuelGame g = DuelManager.get(p);
        if(g != null) g.onMove(p);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        DuelGame g = DuelManager.get(e.getPlayer());
        if(g != null && !g.canBreak(e.getPlayer(), e)) e.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        DuelGame g = DuelManager.get(e.getPlayer());
        if(g != null && !g.canPlace(e.getPlayer(), e)) e.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        Player p = (Player)e.getEntity();
        DuelGame g = DuelManager.get(p);
        if(g != null) g.onDamage(p, e);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        DuelGame g = DuelManager.get(e.getPlayer());
        if(g != null) g.onQuit(e.getPlayer());
    }

    @EventHandler
    public void onBowShoot(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player p = e.getPlayer();
        DuelGame g = DuelManager.get(p);
        if (g != null) {
            ItemStack item = p.getItemInHand();
            if (item != null && item.getType() == Material.BOW) {
                Main.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                    for (Entity entity : p.getWorld().getEntities()) {
                        if (entity instanceof Arrow) {
                            Arrow arrow = (Arrow) entity;
                            if (arrow.getShooter() == p) {
                                g.onBowShoot(p);
                            }
                        }
                    }
                }, 1L);
            }
        }
    }
}
