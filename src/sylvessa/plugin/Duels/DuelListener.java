package sylvessa.plugin.Duels;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
        if(g != null && !g.canBreak(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        DuelGame g = DuelManager.get(e.getPlayer());
        if(g != null && !g.canPlace(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        Player p = (Player)e.getEntity();
        DuelGame g = DuelManager.get(p);
        if(g != null) g.onDamage(p);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        DuelGame g = DuelManager.get(e.getPlayer());
        if(g != null) g.onQuit(e.getPlayer());
    }
}
