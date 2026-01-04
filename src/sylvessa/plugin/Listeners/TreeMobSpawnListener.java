package sylvessa.plugin.Listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import sylvessa.plugin.Log;

public class TreeMobSpawnListener implements Listener {

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        switch(event.getCreatureType()) {
            case ZOMBIE:
            case SKELETON:
            case CREEPER:
            case SPIDER:
                break;
            default:
                return;
        }

        Log.info("Mob spawned");

        Block b = event.getLocation().getBlock();
        boolean onLeaves = b.getType() == Material.LEAVES;

        Block below = b.getRelative(0, -1, 0);
        if(!onLeaves) {
            onLeaves = below.getType() == Material.LEAVES;
        }

        if(onLeaves) {
            event.setCancelled(true);
//            LivingEntity mob = (LivingEntity) event.getEntity();
//            mob.getWorld().createExplosion(mob.getLocation(), 2.0f);
        }
    }
}
