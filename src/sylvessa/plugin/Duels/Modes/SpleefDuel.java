package sylvessa.plugin.Duels.Modes;

import java.io.File;
import java.util.Random;

import org.bukkit.*;
import org.bukkit.Note.Tone;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import sylvessa.plugin.ChunkGenerators.Void;
import sylvessa.plugin.Duels.*;
import sylvessa.plugin.Main;

public class SpleefDuel extends DuelGame {

    private int countdown = 5;
    private int taskId = -1;

    private boolean p1Fell;
    private boolean p2Fell;

    private final int baseY = 64;
    private final int layers = 3;
    private final int layerHeight = 1;
    private final int airHeight = 4;
    private final int width = 20;
    private final int length = 20;

    private Location noteBlockP1;
    private Location noteBlockP2;

    public SpleefDuel(Player p1, Player p2) {
        super(p1, p2);
    }

    public DuelType getType() {
        return DuelType.SPLEEF;
    }

    public void start() {
        createWorld();
        buildArena();
        preparePlayers();
        startCountdown();
    }

    private void createWorld() {
        String name = "duel_spleef_" + new Random().nextInt(1000000);
        world = Bukkit.createWorld(name, World.Environment.NORMAL, new Void());

        int halfWidth = width / 2;
        int halfLength = length / 2;

        for(int l = 0; l < layers; l++) {
            int y = baseY + l * (layerHeight + airHeight);
            for(int x = -halfWidth; x < halfWidth; x++) {
                for(int z = -halfLength; z < halfLength; z++) {
                    world.getBlockAt(x, y, z).setType(Material.SNOW_BLOCK);
                    for(int ay = 1; ay <= airHeight; ay++) {
                        world.getBlockAt(x, y + ay, z).setType(Material.AIR);
                    }
                }
            }
        }
    }

    private void buildArena() {
        for(int x = -10; x < 10; x++) {
            for(int z = -10; z < 10; z++) {
                world.getBlockAt(x, 64, z).setType(Material.SNOW_BLOCK);
            }
        }
    }

    private void preparePlayers() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(
                Main.getInstance(),
                () -> p1.setItemInHand(new ItemStack(277, 1, (short)0)),
                1L
        );
        Bukkit.getScheduler().scheduleSyncDelayedTask(
                Main.getInstance(),
                () -> p2.setItemInHand(new ItemStack(277, 1, (short)0)),
                1L
        );

        int topLayerY = baseY + (layers - 1) * (layerHeight + airHeight);
        double centerX = 0.0;
        double zOffset = length / 2.0 - 1;

        Location l1 = new Location(world, centerX, topLayerY + 1, -zOffset, 0f, 0f);
        Location l2 = new Location(world, centerX, topLayerY + 1, zOffset, 180f, 0f);

        p1.teleport(l1);
        p2.teleport(l2);

        p1.setHealth(20);
        p2.setHealth(20);

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

                        p1.playNote(noteBlockP1, Instrument.PIANO, new Note((byte)1, Tone.C, false));
                        p2.playNote(noteBlockP2, Instrument.PIANO, new Note((byte)1, Tone.C, false));

                        noteBlockP1.getBlock().setType(Material.AIR);
                        noteBlockP2.getBlock().setType(Material.AIR);
                        return;
                    }

                    p1.sendMessage("§e" + countdown);
                    p2.sendMessage("§e" + countdown);

                    p1.playNote(noteBlockP1, Instrument.PIANO, new Note((byte)1, Tone.G, false));
                    p2.playNote(noteBlockP2, Instrument.PIANO, new Note((byte)1, Tone.G, false));

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

        if(started && !finished && p.getWorld() == world && p.getLocation().getY() < 50) {
            markFall(p);
        }
    }

    private void markFall(Player p) {
        if(finished) return;

        if(p == p1) p1Fell = true;
        if(p == p2) p2Fell = true;

        if(p1Fell && p2Fell) {
            finished = true;
            Bukkit.broadcastMessage("§eSpleef duel ended in a tie");
            cleanup();
            return;
        }

        if(p1Fell || p2Fell) {
            finished = true;
            Player loser = p1Fell ? p1 : p2;
            Player winner = loser == p1 ? p2 : p1;

            Bukkit.broadcastMessage("§a" + winner.getName() + " won a spleef duel against " + loser.getName());

            cleanup();
        }
    }

    public void onDamage(Player p, EntityDamageEvent e) {
        e.setCancelled(true);
    }

    public boolean canBreak(Player p, BlockBreakEvent e) {
        return started;
    }

    public boolean canPlace(Player p, BlockPlaceEvent e) {
        return false;
    }

    public void onQuit(Player p) {
        markFall(p);
    }

    private void cleanup() {
        DuelManager.end(this);
    }
}
