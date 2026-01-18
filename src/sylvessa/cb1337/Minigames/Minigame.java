package sylvessa.cb1337.Minigames;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;

public abstract class Minigame {

    protected final List<Player> players;
    protected World world;
    protected boolean started;
    protected boolean finished;

    protected int countdownTask = -1;
    protected boolean countingDown;

    public Minigame(List<Player> players) {
        this.players = players;
    }

    public abstract MinigameType getType();

    public abstract int minPlayers();
    public abstract int maxPlayers();

    public abstract String lobbyTemplate();
    public abstract String arenaTemplate();

    public abstract void teleportToArena();
    public abstract void startGame();
    public abstract void endGame();
    public void onDamage(Player p, EntityDamageEvent e) {};

    public abstract void onMove(Player p);
    public abstract void onQuit(Player p);

    public boolean canBreak(Player p, BlockBreakEvent e) { return false; }
    public boolean canPlace(Player p, BlockPlaceEvent e) { return false; }

    public boolean isPlaying(Player p) {
        return players.contains(p);
    }
    public boolean hasStarted() {return started;}
}

