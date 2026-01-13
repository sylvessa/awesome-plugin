package sylvessa.plugin.Listeners;

import net.minecraft.server.Packet29DestroyEntity;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class TreeMobSpawnListener implements Listener {

    @EventHandler(priority = Event.Priority.Highest)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
//        World world = event.getLocation().getWorld();
//        if (!world.getName().equals("world") && !world.getName().equals("world_nether")) {
//            if(event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM) {
//                event.setCancelled(true);
//                return;
//            }
//        }

        switch(event.getCreatureType()) {
            case ZOMBIE:
            case SKELETON:
            case CREEPER:
            case SPIDER:
                break;
            default:
                return;
        }

        //Log.info("Mob spawned");

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
