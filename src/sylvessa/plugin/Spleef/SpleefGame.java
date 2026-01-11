package sylvessa.plugin.Spleef;

import java.io.File;
import java.util.Random;

import org.bukkit.*;
import org.bukkit.Note.Tone;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sylvessa.plugin.ChunkGenerators.Void;
import sylvessa.plugin.Main;

public class SpleefGame {

    private final Player p1;
    private final Player p2;
    private World world;
    private boolean started;
    private boolean finished;
    private int countdown = 5;
    private int taskId = -1;

    private ItemStack[] inv1;
    private ItemStack[] inv2;
    private boolean p1Fell;
    private boolean p2Fell;
    private Location locP1;
    private Location locP2;

    private final int baseY = 64;
    private final int layers = 3;
    private final int layerHeight = 1;
    private final int airHeight = 4;
    private final int width = 20;
    private final int length = 20;

    private Location noteBlockP1;
    private Location noteBlockP2;

    public SpleefGame(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
        createWorld();
        buildArena();
        preparePlayers();
        startCountdown();
    }

    private void createWorld() {
        String name = "spleef_" + new Random().nextInt(1000000);

        world = Bukkit.getServer().createWorld(name, org.bukkit.World.Environment.NORMAL, new Void());

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
        locP1 = p1.getLocation().clone();
        locP2 = p2.getLocation().clone();

        inv1 = p1.getInventory().getContents();
        inv2 = p2.getInventory().getContents();

        p1.getInventory().clear();
        p2.getInventory().clear();

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(),
                () -> p1.setItemInHand(new ItemStack(277, 1, (short)0)), 1L);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(),
                () -> p2.setItemInHand(new ItemStack(277, 1, (short)0)), 1L);

        int topLayerY = baseY + (layers - 1) * (layerHeight + airHeight);

        double centerX = 0.0;
        double zOffset = length / 2.0 - 1;

        Location loc1 = new Location(world, centerX, topLayerY + 1, -zOffset);
        loc1.setYaw(0);
        loc1.setPitch(0f);

        Location loc2 = new Location(world, centerX, topLayerY + 1, zOffset);
        loc2.setYaw(180f);
        loc2.setPitch(0f);

        p1.teleport(loc1);
        p2.teleport(loc2);

        noteBlockP1 = placeNoteBlockBehind(p1);
        noteBlockP2 = placeNoteBlockBehind(p2);

    }

    private void startCountdown() {
        taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(
                Main.getInstance(),
                () -> {
                    if(countdown == 0) {
                        started = true;
                        p1.sendMessage("§aGO");
                        p2.sendMessage("§aGO");
                        Bukkit.getServer().getScheduler().cancelTask(taskId);

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

    public boolean isFrozen(Player p) {
        return !started && (p == p1 || p == p2);
    }

    public boolean hasStarted() {
        return started;
    }

    public boolean checkFall(Player p) {
        return started && !finished && p.getWorld() == world && p.getLocation().getY() < 50;
    }

    public void markFall(Player p) {
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

            makeBox(loser.getLocation());
            makeBox(winner.getLocation());

            cleanup();
        }
    }

    private void cleanup() {
        if(locP1 != null) {
            p1.teleport(locP1);
            p1.setFallDistance(0f);
        }
        if(locP2 != null) {
            p2.teleport(locP2);
            p2.setFallDistance(0f);
        }

        p1.getInventory().setContents(inv1);
        p2.getInventory().setContents(inv2);

        SpleefManager.end(this);

        World w = world;
        Bukkit.getServer().unloadWorld(w, true);

        File worldFolder = new File(".", w.getName());

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> deleteWorld(worldFolder), 100L);
    }

    private void deleteWorld(File f) {
        if(f.isDirectory()) {
            for(File c : f.listFiles()) deleteWorld(c);
        }
        f.delete();
    }


    private void makeBox(Location l) {
        int x = l.getBlockX();
        int y = 70;
        int z = l.getBlockZ();

        for(int dx = -1; dx <= 1; dx++) {
            for(int dy = 0; dy <= 2; dy++) {
                for(int dz = -1; dz <= 1; dz++) {
                    if(dx == 0 && dy == 1 && dz == 0) continue;
                    l.getWorld().getBlockAt(x + dx, y + dy, z + dz).setType(Material.GLASS);
                }
            }
        }
    }
    public Player getP1() { return p1; }
    public Player getP2() { return p2; }
}