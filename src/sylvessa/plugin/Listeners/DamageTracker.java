package sylvessa.plugin.Listeners;

import net.minecraft.server.EntityTypes;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import sylvessa.plugin.Log;

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
            Log.info("Damaged by " + ((Player) damager).getName());
            lastAttacker.put(key, ((Player) damager).getName());
        } else if (damager instanceof LivingEntity) {
            Log.info("Damaged by " + damager.toString());
            lastAttacker.put(key, formatMobName(damager));

        }
    }


    private String formatMobName(Entity e) {
        return e.toString().replace("Craft", "");
    }
}
