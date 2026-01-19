package sylvessa.cb1337.Duels.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.*;
import sylvessa.cb1337.Duels.DuelGame;
import sylvessa.cb1337.Duels.DuelManager;
import sylvessa.cb1337.Log;

public class DuelEntityListener extends EntityListener {
    public void onEntityDamage(EntityDamageEvent e) {
        if(!(e.getEntity() instanceof Player)) return;

        Player p = (Player)e.getEntity();
        DuelGame g = DuelManager.get(p);
        if(g != null) g.onDamage(p, e);
    }

    @Override
    public void onEntityDeath(EntityDeathEvent e) {
        if(!(e.getEntity() instanceof Player)) return;

        Player p = (Player)e.getEntity();
        DuelGame g = DuelManager.get(p);
        if(g != null) g.onDeath(p, e);
    }

    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        Player p = (Player)event.getEntity();
        DuelGame g = DuelManager.get(p);
        if(g != null) g.onFoodLevelChange(p, event);
    }
}
