package sylvessa.cb1337.commands;

import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sylvessa.cb1337.ChunkGenerators.FlatWorld;
import sylvessa.cb1337.Duels.DuelManager;
import sylvessa.cb1337.Main;
import sylvessa.cb1337.Minigames.MinigameManager;
import sylvessa.cb1337.Types.GameTypes.SavedState;
import sylvessa.cb1337.Types.PluginCommand;
import sylvessa.cb1337.UserConfig;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.command.Command;

public class CreativeCommand implements PluginCommand {

    private static final String WORLD_NAME = "creative";
    private static final Map<String, SavedState> saved = new HashMap<>();

    private World creativeWorld;

    public String name() {
        return "creative";
    }

    public String description() {
        return "Teleport to the creative map";
    }

    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return;

        Player p = (Player) sender;

        if (MinigameManager.get(p) != null || DuelManager.get(p) != null || MinigameManager.getQueued(p) != null) {
            p.sendMessage("§cYou cannot use this command right now!");
            return;
        }

        creativeWorld = Bukkit.getWorld(WORLD_NAME);

        if (creativeWorld == null) {
            WorldCreator wc = new WorldCreator(WORLD_NAME);
            wc.environment(World.Environment.NORMAL);
            wc.generator(new FlatWorld());
            creativeWorld = Bukkit.createWorld(wc);
        }

        if (p.getWorld().getName().equals(WORLD_NAME)) {
            returnFromCreative(p);
            return;
        }

        if (!saved.containsKey(p.getName())) {
            saved.put(
                    p.getName(),
                    new SavedState(
                            p.getLocation().clone(),
                            p.getInventory().getContents(),
                            p.getInventory().getArmorContents(),
                            p.getLevel(),
                            p.getExp()
                    )
            );
        }

        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.setGameMode(GameMode.CREATIVE);

        UserConfig uc = Main.getInstance().getUserConfig(p.getName());

        if (uc.get("creative.x", null) != null) {
            Location l = new Location(
                    creativeWorld,
                    uc.getDouble("creative.x", creativeWorld.getSpawnLocation().getX()),
                    uc.getDouble("creative.y", creativeWorld.getSpawnLocation().getY()),
                    uc.getDouble("creative.z", creativeWorld.getSpawnLocation().getZ()),
                    uc.getFloat("creative.yaw", 0f),
                    uc.getFloat("creative.pitch", 0f)
            );

            p.teleport(l);
        } else {
            p.teleport(creativeWorld.getSpawnLocation());
        }

        p.sendMessage(ChatColor.GREEN + "Teleported to the creative world. Run /creative again to return.");
    }

    public static void returnFromCreative(Player p) {
        if (!p.getWorld().getName().equals(WORLD_NAME)) return;

        SavedState s = saved.remove(p.getName());
        if (s == null) return;

        UserConfig uc = Main.getInstance().getUserConfig(p.getName());
        Location l = p.getLocation();

        uc.set("creative.x", l.getX());
        uc.set("creative.y", l.getY());
        uc.set("creative.z", l.getZ());
        uc.set("creative.yaw", l.getYaw());
        uc.set("creative.pitch", l.getPitch());
        uc.save();

        p.getInventory().clear();
        p.getInventory().setContents(s.inv);
        p.getInventory().setArmorContents(s.armor);
        p.setLevel(s.level);
        p.setExp(s.experience);
        p.setFallDistance(0f);

        p.teleport(s.loc);
        p.setGameMode(GameMode.SURVIVAL);

        p.sendMessage(ChatColor.YELLOW + "Welcome back");
    }
}
