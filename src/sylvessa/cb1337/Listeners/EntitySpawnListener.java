package sylvessa.cb1337.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityListener;
import sylvessa.cb1337.Log;
import sylvessa.cb1337.Main;

public class EntitySpawnListener extends EntityListener {
    @Override
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        World w = event.getLocation().getWorld();
        String name = w.getName();

        if (!name.equals("world") && !name.equals("world_nether") && !name.equals("world_the_end")) {
            //Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> event.getEntity().remove(), 1L);
            event.setCancelled(true);

        }
    }
}
