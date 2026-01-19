package sylvessa.cb1337.Minigames.Games;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.util.Vector;
import sylvessa.cb1337.Main;
import sylvessa.cb1337.Minigames.Minigame;
import sylvessa.cb1337.Minigames.MinigameManager;
import sylvessa.cb1337.Minigames.MinigameType;
import sylvessa.cb1337.Util.GTBList;

import java.util.*;

public class GuessTheBuildMinigame extends Minigame {

    protected Player builder;
    protected Player lastBuilder;
    protected String word;
    protected boolean[] revealed;
    protected boolean roundActive = false;
    protected long roundStart;
    protected int roundLengthTicks = 1800;
    protected int roundId = 0;

    protected Map<Player, Integer> points = new HashMap<>();
    protected Map<Player, Integer> buildCount = new HashMap<>();
    protected Set<Player> guessed = new HashSet<>();

    protected boolean ended = false;

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
            double x = 293 + Math.random() * (333 - 293);
            double z = 591 + Math.random() * (631 - 591);
            double y = 92;

            double dx = center.getX() - x;
            double dy = center.getY() - y;
            double dz = center.getZ() - z;

            float yaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
            float pitch = (float) Math.toDegrees(-Math.atan2(dy, Math.sqrt(dx*dx + dz*dz)));

            Location loc = new Location(world, x, y, z);
            loc.setYaw(yaw);
            loc.setPitch(pitch);

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
            p.sendMessage("§ePoints: §a" + pts);
            if(pts > best) {
                best = pts;
                winner = p;
            }
        }

        if(winner != null) {
            Bukkit.broadcastMessage("§6Guess The Build winner: §a" + winner.getName());
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

        int reveals = Math.max(1, word.length() - 1);
        int revealDelay = roundLengthTicks / reveals;

        Bukkit.getScheduler().scheduleSyncDelayedTask(
                Main.getInstance(),
                () -> revealRandomLetter(myRound),
                revealDelay
        );

        Bukkit.getScheduler().scheduleSyncDelayedTask(
                Main.getInstance(),
                () -> tickTimeBroadcast(myRound),
                200L
        );

        Bukkit.getScheduler().scheduleSyncDelayedTask(
                Main.getInstance(),
                () -> endRound(myRound),
                roundLengthTicks
        );
    }

    private void revealRandomLetter(int id) {
        if(!roundActive || id != roundId || ended) return;

        List<Integer> hidden = new ArrayList<>();
        for(int i = 0; i < revealed.length; i++) if(!revealed[i]) hidden.add(i);
        if(hidden.isEmpty()) return;

        int idx = hidden.get((int)(Math.random() * hidden.size()));
        revealed[idx] = true;

        for(Player p : players) if(p != builder) p.sendMessage("§eWord: " + getWordDisplay());

        int reveals = Math.max(1, word.length() - 1);
        int revealDelay = roundLengthTicks / reveals;

        Bukkit.getScheduler().scheduleSyncDelayedTask(
                Main.getInstance(),
                () -> revealRandomLetter(id),
                revealDelay
        );
    }

    private void tickTimeBroadcast(int id) {
        if(!roundActive || id != roundId || ended) return;

        long elapsed = (System.currentTimeMillis() - roundStart) / 1000;
        long left = (roundLengthTicks / 20) - elapsed;

        if(left > 0 && left % 10 == 0) {
            for(Player p : players) p.sendMessage("§eTime left: §a" + left + "s");
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(
                Main.getInstance(),
                () -> tickTimeBroadcast(id),
                20L
        );
    }

    @Override
    public void onChat(Player p, PlayerChatEvent e) {
        if(!roundActive || p == builder || guessed.contains(p) || ended) return;

        String msg = e.getMessage().toLowerCase();
        if(!msg.equals(word.toLowerCase())) return;

        e.setCancelled(true);
        guessed.add(p);

        long time = (System.currentTimeMillis() - roundStart) / 1000;
        int score = Math.max(1, 30 - (int)time);
        points.put(p, points.get(p) + score);

        for(Player pl : players) pl.sendMessage("§a" + p.getName() + " guessed the word! §7(+" + score + ")");

        if(guessed.size() >= players.size() - 1) {
            roundActive = false;
            roundId++;
            for(Player pl : players) {
                pl.sendMessage("§eEveryone guessed the word!");
                pl.sendMessage("");
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(
                    Main.getInstance(),
                    this::startRound,
                    60L
            );
        }
    }

    private void endRound(int id) {
        if(!roundActive || id != roundId || ended) return;

        roundActive = false;
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
    public boolean canBreak(Player p, BlockBreakEvent e) {
        if(!roundActive || p != builder) return false;

        Block b = e.getBlock();
        int x = b.getX();
        int y = b.getY();
        int z = b.getZ();
        if(y < 91) return false;
        return x >= 293 && x <= 333 && z >= 591 && z <= 631;
    }

    @Override
    public boolean canPlace(Player p, BlockPlaceEvent e) {
        if(!roundActive || p != builder) return false;

        Block b = e.getBlock();
        int x = b.getX();
        int y = b.getY();
        int z = b.getZ();
        if(y < 91 || x < 293 || x > 333 || z < 591 || z > 631) return false;

        Material m = b.getType();
        return !(m == Material.TNT || m == Material.FIRE || m == Material.LAVA
                || m == Material.STATIONARY_LAVA || m == Material.WATER || m == Material.STATIONARY_WATER);
    }

    public void onMove(Player p) {
        if(!started) p.teleport(p.getLocation());
    }

    public void onMoveInQueue(Player p) {
        if(p.getLocation().getY() >= 105) return;

        Location from = p.getLocation();
        Location target = new Location(from.getWorld(), 343, from.getY(), 424);

        Vector horiz = target.toVector().subtract(from.toVector());
        horiz.setY(0);
        horiz.normalize().multiply(1.2);

        Vector v = new Vector(horiz.getX(), 1.3, horiz.getZ());
        p.setVelocity(v);
    }

    public void onQuit(Player p) {
        players.remove(p);
        points.remove(p);
        buildCount.remove(p);
        guessed.remove(p);

        if(players.size() < minPlayers()) {
            roundActive = false;
            roundId++;
        }
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
            sb.append(revealed[i] ? word.charAt(i) : '_').append(' ');
        }
        return sb.toString();
    }

    private void clearBuilderArea() {
        for(int x = 293; x <= 333; x++) {
            for(int y = 91; y <= world.getMaxHeight(); y++) {
                for(int z = 591; z <= 631; z++) {
                    Block b = world.getBlockAt(x, y, z);
                    b.setType(Material.AIR);
                }
            }
        }
    }
}
