package sylvessa.plugin.commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

@SuppressWarnings("unused")
public class RTPCommand implements PluginCommand {
    public String name() {
        return "rtp";
    }

    public String description() {
        return "Teleport to a random location";
    }

    public boolean hidden() {
        return true;
    }

    private static final int RADIUS = 5000;
    private static final int MAX_TRIES = 20;
    private final Random random = new Random();

    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("§cUnfortunately, this command causes insane lag, so this is disabled for now.");
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return;
        }

        Player p = (Player) sender;
        World w = p.getWorld();

        for (int attempt = 0; attempt < MAX_TRIES; attempt++) {
            int x = randomCoord();
            int z = randomCoord();

            int y = findGround(w, x, z);
            if (y == -1) continue;

            Location loc = new Location(w, x + 0.5, y + 1, z + 0.5);
            p.teleport(loc);
            p.sendMessage("§aTeleported to a random location.");
            return;
        }

        p.sendMessage("§cFailed to find a safe location. Try again.");
    }

    private int randomCoord() {
        int v = random.nextInt(RADIUS);
        return random.nextBoolean() ? v : -v;
    }

    private int findGround(World w, int x, int z) {
        int maxY = w.getMaxHeight() - 1;

        for (int y = maxY; y > 1; y--) {
            Material m = w.getBlockAt(x, y, z).getType();
            Material above = w.getBlockAt(x, y + 1, z).getType();

            if (m != Material.AIR && m != Material.LAVA && m != Material.WATER) {
                if (above == Material.AIR) {
                    return y;
                }
            }
        }
        return -1;
    }
}