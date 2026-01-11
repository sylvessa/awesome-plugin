package sylvessa.plugin.Listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import sylvessa.plugin.Main;
import sylvessa.plugin.Types.Team;

import java.util.HashMap;

public class DamageTracker implements Listener {
    private static final HashMap<String, EntityDamageEvent.DamageCause> lastCause = new HashMap<>();
    private static final HashMap<String, String> lastAttacker = new HashMap<>();

    public static EntityDamageEvent.DamageCause getLastCause(Player p) {
        return lastCause.get(p.getName().toLowerCase());
    }

    public static String getLastAttacker(Player p) {
        return lastAttacker.get(p.getName().toLowerCase());
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player victim = (Player) event.getEntity();
        String key = victim.getName().toLowerCase();

        lastCause.put(key, event.getCause());
        lastAttacker.remove(key);

        if (!(event instanceof EntityDamageByEntityEvent)) return;

        EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
        Entity damager = e.getDamager();

        if (damager instanceof Player) {
            lastAttacker.put(key, ((Player) damager).getName());
            Player attacker = (Player) e.getDamager();

            if (victim.getName().equals(attacker.getName())) return;

            Team vt = Main.getInstance().getTeamManager().getPlayerTeam(victim.getName());
            Team at = Main.getInstance().getTeamManager().getPlayerTeam(attacker.getName());

            if (vt == null || at == null) return;
            if (!vt.getName().equalsIgnoreCase(at.getName())) return;
            if (vt.isPvpEnabled()) return;

            e.setCancelled(true);
        } else if (damager instanceof LivingEntity) {
            lastAttacker.put(key, formatMobName(damager));
        }
    }


    private String formatMobName(Entity e) {
        return e.toString().replace("Craft", "");
    }
}
