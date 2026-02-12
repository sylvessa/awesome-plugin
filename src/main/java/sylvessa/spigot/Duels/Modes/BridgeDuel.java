package sylvessa.spigot.Duels.Modes;

import net.minecraft.server.v1_4_R1.Packet9Respawn;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_4_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import sylvessa.spigot.ChunkGenerators.Void;
import sylvessa.spigot.Duels.DuelGame;
import sylvessa.spigot.Duels.DuelManager;
import sylvessa.spigot.Duels.DuelType;
import sylvessa.spigot.Main;
import sylvessa.spigot.Util.CustomWorldLoader;
import sylvessa.spigot.Util.DiscordWebhook;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;

import static sylvessa.spigot.Util.Helpers.freezeWorldTime;

public class BridgeDuel extends DuelGame {
    private int countdown = 5;
    private int taskId = -1;
    private final Random r = new Random();

    private Location noteBlockP1, noteBlockP2;
    private Location player1Spawn, player2Spawn;

    private final Map<UUID, Long> bowCooldown = new HashMap<>();

    private Team p1Team, p2Team;
    private int redScore = 0, blueScore = 0;
    private final int WIN_SCORE = 5;

    private final Map<Location, ItemStack> arenaSnapshot = new HashMap<>();
    private final int arenaRadius = 5, arenaAbove = 5, arenaBelow = 2;

    private final int redMinX = -157, redMaxX = -151, redMinY = 56, redMaxY = 58, redMinZ = -32, redMaxZ = -27;
    private final int blueMinX = -157, blueMaxX = -151, blueMinY = 56, blueMaxY = 58, blueMinZ = 20, blueMaxZ = 24;

    public enum Team { RED, BLUE }

    public BridgeDuel(Player p1, Player p2) { super(p1, p2); }

    public DuelType getType() { return DuelType.BRIDGE; }

    public void start() {
        createWorld();
        freezeWorldTime(world, 6000);
        assignTeamsAndSpawns();
        teleportAndPreparePlayers();
        startCountdown();
    }

    private void createWorld() {
        try {
            String name = "duel_bridge_" + new Random().nextInt(1000000);
            CustomWorldLoader.copyArenaToServerJar("duel_bridge_main", name);
            world = Bukkit.createWorld(
                    new WorldCreator(name)
                            .environment(World.Environment.NORMAL)
                            .generator(new Void())
            );
        } catch (Exception ignored) {}
    }

    private void assignTeamsAndSpawns() {
        if (r.nextBoolean()) {
            p1Team = Team.RED; p2Team = Team.BLUE;
            player1Spawn = new Location(world, -154, 70, -29, 0, 0);
            player2Spawn = new Location(world, -154, 70, 22, 180, 0);
        } else {
            p1Team = Team.BLUE; p2Team = Team.RED;
            player1Spawn = new Location(world, -154, 70, 22, 180, 0);
            player2Spawn = new Location(world, -154, 70, -29, 0, 0);
        }
    }

    private void teleportAndPreparePlayers() {
        forEachPlayer(p -> {
            Location spawn = (p == p1 ? player1Spawn : player2Spawn);
            p.teleport(spawn);
            p.setHealth(20);
            p.setFoodLevel(20);
            p.setSaturation(20);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> refillItems(p), 1L);
        });

        player1Spawn = player1Spawn.clone().add(0, -3, 0);
        player2Spawn = player2Spawn.clone().add(0, -3, 0);
        noteBlockP1 = placeNoteBlockBehind(p1);
        noteBlockP2 = placeNoteBlockBehind(p2);
    }

    private void startCountdown() {
        if (finished) return;
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            if (finished) { Bukkit.getScheduler().cancelTask(taskId); return; }

            if (countdown == 0) {
                started = true;
                sendMessageAll("§aGO");
                playNoteAll(noteBlockP1, noteBlockP2, Note.Tone.C);
                noteBlockP1.getBlock().setType(Material.AIR);
                noteBlockP2.getBlock().setType(Material.AIR);
                clearArenaAroundPlayers();
                Bukkit.getScheduler().cancelTask(taskId);
                return;
            }

            sendMessageAll("§e" + countdown);
            playNoteAll(noteBlockP1, noteBlockP2, Note.Tone.G);
            countdown--;
        }, 0L, 20L);
    }

    private void clearArenaAroundPlayers() {
        forEachPlayer(p -> {
            Location loc = p.getLocation();
            int baseX = loc.getBlockX(), baseY = loc.getBlockY(), baseZ = loc.getBlockZ();
            for (int x = -arenaRadius; x <= arenaRadius; x++)
                for (int z = -arenaRadius; z <= arenaRadius; z++)
                    for (int y = -arenaBelow; y <= arenaAbove; y++) {
                        Block b = world.getBlockAt(baseX + x, baseY + y, baseZ + z);
                        arenaSnapshot.put(b.getLocation().clone(), b.getState().getData().toItemStack());
                        if (b.getType() != Material.AIR) b.setType(Material.AIR);
                    }
        });
    }

    private Location placeNoteBlockBehind(Player p) {
        Location l = p.getLocation(); float yaw = l.getYaw(); int dx = 0, dz = 0;
        if (yaw >= -45 && yaw < 45) dz = -1;
        else if (yaw >= 45 && yaw < 135) dx = -1;
        else if (yaw >= -135 && yaw < -45) dx = 1;
        else dz = 1;
        Location b = l.clone().add(dx, -3, dz);
        b.getBlock().setType(Material.NOTE_BLOCK);
        return b;
    }

    public void onMove(Player p) {
        if (!started || finished) return;
        Location loc = p.getLocation();
        Team team = (p == p1 ? p1Team : p2Team);


        boolean inOwnHole = (team == Team.RED && inZone(loc, redMinX, redMaxX, redMinY, redMaxY, redMinZ, redMaxZ))
                || (team == Team.BLUE && inZone(loc, blueMinX, blueMaxX, blueMinY, blueMaxY, blueMinZ, blueMaxZ));

        boolean inOppHole = (team == Team.RED && inZone(loc, blueMinX, blueMaxX, blueMinY, blueMaxY, blueMinZ, blueMaxZ))
                || (team == Team.BLUE && inZone(loc, redMinX, redMaxX, redMinY, redMaxY, redMinZ, redMaxZ));

        if (inOwnHole) { respawnPlayer(p); return; }
        if (inOppHole) { addPoint(team); if (!finished) startNextRound(); return; }
        if (loc.getY() < 45) respawnPlayer(p);
    }

    private boolean inZone(Location loc, int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
        return loc.getBlockX() >= minX && loc.getBlockX() <= maxX
                && loc.getBlockY() >= minY && loc.getBlockY() <= maxY
                && loc.getBlockZ() >= minZ && loc.getBlockZ() <= maxZ;
    }

    private void startNextRound() {
        if (finished) return;
        resetArena();

        player1Spawn = player1Spawn.clone().add(0, 3, 0);
        player2Spawn = player2Spawn.clone().add(0, 3, 0);

        teleportAndPreparePlayers();
        countdown = 5;
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), this::startCountdown, 40L);
    }

    private void resetArena() {
        arenaSnapshot.forEach((loc, stack) -> {
            Block b = loc.getBlock();
            b.setType(stack.getType());
            if (stack.getType() == Material.WOOL) b.setData(stack.getData().getData());
        });
        arenaSnapshot.clear();
    }

    private void respawnPlayer(Player p) {
        p.setHealth(20); p.setFireTicks(0); p.setFallDistance(0);
        p.teleport(p == p1 ? player1Spawn : player2Spawn);
        refillItems(p);
    }

    private void addPoint(Team team) {
        if (team == Team.RED) redScore++; else blueScore++;
        broadcastScore(team);
        if ((team == Team.RED && redScore >= WIN_SCORE) || (team == Team.BLUE && blueScore >= WIN_SCORE))
            endGame(team);
    }

    private void broadcastScore(Team scoringTeam) {
        Player scorer = (scoringTeam == Team.RED ? (p1Team == Team.RED ? p1 : p2) : (p1Team == Team.BLUE ? p1 : p2));
        String line = "§e-------------------------------------";
        String msg1 = centerText((scoringTeam == Team.RED ? "§c" : "§9") + scorer.getName() + " §escored!", 37);

        forEachPlayer(p -> {
            boolean isRed = (p == p1 ? p1Team : p2Team) == Team.RED;
            String scoreLine = centerText((isRed ? "§c" + redScore : "§9" + blueScore)
                    + " §7- "
                    + (isRed ? "§9" + blueScore : "§c" + redScore), 37);
            p.sendMessage(line); p.sendMessage(msg1); p.sendMessage(scoreLine); p.sendMessage(line);
        });
    }

    private String centerText(String text, int totalLength) {
        int textLength = ChatColor.stripColor(text).length();
        int padding = (totalLength - textLength) / 2 + 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.max(0, padding); i++) sb.append(" ");
        sb.append(text);
        return sb.toString();
    }

    private void forEachPlayer(Consumer<Player> action) { action.accept(p1); action.accept(p2); }

    public boolean canBreak(Player p, BlockBreakEvent e) { return started && e.getBlock().getType() == Material.WOOL; }

    public boolean canPlace(Player p, BlockPlaceEvent e) {
        if (!started || countdown > 0) return false;
        return !inZone(e.getBlockPlaced().getLocation(), redMinX, redMaxX, redMinY, redMaxY, redMinZ, redMaxZ)
                && !inZone(e.getBlockPlaced().getLocation(), blueMinX, blueMaxX, blueMinY, blueMaxY, blueMinZ, blueMaxZ);
    }

    public void onDamage(Player p, EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL) { e.setCancelled(true); return; }
        //if (p.getHealth() - e.getDamage() <= 0) { e.setCancelled(true); respawnPlayer(p); }
    }

    @Override
    public void onDeath(Player p, EntityDeathEvent event) {
        event.getDrops().clear();

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            ((CraftPlayer) p).getHandle().playerConnection.a(new Packet9Respawn());
        }, 2L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            respawnPlayer(p);
        }, 4L);
    }

    private void endGame(Team winnerTeam) {
        if (finished) return;
        finished = true;

        Player winner = (winnerTeam == Team.RED ? (p1Team == Team.RED ? p1 : p2) : (p1Team == Team.BLUE ? p1 : p2));
        Player loser = winner == p1 ? p2 : p1;

        int winnerScore = winnerTeam == Team.RED ? redScore : blueScore;
        int loserScore = winnerTeam == Team.RED ? blueScore : redScore;

        Bukkit.broadcastMessage("§a" + winner.getName() + " won a bridge duel against " + loser.getName() + "! §7(" + winnerScore + " - " + loserScore + ")");

        new DiscordWebhook()
                .setUsername(winner.getName())
                .setAvatarUrl("https://mc-heads.net/avatar/" + winner.getName())
                .sendMessage(winner.getName() + " won a bridge duel against " + loser.getName() + "! (" + winnerScore + " - " + loserScore + ")", 16776960);

        cleanup();
    }


    public void onQuit(Player p) { if (!finished) markLoser(p); }
    private void markLoser(Player p) {
        finished = true;
        Player loser = p == p1 ? p1 : p2;
        Player winner = loser == p1 ? p2 : p1;
        Bukkit.broadcastMessage("§a" + winner.getName() + " won a bridge duel against " + loser.getName() + " §7(FORFEIT)");

        new DiscordWebhook()
                .setUsername(winner.getName())
                .setAvatarUrl("https://mc-heads.net/avatar/" + winner.getName())
                .sendMessage(winner.getName() + " won a bridge duel against " + loser.getName() + " (FORFEIT)", 16776960);

        cleanup();
    }

//    public void onBowShoot(Player p) {
//        Log.info("Player shot arrow");
//        long now = System.currentTimeMillis();
//        Long last = bowCooldown.get(p.getUniqueId());
//
//        if (last != null && now - last < 3500) return;
//
//        bowCooldown.put(p.getUniqueId(), now);
//
//        Bukkit.getScheduler().scheduleSyncDelayedTask(
//                Main.getInstance(),
//                () -> p.getInventory().setItem(8, new ItemStack(Material.ARROW, 1)),
//                70L
//        );
//    }

    public void onFoodLevelChange(Player p, FoodLevelChangeEvent event) { event.setCancelled(true); }

    public void refillItems(Player p) {
        Team team = (p == p1 ? p1Team : p2Team);
        p.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET, 1, (short)0));
        p.getInventory().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1, (short)0));
        p.getInventory().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS, 1, (short)0));
        p.getInventory().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS, 1, (short)0));
        p.getInventory().setItem(0, new ItemStack(Material.IRON_SWORD, 1, (short)0));
        p.getInventory().setItem(1, new ItemStack(Material.BOW, 1, (short)0));
        p.getInventory().setItem(2, new ItemStack(Material.SHEARS, 1, (short)0));
        byte woolData = team == Team.RED ? (byte)14 : (byte)11;
        for (int i = 3; i <= 5; i++) p.getInventory().setItem(i, new ItemStack(Material.WOOL, 64, woolData));
        p.getInventory().setItem(6, new ItemStack(Material.GOLDEN_APPLE, 8, (short)0));
        p.getInventory().setItem(8, new ItemStack(Material.ARROW, 64, (short)0));
    }

    private void sendMessageAll(String msg) { p1.sendMessage(msg); p2.sendMessage(msg); }
    private void playNoteAll(Location l1, Location l2, Note.Tone tone) {
        p1.playNote(l1, Instrument.PIANO, new Note((byte)1, tone, false));
        p2.playNote(l2, Instrument.PIANO, new Note((byte)1, tone, false));
    }

    private void cleanup() { DuelManager.end(this); }
}
