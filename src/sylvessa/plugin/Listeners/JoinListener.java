package sylvessa.plugin.Listeners;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import sylvessa.plugin.Log;
import sylvessa.plugin.Main;
import sylvessa.plugin.UserConfig;

public class JoinListener extends PlayerListener {
    private final Main plugin;

    public JoinListener(Main plugin) {
        this.plugin = plugin;
    }

    public void onPlayerJoin(PlayerJoinEvent event) {
        String name = event.getPlayer().getName();

        UserConfig uc = new UserConfig(event.getPlayer(), plugin);
        plugin.getUserConfigs().put(name.toLowerCase(), uc);

        int joins = uc.getInt("joins", 0);
        uc.set("joins", joins + 1);
        uc.save();

        String color = uc.getString("color", "f");

        //event.getPlayer().sendMessage("you have joined " + (joins + 1) + " times");


        event.setJoinMessage("§e"
                +
                (!color.equals("f") ? "§" + color : "")  // show color if  custom
                +
                name
                +
                "§e joined the game. §8(Joined " +
                joins
                + " time" + (joins != 1 ? "s" : "") + ")§f");
    }


    public void onPlayerQuit(PlayerQuitEvent event) {
        String name = event.getPlayer().getName().toLowerCase();

        Log.info("Saving " + name + "'s data.");

        UserConfig uc = plugin.getUserConfigs().get(name);
        if(uc != null) {
            uc.save();
        }

        plugin.getUserConfigs().remove(name);

        Log.info("Saved " + name + "'s data!");
    }
}
