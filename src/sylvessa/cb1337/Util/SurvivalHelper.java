package sylvessa.cb1337.Util;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import sylvessa.cb1337.ChunkGenerators.Void;
import sylvessa.cb1337.Main;

import java.io.*;
import java.util.*;

import static sylvessa.cb1337.Util.Helpers.freezeWorldTime;

public final class SurvivalHelper {
    private static final Set<String> SURVIVAL_WORLDS = new HashSet<>(Arrays.asList(
            "world",
            "world_nether",
            "world_the_end"
    ));
    private static final Set<UUID> IGNORE_WORLD_CHANGE = new HashSet<>();

    private static final Vector lobbySpawn = new Vector(-545, 81, 218);

    private static File dataDir() {
        File dir = new File(Main.getInstance().getDataFolder(), "playerdata-survival");
        dir.mkdirs();
        return dir;
    }

    public static boolean isSurvivalWorld(World w) {
        return SURVIVAL_WORLDS.contains(w.getName());
    }

    public static boolean hasSave(Player p) {
        return file(p).exists();
    }

    public static void importFromVanillaIfNeeded(Player p) {
        if(hasSave(p)) return;
        if(!isSurvivalWorld(p.getWorld())) return;
        saveSurvival(p);
    }

    public static void handleJoin(Player p) {
        if(isSurvivalWorld(p.getWorld())) {
            importFromVanillaIfNeeded(p);
        }

        clearPlayer(p);
        World lobby = Bukkit.getWorld("lobby");
        if(lobby != null) {
            p.teleport(new Location(lobby, lobbySpawn.getX(), lobbySpawn.getY(), lobbySpawn.getZ()));
            freezeWorldTime(lobby, 6000);
        } else {
            try {
                CustomWorldLoader.copyArenaToServerJar("lobby", "lobby");
                World NewLobby = Bukkit.createWorld("lobby", World.Environment.NORMAL, new Void());
                p.teleport(new Location(NewLobby, lobbySpawn.getX(), lobbySpawn.getY(), lobbySpawn.getZ()));
                freezeWorldTime(NewLobby, 6000);
            } catch(Exception ignored) {}
        }
        p.setGameMode(GameMode.SURVIVAL);
    }

    public static void handleQuit(Player p) {
        if(isSurvivalWorld(p.getWorld())) {
            saveSurvival(p);
        }
    }

    public static void handleWorldChange(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();

        if(IGNORE_WORLD_CHANGE.contains(p.getUniqueId())) {
            return;
        }

        World from = e.getFrom();
        World to = p.getWorld();

        if(isSurvivalWorld(from) && !isSurvivalWorld(to)) {
            saveSurvival(p);
            clearPlayer(p);
            p.setGameMode(GameMode.SURVIVAL);
            return;
        }

        if(!isSurvivalWorld(from) && isSurvivalWorld(to)) {
            loadSurvival(p);
            p.setGameMode(GameMode.SURVIVAL);
        }
    }

    public static void enterSurvival(Player p) {
        loadSurvival(p);
        p.setGameMode(GameMode.SURVIVAL);
    }

    public static void leaveSurvival(Player p) {
        saveSurvival(p);
        clearPlayer(p);
        p.setGameMode(GameMode.SURVIVAL);
    }

    private static List<Map<String, Object>> serialize(ItemStack[] items) {
        List<Map<String, Object>> list = new ArrayList<>();
        for(ItemStack it : items) {
            if(it == null) {
                list.add(null);
            } else {
                Map<String, Object> map = new HashMap<>(it.serialize());
                Object type = map.get("type");
                if(type instanceof Material) {
                    map.put("type", ((Material) type).name());
                }
                list.add(map);
            }
        }
        return list;
    }

    private static ItemStack[] deserialize(List<Map<String, Object>> list) {
        ItemStack[] items = new ItemStack[list.size()];
        for(int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            if(map == null) {
                items[i] = null;
            } else {
                items[i] = ItemStack.deserialize(map);
            }
        }
        return items;
    }




    private static void clearPlayer(Player p) {
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.setHealth(20);
        p.setFoodLevel(20);
        p.setExperience(0);
        p.setLevel(0);
    }

    private static File file(Player p) {
        return new File(dataDir(), p.getUniqueId().toString() + ".dat");
    }

    public static void saveSurvival(Player p) {
        if(!isSurvivalWorld(p.getWorld())) return;

        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file(p)));

            out.writeObject(serialize(p.getInventory().getContents()));
            out.writeObject(serialize(p.getInventory().getArmorContents()));
            out.writeInt(p.getHealth());
            out.writeInt(p.getFoodLevel());
            out.writeInt(p.getExperience());
            out.writeInt(p.getLevel());

            Location l = p.getLocation();
            out.writeUTF(l.getWorld().getName());
            out.writeDouble(l.getX());
            out.writeDouble(l.getY());
            out.writeDouble(l.getZ());
            out.writeFloat(l.getYaw());
            out.writeFloat(l.getPitch());

            out.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unchecked")
    public static void loadSurvival(Player p) {
        File f = file(p);
        if(!f.exists()) return;

        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));

            p.getInventory().setContents(
                    deserialize((List<Map<String, Object>>) in.readObject())
            );
            p.getInventory().setArmorContents(
                    deserialize((List<Map<String, Object>>) in.readObject())
            );

            p.setHealth(in.readInt());
            p.setFoodLevel(in.readInt());
            p.setExperience(in.readInt());
            p.setLevel(in.readInt());

            String world = in.readUTF();
            double x = in.readDouble();
            double y = in.readDouble();
            double z = in.readDouble();
            float yaw = in.readFloat();
            float pitch = in.readFloat();

            World w = Bukkit.getWorld(world);
            if(w != null) {
                p.teleport(new Location(w, x, y, z, yaw, pitch));
            }

            in.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    public static void ignoreNextWorldChange(Player p) {
        IGNORE_WORLD_CHANGE.add(p.getUniqueId());
        Bukkit.getScheduler().scheduleSyncDelayedTask(
                Main.getInstance(),
                () -> IGNORE_WORLD_CHANGE.remove(p.getUniqueId()),
                1L
        );
    }

}
