package sylvessa.cb1337.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public class PortalListener implements Listener {
    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        World w = event.getPlayer().getWorld();
        if (!w.getName().equalsIgnoreCase("world") && !w.getName().equalsIgnoreCase("world_nether") && !w.getName().equalsIgnoreCase("world_the_end")) {
            //event.getPlayer().sendMessage(ChatColor.RED + "Cannot let you do that.");
            event.setCancelled(true);
        }
    }
}
