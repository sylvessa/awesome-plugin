package sylvessa.cb1337.Listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import sylvessa.cb1337.Duels.DuelGame;
import sylvessa.cb1337.Duels.DuelManager;
import sylvessa.cb1337.Log;
import sylvessa.cb1337.Main;
import sylvessa.cb1337.Types.Team;

import java.util.HashMap;

public class DamageListener extends EntityListener {
    private static final HashMap<String, EntityDamageEvent.DamageCause> lastCause = new HashMap<>();
    private static final HashMap<String, String> lastAttacker = new HashMap<>();

    public static EntityDamageEvent.DamageCause getLastCause(Player p) {
        return lastCause.get(p.getName().toLowerCase());
    }

    public static String getLastAttacker(Player p) {
        return lastAttacker.get(p.getName().toLowerCase());
    }

    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player victim = (Player) event.getEntity();

        String key = victim.getName().toLowerCase();

        lastCause.put(key, event.getCause());
        lastAttacker.remove(key);

//        double finalHealth = victim.getHealth() - event.getDamage();
//
//        if (finalHealth <= 0) {
//            event.setCancelled(true);
//
//            victim.setHealth(20);
//            victim.setFireTicks(0);
//
//            World spawnWorld = Bukkit.getWorlds().get(0);
//            Location spawnLoc = spawnWorld.getSpawnLocation();
//            victim.teleport(spawnLoc);
//        }

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

            DuelGame fromGame = DuelManager.get(victim);
            if (fromGame != null) return;

            e.setCancelled(true);
        } else if (damager instanceof LivingEntity) {
            lastAttacker.put(key, formatMobName(damager));
        }
    }


    private String formatMobName(Entity e) {
        return e.toString().replace("Craft", "");
    }
}
