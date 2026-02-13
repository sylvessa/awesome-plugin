package sylvessa.spigot.Minigames.Games;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.util.Vector;
import sylvessa.spigot.Main;
import sylvessa.spigot.Minigames.Minigame;
import sylvessa.spigot.Minigames.MinigameManager;
import sylvessa.spigot.Minigames.MinigameType;
import sylvessa.spigot.Util.GTBList;

import java.util.*;

public class GuessTheBuildMinigame extends Minigame {

    protected Player builder;
    protected Player lastBuilder;
    protected String word;
    protected boolean[] revealed;
    protected boolean roundActive = false;
    protected long roundStart;
    protected long roundEndTime;
    protected int roundLengthTicks = 1800;
    protected int roundId = 0;

    protected Map<Player, Integer> points = new HashMap<>();
    protected Map<Player, Integer> buildCount = new HashMap<>();
    protected Set<Player> guessed = new HashSet<>();

    protected boolean ended = false;

    private int roundEndTaskId = -1;

    private static final String[] WORDS = GTBList.getWordList();

    public GuessTheBuildMinigame(List<Player> players) {
        super(players,
                "minigame_guess_lobby",
                "minigame_guess_arena_happyworld",
                new Vector(343, 108, 424),
                6000,
                6000,
                ChatColor.YELLOW + "Welcome to Guess the Build!\n" +
                        ChatColor.GOLD + "You are given 90 seconds to guess someones build.\n" +
                        ChatColor.GREEN + "Whoever guesses the most builds correctly wins!"
        );
    }

    public MinigameType getType() { return MinigameType.GTB; }
    public int minPlayers() { return 2; }
    public int maxPlayers() { return 15; }

    public void teleportToArena() {
        Location center = new Location(world, 313, 92, 611);
        for(Player p : players) {
            double x = 293 + Math.random() * 40;
            double z = 591 + Math.random() * 40;
            double dx = center.getX() - x;
            double dz = center.getZ() - z;
            float yaw = (float)Math.toDegrees(Math.atan2(-dx, dz));
            Location loc = new Location(world, x, 92, z);
            loc.setYaw(yaw);
            p.teleport(loc);
            p.setGameMode(GameMode.CREATIVE);
        }
    }

    public void startGame() {
        started = true;
        for(Player p : players) {
            points.put(p, 0);
            buildCount.put(p, 0);
            p.setGameMode(GameMode.CREATIVE);
            p.sendMessage("§aGuess The Build started");
        }
        startRound();
    }

    public void endGame() {
        if(ended) return;
        ended = true;

        Player winner = null;
        int best = -1;

        for(Player p : players) {
            int pts = points.get(p);
            p.sendMessage("§eYour Total Points: §a" + pts);
            if(pts > best) {
                best = pts;
                winner = p;
            }
        }

        if(winner != null) {
            Bukkit.broadcastMessage("§6Guess The Build winner: §a" + winner.getName() + " §7(" + points.get(winner) + ")");
        }
    }

    private void startRound() {
        if(ended) return;
        roundId++;

        if(players.size() < minPlayers()) return;
        if(allBuiltTwice()) {
            MinigameManager.end(this);
            return;
        }

        builder = selectNextBuilder();
        if(builder == null) {
            MinigameManager.end(this);
            return;
        }

        lastBuilder = builder;
        buildCount.put(builder, buildCount.get(builder) + 1);

        word = WORDS[(int)(Math.random() * WORDS.length)];
        revealed = new boolean[word.length()];
        guessed.clear();
        roundActive = true;
        roundStart = System.currentTimeMillis();
        roundEndTime = roundStart + roundLengthTicks * 50L;

        int myRound = roundId;

        clearBuilderArea();

        for(Player p : players) {
            p.setGameMode(GameMode.CREATIVE);
            if(p == builder) {
                p.sendMessage("§eYou are building: §a" + word);
            } else {
                p.sendMessage("§eBuilder: §a" + builder.getName());
                p.sendMessage("§eWord: " + getWordDisplay());
            }
        }

        int reveals = Math.max(1, word.replace(" ", "").length() - 1);
        int revealDelay = roundLengthTicks / reveals;

        Bukkit.getScheduler().scheduleSyncDelayedTask(
                Main.getInstance(),
                () -> revealRandomLetter(myRound),
                revealDelay
        );

        Bukkit.getScheduler().scheduleSyncDelayedTask(
                Main.getInstance(),
                () -> tickTimeBroadcast(myRound),
                20L
        );

        if(roundEndTaskId != -1) Bukkit.getScheduler().cancelTask(roundEndTaskId);
        long delayTicks = (roundEndTime - System.currentTimeMillis()) / 50;
        roundEndTaskId = Bukkit.getScheduler().scheduleSyncDelayedTask(
                Main.getInstance(),
                () -> endRound(myRound),
                delayTicks
        );
    }

    private void revealRandomLetter(int id) {
        if(!roundActive || id != roundId || ended) return;

        List<Integer> hidden = new ArrayList<>();
        for(int i = 0; i < word.length(); i++) {
            if(word.charAt(i) != ' ' && !revealed[i]) hidden.add(i);
        }
        if(hidden.isEmpty()) return;

        int idx = hidden.get((int)(Math.random() * hidden.size()));
        revealed[idx] = true;

        for(Player p : players) if(p != builder) p.sendMessage("§eWord: " + getWordDisplay());

        int reveals = Math.max(1, word.replace(" ", "").length() - 1);
        int revealDelay = roundLengthTicks / reveals;

        Bukkit.getScheduler().scheduleSyncDelayedTask(
                Main.getInstance(),
                () -> revealRandomLetter(id),
                revealDelay
        );
    }

    private void tickTimeBroadcast(int id) {
        if(!roundActive || id != roundId || ended) return;

        long left = Math.max(0, (roundEndTime - System.currentTimeMillis()) / 1000);
        if(left > 0 && left % 10 == 0) {
            for(Player p : players) p.sendMessage("§eTime left: §a" + left + "s");
        }

        if(roundActive) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(
                    Main.getInstance(),
                    () -> tickTimeBroadcast(id),
                    20L
            );
        }
    }

    private void endRound(int id) {
        if(!roundActive || id != roundId || ended) return;

        roundActive = false;

        if(roundEndTaskId != -1) {
            Bukkit.getScheduler().cancelTask(roundEndTaskId);
            roundEndTaskId = -1;
        }

        for(Player p : players) {
            p.sendMessage("§cWord was: §a" + word);
            p.sendMessage("");
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(
                Main.getInstance(),
                this::startRound,
                100L
        );
    }

    @Override
    public void onChat(Player p, AsyncPlayerChatEvent e) {
        if(!roundActive || p == builder || guessed.contains(p) || ended) return;

        String guess = e.getMessage().toLowerCase().replace(" ", "");
        String target = word.toLowerCase().replace(" ", "");

        if(!guess.equals(target)) return;

        e.setCancelled(true);

        boolean firstGuess = guessed.isEmpty();
        guessed.add(p);

        long total = roundLengthTicks / 20;
        long left = Math.max(0, (roundEndTime - System.currentTimeMillis()) / 1000);

        int baseScore = (int)(5 + ((double)left / total) * 15);
        int bonus = firstGuess ? 5 : 0;
        int score = baseScore + bonus;

        points.put(p, points.get(p) + score);

        for(Player pl : players) {
            if(firstGuess) {
                pl.sendMessage("§6" + p.getName() + " was first to guess! §7(+" + score + ")");
            } else {
                pl.sendMessage("§a" + p.getName() + " guessed the word! §7(+" + score + ")");
            }
        }

        roundEndTime -= 10_000;

        if(roundEndTaskId != -1) Bukkit.getScheduler().cancelTask(roundEndTaskId);
        long newDelayTicks = Math.max(0, (roundEndTime - System.currentTimeMillis()) / 50);
        roundEndTaskId = Bukkit.getScheduler().scheduleSyncDelayedTask(
                Main.getInstance(),
                () -> endRound(roundId),
                newDelayTicks
        );

        if(guessed.size() >= players.size() - 1) {
            Bukkit.broadcastMessage("§eEveryone has guessed the word!");
            endRound(roundId);
        }
    }

    public void onQuit(Player p) {
        players.remove(p);
        points.remove(p);
        buildCount.remove(p);
        guessed.remove(p);

        if(p == builder) {
            roundActive = false;
            roundId++;
            Bukkit.getScheduler().scheduleSyncDelayedTask(
                    Main.getInstance(),
                    this::startRound,
                    40L
            );
        }

        if(players.size() < minPlayers()) {
            roundActive = false;
        }
    }

    public void onMove(Player p) {
        if(!started) p.teleport(p.getLocation());
        if (p.getLocation().getY() > 127) p.teleport(p.getLocation().add(0, -4, 0));
    }

    public void onMoveInQueue(Player p) {
        if(p.getLocation().getY() >= 105) return;

        Location from = p.getLocation();
        Location target = new Location(from.getWorld(), 343, from.getY(), 424);

        Vector horiz = target.toVector().subtract(from.toVector());
        horiz.setY(0);
        horiz.normalize().multiply(1.5);

        p.setVelocity(new Vector(horiz.getX(), 1.3, horiz.getZ()));
    }

    @Override
    public boolean canBreak(Player p, BlockBreakEvent e) {
        if(!roundActive || p != builder) return false;
        Block b = e.getBlock();
        int x = b.getX();
        int y = b.getY();
        int z = b.getZ();
        return y >= 91 && x >= 293 && x <= 333 && z >= 591 && z <= 631;
    }

    @Override
    public boolean canPlace(Player p, BlockPlaceEvent e) {
        if(!roundActive || p != builder) return false;
        Block b = e.getBlock();
        int x = b.getX();
        int y = b.getY();
        int z = b.getZ();

        if(y < 91 || x < 293 || x > 333 || z < 591 || z > 631) return false;

        Material block = b.getType();
        Material item = e.getItemInHand().getType();

        if(block == Material.FIRE || block == Material.LAVA || block == Material.STATIONARY_LAVA
                || block == Material.WATER || block == Material.STATIONARY_WATER) return false;

        if(item == Material.FLINT_AND_STEEL || item == Material.LAVA_BUCKET
                || item == Material.WATER_BUCKET) return false;

        return true;
    }

    @Override
    public void onBucketEmpty(Player p, PlayerBucketEmptyEvent event) {
        event.setCancelled(true);
    }

    private boolean allBuiltTwice() {
        for(Player p : players) if(buildCount.get(p) < 2) return false;
        return true;
    }

    private Player selectNextBuilder() {
        List<Player> eligible = new ArrayList<>();
        for(Player p : players) if(buildCount.get(p) < 2 && p != lastBuilder) eligible.add(p);

        if(eligible.isEmpty()) {
            for(Player p : players) if(buildCount.get(p) < 2) return p;
            return null;
        }

        return eligible.get((int)(Math.random() * eligible.size()));
    }

    private String getWordDisplay() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if(c == ' ') sb.append("  ");
            else {
                sb.append(revealed[i] ? c : '_');
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private void clearBuilderArea() {
        for(int x = 293; x <= 333; x++) {
            for(int y = 91; y <= world.getMaxHeight(); y++) {
                for(int z = 591; z <= 631; z++) {
                    world.getBlockAt(x, y, z).setType(Material.AIR);
                }
            }
        }
    }
}
