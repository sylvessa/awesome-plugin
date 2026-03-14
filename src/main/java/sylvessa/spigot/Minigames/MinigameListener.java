package sylvessa.spigot.Minigames;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;

public class MinigameListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        Minigame g = MinigameManager.get(p);
        if (MinigameManager.isQueued(p)) event.setCancelled(true);
        if (g != null && !g.canBreak(p, event)) event.setCancelled(true);

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player p = event.getPlayer();
        Minigame g = MinigameManager.get(p);
        if (MinigameManager.isQueued(p)) event.setCancelled(true);
        if (g != null && !g.canPlace(p, event)) event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player p = (Player) event.getEntity();
        if (MinigameManager.isQueued(p)) event.setCancelled(true);
        Minigame g = MinigameManager.get(p);
        if (g != null) g.onDamage(p, event);
    }

    @EventHandler
    public void onEntityDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        Minigame g = MinigameManager.get(p);
        if (g != null) g.onDeath(p, event);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player p = (Player) event.getEntity();
        if (MinigameManager.isQueued(p) || MinigameManager.get(p) != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        Minigame g = MinigameManager.get(p);
        Minigame q = MinigameManager.getQueued(p);
        if (g != null) g.onMove(p);
        if (q != null) q.onMoveInQueue(p);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        Minigame g = MinigameManager.get(p);
        if (g != null) g.onQuit(p);
        MinigameManager.remove(p);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Minigame g = MinigameManager.get(event.getPlayer());
        if (g != null) g.onChat(event.getPlayer(), event);
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        Minigame g = MinigameManager.get(event.getPlayer());
        if (g != null) g.onBucketEmpty(event.getPlayer(), event);
    }

    @EventHandler
    public void onPlayerPickupArrow(PlayerPickupItemEvent event) {
        Minigame g = MinigameManager.get(event.getPlayer());
        if (g != null) g.onPlayerPickupArrow(event.getPlayer(), event);
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player p = (Player) event.getEntity();
        Minigame g = MinigameManager.get(p);
        if (g != null) g.onBowShoot(p, event);
    }
}