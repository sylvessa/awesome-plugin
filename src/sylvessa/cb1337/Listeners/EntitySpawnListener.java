package sylvessa.cb1337.Listeners;

import org.bukkit.World;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityListener;

public class EntitySpawnListener extends EntityListener {
    @Override
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        World w = event.getLocation().getWorld();
        String name = w.getName();

        if (!name.equals("world") && !name.equals("world_nether")) {
            event.setCancelled(true);
        }
    }
}
