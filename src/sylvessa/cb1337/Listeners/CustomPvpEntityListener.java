package sylvessa.cb1337.Listeners;

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

        if(!(e.getDamager() instanceof Snowball)) return;
        if(!(e.getEntity() instanceof Player)) return;

        Snowball snowball = (Snowball)e.getDamager();
        if(!(snowball.getShooter() instanceof Player)) return;

        Player attacker = (Player)snowball.getShooter();
        Player victim = (Player)e.getEntity();

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
