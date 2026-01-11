package sylvessa.plugin.Spleef;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;

public class SpleefManager {

    private static Map<String,String> pending = new HashMap<String,String>();
    private static Map<String,SpleefGame> active = new HashMap<String,SpleefGame>();

    public static void challenge(Player from, Player to) {
        pending.put(to.getName(), from.getName());
        //to.sendMessage("§b" + from.getName() + " challenged you to spleef. /spleef accept or /spleef deny");
    }

    public static void accept(Player p) {
        String c = pending.remove(p.getName());
        if(c == null) return;

        Player challenger = p.getServer().getPlayer(c);
        if(challenger == null) return;

        SpleefGame game = new SpleefGame(challenger, p);
        active.put(challenger.getName(), game);
        active.put(p.getName(), game);
    }

    public static void deny(Player p) {
        String c = pending.remove(p.getName());
    }

    public static SpleefGame get(Player p) {
        return active.get(p.getName());
    }

    public static void end(SpleefGame g) {
        active.remove(g.getP1().getName());
        active.remove(g.getP2().getName());
    }

    public static boolean isChallenged(Player from, Player to) {
        return pending.containsKey(to.getName()) && pending.get(to.getName()).equals(from.getName());
    }

    public static Player getChallenger(Player p) {
        String challengerName = pending.get(p.getName());
        if(challengerName == null) return null;
        return p.getServer().getPlayer(challengerName);
    }
}
