package sylvessa.cb1337.commands;

import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import sylvessa.cb1337.Duels.DuelManager;
import sylvessa.cb1337.Minigames.MinigameManager;
import sylvessa.cb1337.Types.PluginCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CreativeCommand implements PluginCommand {

    private static final String WORLD_NAME = "creative";

    private static final Map<Player, ItemStack[]> savedInventories = new HashMap<>();
    private static final Map<Player, ItemStack[]> savedArmor = new HashMap<>();
    private static final Map<Player, Location> savedLocations = new HashMap<>();

    public String name() {
        return "creative";
    }

    public String description() {
        return "Teleport to the creative map";
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;

        Player p = (Player) sender;

        if (MinigameManager.get(p) != null || DuelManager.get(p) != null || MinigameManager.getQueued(p) != null) {
            p.sendMessage("§cYou cannot use this command right now!");
            return;
        }

        World creativeWorld = Bukkit.getWorld(WORLD_NAME);
        if (creativeWorld == null) {
            WorldCreator wc = new WorldCreator(WORLD_NAME);
            wc.environment(World.Environment.NORMAL);
            wc.generator(new org.bukkit.generator.ChunkGenerator() {
                @Override
                public byte[] generate(World world, Random random, int cx, int cz) {
                    byte[] chunk = new byte[32768]; // 16*128*16
                    for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                            chunk[x * 128 + 0 + z * 128 * 16] = (byte) Material.BEDROCK.getId();
                            for (int y = 1; y <= 49; y++) {
                                chunk[x * 128 + y + z * 128 * 16] = (byte) Material.DIRT.getId();
                            }
                            chunk[x * 128 + 50 + z * 128 * 16] = (byte) Material.GRASS.getId();
                        }
                    }
                    return chunk;
                }
            });
            creativeWorld = Bukkit.createWorld(wc);
        }

        if (p.getWorld().getName().equals(WORLD_NAME)) {
            returnFromCreative(p);
            return;
        }

        if (!savedInventories.containsKey(p)) {
            savedInventories.put(p, p.getInventory().getContents());
            savedArmor.put(p, p.getInventory().getArmorContents());
            savedLocations.put(p, p.getLocation());
        }

        p.getInventory().clear();
        p.getInventory().setArmorContents(new ItemStack[4]);
        p.setGameMode(GameMode.CREATIVE);

        p.teleport(creativeWorld.getSpawnLocation());
        p.sendMessage(ChatColor.GREEN + "Teleported to the creative world. Run /creative again to return to overworld.");
    }

    public static void returnFromCreative(Player p) {
        if (!p.getWorld().getName().equals(WORLD_NAME)) return;
        if (!savedInventories.containsKey(p)) return;

        p.getInventory().clear();
        p.getInventory().setContents(savedInventories.remove(p));
        p.getInventory().setArmorContents(savedArmor.remove(p));

        Location loc = savedLocations.remove(p);
        if (loc != null) p.teleport(loc);

        p.setGameMode(GameMode.SURVIVAL);
        p.sendMessage(ChatColor.YELLOW + "Welcome back");
    }
}
