package sylvessa.cb1337.Listeners;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class CustomPVPListener implements Listener {
    @EventHandler
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

    @EventHandler
    public void onPlayerFish(PlayerFishEvent e) {
        if(e.getCaught() == null) return;
        if(!(e.getCaught() instanceof Player)) return;

        Player attacker = e.getPlayer();
        Player victim = (Player)e.getCaught();

        if(e.getState() != PlayerFishEvent.State.CAUGHT_ENTITY) return;

        int damage = 2;

        double knockbackStrength = 0.4;
        double knockbackY = 0.3;

        Vector v = victim.getLocation().toVector()
                .subtract(attacker.getLocation().toVector())
                .normalize()
                .multiply(knockbackStrength);

        v.setY(knockbackY);

        victim.setVelocity(v);
        victim.damage(damage, attacker);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        Location to = event.getTo();

        if (to == null) return;
        if (!p.getWorld().getName().equalsIgnoreCase("creative")) return;
        if (to.getY() >= -5) return;

        p.teleport(p.getWorld().getSpawnLocation());
    }
}
