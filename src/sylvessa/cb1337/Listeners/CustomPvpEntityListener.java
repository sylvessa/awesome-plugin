package sylvessa.cb1337.Listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.util.Vector;

public class CustomPvpEntityListener extends EntityListener {
    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event instanceof EntityDamageByEntityEvent)) return;
        EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;

        if(!(e.getDamager() instanceof Snowball || e.getDamager() instanceof Egg)) return;
        if(!(e.getEntity() instanceof Player)) return;

        Player victim = (Player)e.getEntity();
        if (victim.getGameMode().equals(GameMode.CREATIVE)) return;

        Object projectile = e.getDamager();
        if(!((projectile instanceof Snowball || projectile instanceof Egg) &&
                ((projectile instanceof Snowball && ((Snowball)projectile).getShooter() instanceof Player) ||
                        (projectile instanceof Egg && ((Egg)projectile).getShooter() instanceof Player)))) return;

        Player attacker;
        if(projectile instanceof Snowball) attacker = (Player)((Snowball)projectile).getShooter();
        else attacker = (Player)((Egg)projectile).getShooter();

        e.setCancelled(true);

        int damage = 1;
        double knockbackStrength = 0.5;
        double knockbackY = 0.25;

        Vector v = victim.getLocation().toVector()
                .subtract(attacker.getLocation().toVector())
                .normalize()
                .multiply(knockbackStrength);
        v.setY(knockbackY);

        victim.setVelocity(v);
        victim.damage(damage, attacker);
    }
}
