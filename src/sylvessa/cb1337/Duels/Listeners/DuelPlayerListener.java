package sylvessa.cb1337.Duels.Listeners;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import sylvessa.cb1337.Duels.DuelGame;
import sylvessa.cb1337.Duels.DuelManager;
import sylvessa.cb1337.Main;



public class DuelPlayerListener extends PlayerListener {

    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        DuelGame g = DuelManager.get(p);
        if(g != null) g.onMove(p);
    }

    public void onPlayerQuit(PlayerQuitEvent e) {
        DuelGame g = DuelManager.get(e.getPlayer());
        if(g != null) g.onQuit(e.getPlayer());
    }

    public void onPlayerInteract(PlayerInteractEvent e) {
        if(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player p = e.getPlayer();
        DuelGame g = DuelManager.get(p);
        if(g == null) return;

//        ItemStack item = p.getItemInHand();
//        if(item == null || item.getType() != Material.BOW) return;
//
//        Main.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
//            for(Entity entity : p.getWorld().getEntities()) {
//                if(entity instanceof Arrow) {
//                    Arrow a = (Arrow) entity;
//                    if(a.getShooter() == p) g.onBowShoot(p);
//                }
//            }
//        }, 1L);
    }
}
