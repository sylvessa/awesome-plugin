package sylvessa.spigot.Listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

public class BlockIceListener implements Listener {

    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        if(event.getNewState().getType() != Material.ICE) return;

        Location spawn = event.getBlock().getWorld().getSpawnLocation();
        Location blockLoc = event.getBlock().getLocation();

        double radiusSquared = 1000 * 1000;
        if(blockLoc.distanceSquared(spawn) <= radiusSquared && event.getBlock().getWorld().getName().equals("world")) {
            event.setCancelled(true);
        }
    }
}