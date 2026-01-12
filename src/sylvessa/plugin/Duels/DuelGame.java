package sylvessa.plugin.Duels;

import org.bukkit.World;
import org.bukkit.entity.Player;

public abstract class DuelGame {

    protected final Player p1;
    protected final Player p2;
    protected World world;
    protected boolean started;
    protected boolean finished;

    public DuelGame(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public abstract DuelType getType();
    public abstract void start();
    public abstract void onMove(Player p);
    public abstract void onDamage(Player p);
    public abstract void onQuit(Player p);
    public abstract boolean canBreak(Player p);
    public abstract boolean canPlace(Player p);

    public boolean isParticipant(Player p) {
        return p == p1 || p == p2;
    }

    public Player getOpponent(Player p) {
        return p == p1 ? p2 : p1;
    }
}
