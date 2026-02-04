package sylvessa.cb1337.Listeners;

import net.minecraft.server.Packet18ArmAnimation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerChatEvent;
import sylvessa.cb1337.Main;

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

            votes.putIfAbsent(world, new HashSet<>());
            messageSent.putIfAbsent(world, false);

            // count non-AFK players
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
    public void onChatVote(PlayerChatEvent event) {
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

            int totalNeeded = 0; // -1 cause deduct one for the player who inited vote
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
                ((CraftPlayer) p).getHandle().netServerHandler.sendPacket(new Packet18ArmAnimation(((CraftPlayer) p).getHandle(), 3));
            }
        }

        Bukkit.broadcastMessage(ChatColor.GREEN + "Night skipped!");
    }
}
