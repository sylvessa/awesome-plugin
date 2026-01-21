package sylvessa.cb1337.Listeners.Lobby;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;

public class LobbyEntityListener extends EntityListener {
    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player victim = (Player) event.getEntity();
        if (victim.getWorld().getName().equals("lobby")) {
            event.setCancelled(true);
            return;
        }
    }
}
