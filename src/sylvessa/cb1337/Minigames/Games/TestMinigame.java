package sylvessa.cb1337.Minigames.Games;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import sylvessa.cb1337.Log;
import sylvessa.cb1337.Main;
import sylvessa.cb1337.Minigames.Minigame;
import sylvessa.cb1337.Minigames.MinigameManager;
import sylvessa.cb1337.Minigames.MinigameType;

import java.util.List;

public class TestMinigame extends Minigame {

    public TestMinigame(List<Player> players) {
        super(players);
    }

    public MinigameType getType() {
        return MinigameType.TEST;
    }

    public int minPlayers() { return 2; }
    public int maxPlayers() { return 2; }

    public String lobbyTemplate() { return "test_lobby"; }
    public String arenaTemplate() { return "test_arena"; }

    public void teleportToArena() {
        int i = 0;
        for(Player p : players) {
            Log.info("TPING TO " + world.getName());
            p.teleport(world.getSpawnLocation().clone().add(i * 2, 0, 0));
            i++;
        }
    }

    public void startGame() {
        for(Player p : players) p.sendMessage("§aTest game started");
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> MinigameManager.end(this), 400L);
    }

    public void endGame() {
        for(Player p : players) p.sendMessage("§cTest game ended");
    }

    public void onMove(Player p) {
        if(!started) p.teleport(p.getLocation());
    }

    public void onQuit(Player p) {
        MinigameManager.remove(p);
    }
}

