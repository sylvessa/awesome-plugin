package sylvessa.spigot.Duels;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerPickupItemEvent;

@SuppressWarnings("EmptyMethod")
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
    public abstract void onDamage(Player p, EntityDamageEvent e);
    public abstract void onQuit(Player p);
    public abstract boolean canBreak(Player p, BlockBreakEvent e);
    public abstract boolean canPlace(Player p, BlockPlaceEvent e);
    public void onBowShoot(Player p, EntityShootBowEvent e) {}
    public void onFoodLevelChange(Player p, FoodLevelChangeEvent event) {}
    public void onDeath(Player p, PlayerDeathEvent event) {}
    public void onPlayerPickupArrow(Player p, PlayerPickupItemEvent event) {}


    public boolean isParticipant(Player p) {
        return p == p1 || p == p2;
    }

    public Player getOpponent(Player p) {
        return p == p1 ? p2 : p1;
    }
}