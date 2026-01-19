package sylvessa.cb1337.Minigames.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import sylvessa.cb1337.Minigames.Minigame;
import sylvessa.cb1337.Minigames.MinigameManager;

public class MinigameEntityListener extends EntityListener {
    public void onEntityDamage(EntityDamageEvent e) {
        if(!(e.getEntity() instanceof Player)) return;

        Player p = (Player)e.getEntity();

        if (MinigameManager.isQueued(p)) e.setCancelled(true); // no queue killing fags

        Minigame g = MinigameManager.get(p);
        if(g != null) g.onDamage(p, e);
    }

    public void onEntityDeath(EntityDeathEvent e) {
        if(!(e.getEntity() instanceof Player)) return;

        Player p = (Player)e.getEntity();

        Minigame g = MinigameManager.get(p);
        if(g != null) g.onDeath(p, e);
    }

    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        Player p = (Player) event.getEntity();
        if (MinigameManager.isQueued(p) || MinigameManager.get(p) != null) {
            event.setCancelled(true);
        }
    }
}
