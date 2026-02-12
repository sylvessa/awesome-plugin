package sylvessa.spigot.Minigames;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.util.Vector;

import java.util.List;

public abstract class Minigame {

    protected final List<Player> players;
    protected World world;
    protected boolean started;
    protected boolean ended;

    protected int countdownTask = -1;
    protected boolean countingDown;

    protected final String lobbyTemplate;
    protected final String arenaTemplate;
    protected final Vector lobbySpawn;
    protected final long lobbyTime;
    protected final long arenaTime;
    protected final String lobbyJoinDesc;


    public Minigame(List<Player> players, String lobbyTemplate, String arenaTemplate, Vector lobbySpawn,
                    long lobbyTime, long arenaTime, String lobbyJoinDesc) {
        this.players = players;
        this.lobbyTemplate = lobbyTemplate;
        this.arenaTemplate = arenaTemplate;
        this.lobbySpawn = lobbySpawn;
        this.lobbyTime = lobbyTime;
        this.arenaTime = arenaTime;
        this.lobbyJoinDesc = lobbyJoinDesc;

    }

    public abstract MinigameType getType();

    public abstract int minPlayers();
    public abstract int maxPlayers();

    public abstract void teleportToArena();
    public abstract void startGame();
    public abstract void endGame();
    public void onDamage(Player p, EntityDamageEvent e) {};

    public abstract void onMove(Player p);
    public abstract void onMoveInQueue(Player p);
    public abstract void onQuit(Player p);

    public boolean canBreak(Player p, BlockBreakEvent e) { return false; }
    public boolean canPlace(Player p, BlockPlaceEvent e) { return false; }
    public void onChat(Player p, AsyncPlayerChatEvent e) {}
    public void onDeath(Player p, EntityDeathEvent e) {}

    public boolean isPlaying(Player p) {
        return players.contains(p);
    }
    public boolean hasStarted() {return started;}
}

