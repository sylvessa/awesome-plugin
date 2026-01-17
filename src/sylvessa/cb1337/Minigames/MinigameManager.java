package sylvessa.cb1337.Minigames;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sylvessa.cb1337.ChunkGenerators.Void;
import sylvessa.cb1337.Main;
import sylvessa.cb1337.Util.CustomWorldLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MinigameManager {
    private static final Map<MinigameType, Minigame> queued = new HashMap<>();
    private static final Map<String, Minigame> active = new HashMap<>();
    private static final Map<String, SavedState> saved = new HashMap<>();

    public static void queue(Player p, MinigameType type) {
        Minigame g = queued.get(type);

        if(g == null) {
            g = type.create();
            createLobbyWorld(g);
            queued.put(type, g);
        }

        if(g.players.contains(p)) return;
        if(g.players.size() >= g.maxPlayers()) {
            p.sendMessage("§cQueue is full");
            return;
        }

        saveState(p);
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);

        g.players.add(p);
        p.teleport(g.world.getSpawnLocation());
        p.sendMessage("§aJoined queue");

        manageLobbyCountdown(g);
    }

    private static void manageLobbyCountdown(Minigame g) {
        if(g.started || g.countingDown) return;

        if(g.players.size() < g.minPlayers()) {
            if(g.countdownTask == -1) {
                g.countdownTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                        Main.getInstance(),
                        () -> {
                            if(g.players.size() >= g.minPlayers()) {
                                Bukkit.getScheduler().cancelTask(g.countdownTask);
                                g.countdownTask = -1;
                                manageLobbyCountdown(g);
                                return;
                            }

                            for(Player p : g.players) {
                                p.sendMessage("§eWaiting for players (" + g.minPlayers() + " players needed)...");
                            }
                        },
                        0L,
                        200L
                );
            }
            return;
        }

        startCountdown(g);
    }

    private static void startCountdown(Minigame g) {
        g.countingDown = true;
        final int[] time = {20};

        if(g.countdownTask != -1) {
            Bukkit.getScheduler().cancelTask(g.countdownTask);
        }

        g.countdownTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                Main.getInstance(),
                () -> {
                    if(g.players.size() < g.minPlayers()) {
                        cancelCountdown(g);
                        for(Player p : g.players) {
                            p.sendMessage("§cNot enough players, countdown cancelled");
                        }

                        g.countingDown = false;
                        manageLobbyCountdown(g);
                        return;
                    }

                    if(time[0] == 0) {
                        cancelCountdown(g);
                        startGame(g);
                        return;
                    }

                    for(Player p : g.players) {
                        p.sendMessage("§eStarting in " + time[0]);
                    }
                    time[0]--;
                },
                0L,
                20L
        );
    }

    private static void startGame(Minigame g) {
        World lobbyWorld = g.world;

        createArenaWorld(g);
        g.teleportToArena();
        g.started = true;
        g.startGame();

        for(Player p : g.players) {
            active.put(p.getName(), g);
        }

        unloadWorld(lobbyWorld);
    }

    public static void remove(Player p) {
        Minigame g = active.remove(p.getName());
        if(g != null) {
            g.players.remove(p);
            restoreState(p);

            if(g.players.size() < g.minPlayers()) {
                end(g);
            }
            return;
        }

        for(Minigame mg : queued.values()) {
            if(mg.players.remove(p)) {
                restoreState(p);
                cancelCountdown(mg);
                manageLobbyCountdown(mg);
                return;
            }
        }
    }

    public static Minigame get(Player p) {
        return active.get(p.getName());
    }

    public static void end(Minigame g) {
        for(Player p : new ArrayList<>(g.players)) {
            restoreState(p);
            active.remove(p.getName());
        }

        g.endGame();
        unloadWorld(g.world);
        queued.remove(g.getType());
    }

    private static void createLobbyWorld(Minigame g) {
        try {
            String name = "mg_lobby_" + g.getType().name().toLowerCase() + "_" + new Random().nextInt(100000);
            CustomWorldLoader.copyArenaToServerJar(g.lobbyTemplate(), name);
            g.world = Bukkit.createWorld(name, World.Environment.NORMAL, new Void());
            g.world.setStorm(false);
        } catch(Exception ignored) {}
    }

    private static void createArenaWorld(Minigame g) {
        try {
            String name = "mg_game_" + g.getType().name().toLowerCase() + "_" + new Random().nextInt(100000);
            CustomWorldLoader.copyArenaToServerJar(g.arenaTemplate(), name);
            g.world = Bukkit.createWorld(name, World.Environment.NORMAL, new Void());
            g.world.setStorm(false);
        } catch(Exception ignored) {}
    }

    private static void cancelCountdown(Minigame g) {
        if(g.countdownTask != -1) {
            Bukkit.getScheduler().cancelTask(g.countdownTask);
            g.countdownTask = -1;
        }
        g.countingDown = false;
    }

    private static void unloadWorld(World w) {
        File f = new File(".", w.getName());
        Bukkit.unloadWorld(w, true);
        delete(f);
    }

    private static void delete(File f) {
        if(f.isDirectory()) for(File c : f.listFiles()) delete(c);
        f.delete();
    }

    private static void saveState(Player p) {
        saved.put(p.getName(), new SavedState(
                p.getLocation().clone(),
                p.getInventory().getContents(),
                p.getInventory().getArmorContents()
        ));
    }

    private static void restoreState(Player p) {
        SavedState s = saved.remove(p.getName());
        if(s == null) return;

        p.teleport(s.loc);
        p.getInventory().setContents(s.inv);
        p.getInventory().setArmorContents(s.armor);
        p.setFallDistance(0f);
    }

    private static class SavedState {
        Location loc;
        ItemStack[] inv;
        ItemStack[] armor;

        SavedState(Location l, ItemStack[] i, ItemStack[] a) {
            loc = l;
            inv = i;
            armor = a;
        }
    }
}
