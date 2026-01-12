package sylvessa.plugin.Spleef;

import org.bukkit.event.Listener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import sylvessa.plugin.Main;

public class SpleefListener implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        SpleefGame g = SpleefManager.get(p);
        if(g == null) return;

        if(g.isFrozen(p)) {
            e.setTo(e.getFrom());
            return;
        }

        if(g.checkFall(p)) {
            g.markFall(p);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        SpleefGame g = SpleefManager.get(p);
        if(g == null) return;

        if(!g.hasStarted()) {
            e.setCancelled(true);
            return;
        }

        if(e.getBlock().getType() != Material.SNOW_BLOCK) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        Player p = (Player) e.getEntity();

        SpleefGame game = SpleefManager.get(p);
        if (game != null) {
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if(SpleefManager.get(e.getPlayer()) != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        Player p = (Player)e.getEntity();
        if(SpleefManager.get(p) != null) {
            //e.setDamage(0);
            Main.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                p.setHealth(20);
                p.setFireTicks(0);
            }, 1L);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        SpleefGame g = SpleefManager.get(p);
        if(g != null) {
            g.markFall(p);
        }
    }
}
