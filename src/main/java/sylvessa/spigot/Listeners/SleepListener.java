package sylvessa.spigot.Listeners;

import net.minecraft.server.v1_6_R3.Packet18ArmAnimation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import sylvessa.spigot.Main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SleepListener implements Listener {
    private final Map<World, Set<Player>> votes = new HashMap<>();
    private final Map<World, Boolean> messageSent = new HashMap<>();

    @EventHandler
    public void onPlayerBedEnter(final PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            if (!player.isSleeping()) return;
            if (world.getPlayers().size() < 2) return;

            votes.putIfAbsent(world, new HashSet<>());
            messageSent.putIfAbsent(world, false);

            int nonAFK = 0;
            for (Player p : world.getPlayers()) {
                if (!AFKListener.isAFK(p)) nonAFK++;
            }

            if (nonAFK <= 1) {
                skipNight(world);
                votes.get(world).clear();
                messageSent.put(world, false);
                return;
            }

            if (!messageSent.get(world)) {
                Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + " wants to skip the night! Type Y in chat to vote.");
                messageSent.put(world, true);
            }
        }, 1L);
    }

    @EventHandler
    public void onPlayerBedLeave(final PlayerBedLeaveEvent event) {
        final Player player = event.getPlayer();
        final World world = player.getWorld();

        if (votes.containsKey(world)) {
            votes.get(world).remove(player);
        }

        boolean anyoneSleeping = false;
        for (Player p : world.getPlayers()) {
            if (p.isSleeping()) {
                anyoneSleeping = true;
                break;
            }
        }

        if (!anyoneSleeping) {
            messageSent.put(world, false);
            if (votes.containsKey(world)) votes.get(world).clear();
        }
    }

    @EventHandler
    public void onChatVote(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();

        if (!votes.containsKey(world) || !messageSent.getOrDefault(world, false)) {
            return;
        }

        if (!event.getMessage().equalsIgnoreCase("Y")) return;

        event.setCancelled(true);

        Player bedInitiator = null;
        for (Player p : world.getPlayers()) {
            if (p.isSleeping()) {
                bedInitiator = p;
                break;
            }
        }

        if (player.equals(bedInitiator)) return;

        Set<Player> worldVotes = votes.get(world);

        if (!worldVotes.contains(player)) {
            worldVotes.add(player);

            int totalNeeded = 0;
            int votesSoFar = worldVotes.size();

            for (Player p : world.getPlayers()) {
                if (!AFKListener.isAFK(p) && !p.equals(bedInitiator)) totalNeeded++;
            }

            Bukkit.broadcastMessage(ChatColor.AQUA.toString() + votesSoFar + "/" + totalNeeded + " players have voted to skip the night");

            if (votesSoFar >= totalNeeded) {
                skipNight(world);
                worldVotes.clear();
                messageSent.put(world, false);
            }
        }
    }

    private void skipNight(World world) {
        long time = world.getTime();
        long ticksToDay = 24000 - (time % 24000);
        world.setTime(time + ticksToDay);
        world.setStorm(false);

        for (Player p : world.getPlayers()) {
            if (p.isSleeping()) {
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new Packet18ArmAnimation(((CraftPlayer) p).getHandle(), 3));
            }
        }

        Bukkit.broadcastMessage(ChatColor.GREEN + "Night skipped!");
    }
}
