package sylvessa.spigot.Listeners;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherListener implements Listener {
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        World world = event.getWorld();
        if(!world.getName().equals("world") && !world.getName().equals("world_nether") && !world.getName().equals("world_the_end")) {
            event.setCancelled(true);
        }
    }
}
