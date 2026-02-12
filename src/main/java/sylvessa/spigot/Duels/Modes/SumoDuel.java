package sylvessa.spigot.Duels.Modes;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import sylvessa.spigot.ChunkGenerators.Void;
import sylvessa.spigot.Duels.DuelGame;
import sylvessa.spigot.Duels.DuelManager;
import sylvessa.spigot.Duels.DuelType;
import sylvessa.spigot.Main;
import sylvessa.spigot.Util.DiscordWebhook;

import java.util.Random;

import static sylvessa.spigot.Util.Helpers.freezeWorldTime;

public class SumoDuel extends DuelGame {
    private int countdown = 5;
    private int taskId = -1;

    private Location noteBlockP1;
    private Location noteBlockP2;

    private boolean p1Fell;
    private boolean p2Fell;

    private final int baseY = 64;
    private final int radius = 6;

    public SumoDuel(Player p1, Player p2) {
        super(p1, p2);
    }

    public DuelType getType() {
        return DuelType.SUMO;
    }

    public void start() {
        createWorld();
        buildArena();
        freezeWorldTime(world, 6000);
        preparePlayers();
        startCountdown();
    }

    private void createWorld() {
        String name = "duel_sumo_" + new Random().nextInt(1000000);
        world = Bukkit.createWorld(
                new WorldCreator(name)
                        .environment(World.Environment.NORMAL)
                        .generator(new Void()));
    }

    private void buildArena() {
        for(int x = -radius; x <= radius; x++) {
            for(int z = -radius; z <= radius; z++) {
                if(x * x + z * z <= radius * radius) {
                    world.getBlockAt(x, baseY, z).setType(Material.BEDROCK);
                    for(int y = baseY + 1; y <= baseY + 6; y++) {
                        world.getBlockAt(x, y, z).setType(Material.AIR);
                    }
                }
            }
        }
    }

    private void preparePlayers() {
        Location l1 = new Location(world, -radius + 1, baseY + 1, 0, -90f, 0f);
        Location l2 = new Location(world, radius - 1, baseY + 1, 0, 90f, 0f);

        p1.teleport(l1);
        p2.teleport(l2);

        p1.setHealth(20);
        p2.setHealth(20);


        p1.setFoodLevel(20);
        p1.setSaturation(20);

        p2.setFoodLevel(20);
        p2.setSaturation(20);

        noteBlockP1 = placeNoteBlockBehind(p1);
        noteBlockP2 = placeNoteBlockBehind(p2);
    }

    private void startCountdown() {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                Main.getInstance(),
                () -> {
                    if(countdown == 0) {
                        started = true;

                        p1.sendMessage("§aGO");
                        p2.sendMessage("§aGO");

                        Bukkit.getScheduler().cancelTask(taskId);

                        p1.playNote(noteBlockP1, Instrument.PIANO, new Note((byte)1, Note.Tone.C, false));
                        p2.playNote(noteBlockP2, Instrument.PIANO, new Note((byte)1, Note.Tone.C, false));

                        noteBlockP1.getBlock().setType(Material.AIR);
                        noteBlockP2.getBlock().setType(Material.AIR);
                        return;
                    }

                    p1.sendMessage("§e" + countdown);
                    p2.sendMessage("§e" + countdown);

                    p1.playNote(noteBlockP1, Instrument.PIANO, new Note((byte)1, Note.Tone.G, false));
                    p2.playNote(noteBlockP2, Instrument.PIANO, new Note((byte)1, Note.Tone.G, false));

                    countdown--;
                },
                0L,
                20L
        );
    }

    private Location placeNoteBlockBehind(Player p) {
        Location l = p.getLocation();
        float yaw = l.getYaw();
        int dx = 0;
        int dz = 0;

        if(yaw >= -45 && yaw < 45) dz = -1;
        else if(yaw >= 45 && yaw < 135) dx = -1;
        else if(yaw >= -135 && yaw < -45) dx = 1;
        else dz = 1;

        Location b = l.clone().add(dx, -2, dz);
        b.getBlock().setType(Material.NOTE_BLOCK);
        return b;
    }

    public void onMove(Player p) {
        if(!started && isParticipant(p)) {
            p.teleport(p.getLocation());
            return;
        }
        if(started && !finished && p.getWorld() == world && p.getLocation().getY() < baseY - 3) {
            markFall(p);
        }
    }

    private void markFall(Player p) {
        if(finished) return;

        if(p == p1) p1Fell = true;
        if(p == p2) p2Fell = true;

        if(p1Fell && p2Fell) {
            finished = true;
            Bukkit.broadcastMessage("§eSumo duel between " + p1.getName() + " and " + p2.getName() + " ended in a tie");
            cleanup();
            return;
        }

        if(p1Fell || p2Fell) {
            finished = true;

            Player loser = p1Fell ? p1 : p2;
            Player winner = loser == p1 ? p2 : p1;

            Bukkit.broadcastMessage("§a" + winner.getName() + " won a sumo duel against " + loser.getName());

            new DiscordWebhook()
                    .setUsername(winner.getName())
                    .setAvatarUrl("https://mc-heads.net/avatar/" + winner.getName())
                    .sendMessage(winner.getName() + " won a sumo duel against " + loser.getName(), 16776960);

            //BotMessageCreator.sendBotMessage("**" + winner.getName() + "** won a sumo duel against **" + loser.getName() + "**");

            cleanup();
        }
    }

    public void onDamage(Player p, EntityDamageEvent e) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(
                Main.getInstance(),
                () -> {
                    p.setHealth(20);
                    p.setFireTicks(0);
                },
                1L
        );
    }

    public boolean canBreak(Player p, BlockBreakEvent e) {
        return false;
    }

    public boolean canPlace(Player p, BlockPlaceEvent e) {
        return false;
    }

    public void onQuit(Player p) {
        markFall(p);
    }

    @Override
    public void onFoodLevelChange(Player p, FoodLevelChangeEvent event) { event.setCancelled(true); }

    private void cleanup() {
        DuelManager.end(this);
    }
}
