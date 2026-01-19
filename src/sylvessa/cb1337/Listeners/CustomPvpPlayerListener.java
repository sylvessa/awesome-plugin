package sylvessa.cb1337.Listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class CustomPvpPlayerListener extends PlayerListener {
    @Override
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

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        Location to = event.getTo();

        if (to == null) return;
        if (!p.getWorld().getName().equalsIgnoreCase("creative")) return;
        if (to.getY() >= -5) return;

        Location spawn = new Location(p.getWorld(), 0, 64, 0);
        p.teleport(spawn);
    }
}
