package sylvessa.plugin.Duels;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import sylvessa.plugin.Duels.Modes.BridgeDuel;
import sylvessa.plugin.Duels.Modes.SpleefDuel;
import sylvessa.plugin.Duels.Modes.SumoDuel;
import sylvessa.plugin.Main;

public class DuelManager {

    private static final Map<String, PendingDuel> pending = new HashMap<>();
    private static final Map<String, DuelGame> active = new HashMap<>();
    private static final Map<String, SavedState> saved = new HashMap<>();

    public static void challenge(Player from, Player to, DuelType type) {
        pending.put(to.getName(), new PendingDuel(from.getName(), type));
    }

    public static void accept(Player p) {
        PendingDuel pd = pending.remove(p.getName());
        if(pd == null) return;

        Player challenger = p.getServer().getPlayer(pd.challenger);
        if(challenger == null) return;

        saveState(challenger);
        saveState(p);

        challenger.getInventory().clear();
        challenger.getInventory().setArmorContents(null);

        p.getInventory().clear();
        p.getInventory().setArmorContents(null);

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
        restoreState(g.p1);
        restoreState(g.p2);

        active.remove(g.p1.getName());
        active.remove(g.p2.getName());

        Bukkit.unloadWorld(g.world, true);
        File worldFolder = new File(".", g.world.getName());
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> deleteWorld(worldFolder), 100L);
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
        if(type == DuelType.SPLEEF) return new SpleefDuel(p1, p2);
        if(type == DuelType.SUMO) return new SumoDuel(p1, p2);
        if(type == DuelType.BRIDGE) return new BridgeDuel(p1, p2);

        return null;
    }

    private static void saveState(Player p) {
        saved.put(
                p.getName(),
                new SavedState(
                        p.getLocation().clone(),
                        p.getInventory().getContents(),
                        p.getInventory().getArmorContents()
                )
        );
    }

    private static void restoreState(Player p) {
        SavedState s = saved.remove(p.getName());
        if(s == null) return;

        p.teleport(s.loc);
        p.getInventory().setContents(s.inv);
        p.getInventory().setArmorContents(s.armor);
        p.setFallDistance(0f);
    }

    private static void deleteWorld(File f) {
        if(f.isDirectory()) {
            for(File c : f.listFiles()) deleteWorld(c);
        }
        f.delete();
    }

    private static class PendingDuel {
        String challenger;
        DuelType type;

        PendingDuel(String c, DuelType t) {
            challenger = c;
            type = t;
        }
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
