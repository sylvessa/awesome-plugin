package sylvessa.plugin.Minigames;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MinigameListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Minigame g = MinigameManager.get(e.getPlayer());
        if(g != null) g.onMove(e.getPlayer());
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Minigame g = MinigameManager.get(e.getPlayer());
        if(g != null && !g.canBreak(e.getPlayer(), e)) e.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Minigame g = MinigameManager.get(e.getPlayer());
        if(g != null && !g.canPlace(e.getPlayer(), e)) e.setCancelled(true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        Minigame g = MinigameManager.get(p);
        if(g != null) {
            g.onQuit(p);
        }

        MinigameManager.remove(p);
    }

}
