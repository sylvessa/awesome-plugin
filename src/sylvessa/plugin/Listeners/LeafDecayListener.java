package sylvessa.plugin.Listeners;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;

public class LeafDecayListener implements Listener {
    @EventHandler
    public void onLeafDecay(LeavesDecayEvent event) {
        World world = event.getBlock().getWorld();
        if(!world.getName().equals("world") && !world.getName().equals("world_nether")) {
            event.setCancelled(true);
        }
    }
}
