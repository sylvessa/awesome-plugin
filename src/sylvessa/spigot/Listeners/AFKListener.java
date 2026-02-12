package sylvessa.spigot.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import sylvessa.spigot.Main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AFKListener implements Listener {
    private static final Set<String> afkPlayers = new HashSet<>();
    private final long afkTimeout = 2 * 60 * 1000;
    private final Map<String, Long> lastActivity = new HashMap<>();

    public AFKListener() {
        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
            long now = System.currentTimeMillis();
            for (Player p : Bukkit.getOnlinePlayers()) {
                String name = p.getName();
                if (!afkPlayers.contains(name) && lastActivity.getOrDefault(name, now) + afkTimeout <= now) {
                    afkPlayers.add(name);
                    p.sendMessage("§eYou are now AFK.");
                }
            }
        }, 20L, 20L * 30);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        String name = event.getPlayer().getName();
        if (afkPlayers.remove(name)) {
            event.getPlayer().sendMessage("§aYou are no longer AFK.");
        }
        lastActivity.put(name, System.currentTimeMillis());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String name = event.getPlayer().getName();
        afkPlayers.remove(name);
        lastActivity.remove(name);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        lastActivity.put(event.getPlayer().getName(), System.currentTimeMillis());
    }

    public static boolean isAFK(Player player) {
        return afkPlayers.contains(player.getName());
    }

    public static Set<Player> getAFKPlayers() {
        Set<Player> set = new HashSet<>();
        for (String name : afkPlayers) {
            Player p = Bukkit.getPlayer(name);
            if (p != null) set.add(p);
        }
        return set;
    }
}
