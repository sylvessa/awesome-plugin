package sylvessa.plugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import org.bukkit.event.entity.EntityDeathEvent;
import sylvessa.plugin.Log;
import sylvessa.plugin.Main;
import sylvessa.plugin.UserConfig;

public class DeathListener implements Listener {
    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        Entity e = event.getEntity();
        if (!(e instanceof Player)) return;

        Player p = (Player) e;

        UserConfig uc = Main.getInstance().getUserConfig(p.getName());
        String color = uc != null ? uc.getString("color", "f") : "f";
        String pname = "§" + color + p.getName() + "§f";

        EntityDamageEvent.DamageCause cause = DamageTracker.getLastCause(p);
        String attacker = DamageTracker.getLastAttacker(p);

        String msg = pname + " died";

        if (cause != null) {
            if (cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK && attacker != null) {

                UserConfig kuc = Main.getInstance().getUserConfig(attacker);
                if (kuc != null) {
                    String kcolor = kuc.getString("color", "f");
                    msg = pname + " was slain by §" + kcolor + attacker;
                } else {
                    msg = pname + " was slain by " + attacker;
                }

            } else if (cause == EntityDamageEvent.DamageCause.FALL) {
                msg = pname + " fell from a high place";
            } else if (cause == EntityDamageEvent.DamageCause.LAVA) {
                msg = pname + " tried to swim in lava";
            } else if (cause == EntityDamageEvent.DamageCause.FIRE || cause == EntityDamageEvent.DamageCause.FIRE_TICK) {
                msg = pname + " went up in flames";
            } else if (cause == EntityDamageEvent.DamageCause.DROWNING) {
                msg = pname + " drowned";
            } else if (cause == EntityDamageEvent.DamageCause.VOID) {
                msg = pname + " fell out of the world";
            } else if (cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION
                    || cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
                msg = pname + " blew up";
            }
        }

        Bukkit.broadcastMessage(msg);
    }
}
