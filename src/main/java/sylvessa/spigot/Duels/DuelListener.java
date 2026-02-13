package sylvessa.spigot.Duels;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DuelListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        DuelGame g = DuelManager.get(e.getPlayer());
        if (g != null && !g.canBreak(e.getPlayer(), e)) e.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        DuelGame g = DuelManager.get(e.getPlayer());
        if (g != null && !g.canPlace(e.getPlayer(), e)) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        DuelGame g = DuelManager.get(p);
        if (g != null) g.onDamage(p, e);
    }

    @EventHandler
    public void onEntityDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        DuelGame g = DuelManager.get(p);
        if (g != null) g.onDeath(p, e);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        DuelGame g = DuelManager.get(p);
        if (g != null) g.onFoodLevelChange(p, e);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        DuelGame g = DuelManager.get(e.getPlayer());
        if (g != null) g.onMove(e.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        DuelGame g = DuelManager.get(e.getPlayer());
        if (g != null) g.onQuit(e.getPlayer());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        DuelGame g = DuelManager.get(e.getPlayer());
        if (g == null) return;
    }

    @EventHandler
    public void onPlayerShootBow(EntityShootBowEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        DuelGame g = DuelManager.get(p);
        if (g != null) g.onBowShoot(p, e);
    }

    @EventHandler
    public void onPlayerPickupArrow(PlayerPickupItemEvent e) {
        DuelGame g = DuelManager.get(e.getPlayer());
        if (g != null) g.onPlayerPickupArrow(e.getPlayer(), e);
    }
}
