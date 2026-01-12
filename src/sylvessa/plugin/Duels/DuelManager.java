package sylvessa.plugin.Duels;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import sylvessa.plugin.Duels.Modes.SpleefDuel;

public class DuelManager {

    private static final Map<String, PendingDuel> pending = new HashMap<>();
    private static final Map<String, DuelGame> active = new HashMap<>();

    public static void challenge(Player from, Player to, DuelType type) {
        pending.put(to.getName(), new PendingDuel(from.getName(), type));
    }

    public static void accept(Player p) {
        PendingDuel pd = pending.remove(p.getName());
        if(pd == null) return;

        Player challenger = p.getServer().getPlayer(pd.challenger);
        if(challenger == null) return;

        DuelGame game = createGame(pd.type, challenger, p);
        active.put(challenger.getName(), game);
        active.put(p.getName(), game);
        game.start();
    }

    public static void deny(Player p) {
        pending.remove(p.getName());
    }

    public static DuelGame get(Player p) {
        return active.get(p.getName());
    }

    public static void end(DuelGame g) {
        active.remove(g.p1.getName());
        active.remove(g.p2.getName());
    }

    public static Player getChallenger(Player p) {
        PendingDuel pd = pending.get(p.getName());
        if(pd == null) return null;
        return p.getServer().getPlayer(pd.challenger);
    }

    public static boolean isChallenged(Player from, Player to) {
        PendingDuel pd = pending.get(to.getName());
        if(pd == null) return false;
        return pd.challenger.equals(from.getName());
    }

    private static DuelGame createGame(DuelType type, Player p1, Player p2) {
        if(type == DuelType.SPLEEF) {
            return new SpleefDuel(p1, p2);
        }
        return null;
    }

    private static class PendingDuel {
        String challenger;
        DuelType type;

        PendingDuel(String c, DuelType t) {
            challenger = c;
            type = t;
        }
    }
}
