package sylvessa.cb1337.Minigames;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sylvessa.cb1337.ChunkGenerators.Void;
import sylvessa.cb1337.Main;
import sylvessa.cb1337.Types.GameTypes.SavedState;
import sylvessa.cb1337.Util.CustomWorldLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static sylvessa.cb1337.Util.Helpers.freezeWorldTime;

public class MinigameManager {
    private static final Map<MinigameType, ArrayList<Minigame>> queued = new HashMap<>();
    private static final Map<String, Minigame> active = new HashMap<>();
    private static final Map<String, SavedState> saved = new HashMap<>();

    public static void queue(Player p, MinigameType type) {
        ArrayList<Minigame> list = queued.computeIfAbsent(type, k -> new ArrayList<>());
        Minigame g = null;

        for(Minigame mg : list) {
            if(!mg.started && mg.players.size() < mg.maxPlayers()) {
                g = mg;
                break;
            }
        }

        if(g == null) {
            g = type.create();
            createLobbyWorld(g);
            list.add(g);
        }

        if(g.players.contains(p)) return;

        saveState(p);
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);

        g.players.add(p);
        //p.teleport(g.world.getSpawnLocation());
        p.teleport(new Location(g.world, g.lobbySpawn.getX(), g.lobbySpawn.getY(), g.lobbySpawn.getZ()));

        p.setHealth(20);
        p.setSaturation(20);
        p.setFoodLevel(20);

        p.sendMessage("§aJoined queue. Run /queue to leave queue." );

        for(String line : g.lobbyJoinDesc.split("\n")) {
            p.sendMessage(line);
        }
        p.sendMessage("");

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
        final int[] time = {30};

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
                        manageLobbyCountdown(g);
                        return;
                    }

                    if(time[0] == 0) {
                        cancelCountdown(g);
                        startGame(g);
                        return;
                    }

                    if(time[0] <= 5 || time[0] % 10 == 0) {
                        for(Player p : g.players) {
                            p.sendMessage("§eBeginning in " + time[0] + "...");
                        }
                    }

                    time[0]--;
                },
                0L,
                20L
        );
    }

    private static void startGame(Minigame g) {
        World lobbyWorld = g.world;

        ArrayList<Minigame> list = queued.get(g.getType());
        if(list != null) {
            list.remove(g);
            if(list.isEmpty()) queued.remove(g.getType());
        }

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

        for(ArrayList<Minigame> list : queued.values()) {
            for(Minigame mg : new ArrayList<>(list)) {
                if(mg.players.remove(p)) {
                    restoreState(p);
                    cancelCountdown(mg);
                    manageLobbyCountdown(mg);

                    // if queue is now empty and game hasnt started, delete the lobby world
                    if(mg.players.isEmpty() && !mg.started) {
                        list.remove(mg);
                        unloadWorld(mg.world);
                    }
                    return;
                }
            }
        }
    }


    public static Minigame get(Player p) {
        return active.get(p.getName());
    }

    public static Minigame getQueued(Player p) {
        for(ArrayList<Minigame> list : queued.values()) {
            for(Minigame g : list) {
                if(g.players.contains(p)) return g;
            }
        }
        return null;
    }

    public static boolean isQueued(Player p) {
        return getQueued(p) != null;
    }



    public static void end(Minigame g) {
        if(g.ended) return;
        g.ended = true;

        for(Player p : new ArrayList<>(g.players)) {
            restoreState(p);
            active.remove(p.getName());
        }

        g.endGame();
        unloadWorld(g.world);
    }

    private static void createLobbyWorld(Minigame g) {
        try {
            String name = "mg_lobby_" + g.getType().name().toLowerCase() + "_" + new Random().nextInt(100000);
            CustomWorldLoader.copyArenaToServerJar(g.lobbyTemplate, name);
            g.world = Bukkit.createWorld(
                    new WorldCreator(name)
                            .environment(World.Environment.NORMAL)
                            .generator(new Void()));

            freezeWorldTime(g.world, g.lobbyTime);
        } catch(Exception ignored) {}
    }


    private static void createArenaWorld(Minigame g) {
        try {
            String name = "mg_game_" + g.getType().name().toLowerCase() + "_" + new Random().nextInt(100000);
            CustomWorldLoader.copyArenaToServerJar(g.arenaTemplate, name);
            g.world = Bukkit.createWorld(
                    new WorldCreator(name)
                            .environment(World.Environment.NORMAL)
                            .generator(new Void()));

            freezeWorldTime(g.world, g.arenaTime);
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
        Bukkit.getScheduler().scheduleSyncDelayedTask(
                Main.getInstance(),
                () -> delete(f),
                100L
        );
    }

    private static void delete(File f) {
        if(f.isDirectory()) for(File c : f.listFiles()) delete(c);
        f.delete();
    }

    private static void saveState(Player p) {
        saved.put(p.getName(), new SavedState(
                p.getLocation().clone(),
                p.getInventory().getContents(),
                p.getInventory().getArmorContents(),
                p.getLevel(),
                p.getExp()
        ));
    }

    private static void restoreState(Player p) {
        SavedState s = saved.remove(p.getName());
        if(s == null) return;

        p.teleport(s.loc);
        p.getInventory().setContents(s.inv);
        p.setGameMode(GameMode.SURVIVAL);
        p.getInventory().setArmorContents(s.armor);
        p.setExp(s.experience);
        p.setLevel(s.level);
        p.setFallDistance(0f);
    }


}
