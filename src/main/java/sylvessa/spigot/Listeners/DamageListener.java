package sylvessa.spigot.Listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import sylvessa.spigot.Duels.DuelGame;
import sylvessa.spigot.Duels.DuelManager;
import sylvessa.spigot.Main;
import sylvessa.spigot.Types.Team;

public class DamageListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event instanceof EntityDamageByEntityEvent)) return;

        EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;

        Entity damager = e.getDamager();
        if (!(damager instanceof Player)) return;

        Player victim = (Player) e.getEntity();
        Player attacker = (Player) damager;

        if (victim.getName().equals(attacker.getName())) return;

        Team vt = Main.getInstance().getTeamManager().getPlayerTeam(victim.getName());
        Team at = Main.getInstance().getTeamManager().getPlayerTeam(attacker.getName());

        if (vt == null || at == null) return;
        if (!vt.getName().equalsIgnoreCase(at.getName())) return;
        if (vt.isPvpEnabled()) return;

        DuelGame fromGame = DuelManager.get(victim);
        if (fromGame != null) return;

        e.setCancelled(true);
    }
}
