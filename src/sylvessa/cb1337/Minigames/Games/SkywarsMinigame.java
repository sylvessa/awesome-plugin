package sylvessa.cb1337.Minigames.Games;

import net.minecraft.server.v1_4_R1.Packet9Respawn;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_4_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import sylvessa.cb1337.Main;
import sylvessa.cb1337.Minigames.Minigame;
import sylvessa.cb1337.Minigames.MinigameManager;
import sylvessa.cb1337.Minigames.MinigameType;

import java.util.*;

public class SkywarsMinigame extends Minigame {

    private final List<Location> spawns = new ArrayList<>();
    private final List<Location> spawnChests = new ArrayList<>();
    private final List<Location> midChests = new ArrayList<>();
    private final Map<Player, Long> fallImmunity = new HashMap<>();
    private final Map<Player, Location> playerNoteBlocks = new HashMap<>();
    private final Random rand = new Random();

    private boolean refill1 = false;
    private boolean refill2 = false;

    private boolean canDoStuff = false;

    public SkywarsMinigame(List<Player> players) {
        super(players,
                "skywars_ancient",
                "skywars_ancient",
                new Vector(165, 105, 420),
                6000,
                6000,
                "§dWelcome to Skywars!\n" +
                        "§6Start on your own island and grab loot\n" +
                        "§aBuild bridges and explore other islands\n" +
                        "§cFight players and be the last one standing"
        );
    }

    public MinigameType getType() { return MinigameType.SKYWARS; }
    public int minPlayers() { return 2; }
    public int maxPlayers() { return 12; }

    public void teleportToArena() {
        int[][] spawnsRaw = {
                {114,72,421},{120,72,446},{140,72,466},{167,72,472},{192,72,466},{212,72,446},
                {218,72,419},{212,72,394},{192,72,374},{165,72,368},{140,72,374},{120,72,394}
        };
        for(int[] s : spawnsRaw) spawns.add(new Location(world, s[0], s[1], s[2]));

        int[][] spawnChestsRaw = {
                {108,64,420},{112,63,424},{111,57,423},{114,64,393},{118,63,397},{117,57,396},
                {139,64,368},{143,63,372},{142,57,371},{162,63,366},{166,64,362},{163,57,365},
                {193,64,368},{189,63,372},{190,57,371},{218,64,393},{214,63,397},{215,57,396},
                {224,64,420},{220,63,416},{221,57,417},{218,64,447},{214,63,443},{215,57,444},
                {193,64,472},{189,63,468},{190,57,469},{166,64,478},{170,63,474},{169,57,574},
                {139,64,472},{143,63,468},{142,57,469},{114,64,447},{118,63,443},{117,57,444},
                {169,57,475}
        };
        for(int[] c : spawnChestsRaw) spawnChests.add(new Location(world, c[0], c[1] - 1, c[2]));

        int[][] midChestsRaw = {
                {182,64,404},{182,64,436},{150,64,436},{150,64,404},{163,68,423},{169,68,417}
        };
        for(int[] c : midChestsRaw) midChests.add(new Location(world, c[0], c[1] - 1, c[2]));

        Collections.shuffle(spawns, rand);

        int i = 0;
        for(Player p : players) {
            Location spawn = spawns.get(i);
            p.teleport(spawn);
            p.setGameMode(GameMode.SURVIVAL);
            i++;
        }
    }

    public void startGame() {
        fillChests(spawnChests, true);
        fillChests(midChests, false);
        spawnNoteBlocksOnce();

        final int[] taskId = new int[1];
        taskId[0] = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                Main.getInstance(),
                new Runnable() {
                    int time = 10;
                    public void run() {
                        if(time <= 0) {

                            for(Player p : players) {
                                fallImmunity.put(p, System.currentTimeMillis());
                            }

                            canDoStuff = true;

                            removeSpawnCages();
                            for(Player p : players) {
                                p.sendMessage("§aGo!");
                                p.playNote(playerNoteBlocks.get(p), Instrument.PIANO, new Note((byte)1, Note.Tone.C, false));
                            }

                            startRefills();
                            Bukkit.getScheduler().cancelTask(taskId[0]);
                            removeNoteBlocks();
                            return;
                        }
                        for(Player p : players) {
                            p.sendMessage("§cSkywars starting in " + time + "...");
                            p.playNote(playerNoteBlocks.get(p), Instrument.PIANO, new Note((byte)1, Note.Tone.G, false));
                        }
                        time--;
                    }
                },
                0L,
                20L
        );
    }

    private void removeSpawnCages() {
        for(Location loc : spawns) {
            int x0 = loc.getBlockX(), y0 = loc.getBlockY(), z0 = loc.getBlockZ();
            for(int x = x0-2;x<=x0+2;x++)
                for(int y=y0-2;y<=y0+2;y++)
                    for(int z=z0-2;z<=z0+2;z++) {
                        Block b = world.getBlockAt(x,y,z);
                        if(b.getType() == Material.GLASS) b.setType(Material.AIR);
                    }
        }
    }

    private void startRefills() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            if (ended) return;
            fillChests(spawnChests, true);
            fillChests(midChests, false);
            refill1 = true;
            for(Player p : players) p.sendMessage("§aFirst chest refill!");
        }, 120*20L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            if (ended) return;
            fillChests(spawnChests, true);
            fillChests(midChests, false);
            refill2 = true;
            for(Player p : players) p.sendMessage("§aSecond chest refill!");
        }, 240*20L);
    }

    private void fillChests(List<Location> chests, boolean spawnChest) {
        for(Location loc : chests) {
            Block b = loc.getBlock();
            b.setType(Material.CHEST);
            Chest chest = (Chest)b.getState();
            chest.getInventory().clear();

            List<ItemStack> loot = generateLoot(spawnChest);

            boolean[] filled = new boolean[27];
            for(ItemStack i : loot) {
                int slot;
                do slot = rand.nextInt(27);
                while(filled[slot]);
                filled[slot] = true;
                chest.getInventory().setItem(slot, i);
            }
        }
    }

    private List<ItemStack> generateLoot(boolean spawnChest) {
        List<ItemStack> items = new ArrayList<>();
        Random r = new Random();

        if(spawnChest) {
            // mostly basic stuff
            items.add(new ItemStack(Material.WOOD, 24 + r.nextInt(16))); // wood
            items.add(new ItemStack(Material.STONE, 8 + r.nextInt(8)));   // stone

            // weapons: mostly stone, small chance diamond sword
            if(r.nextInt(100) < 10) items.add(new ItemStack(Material.DIAMOND_SWORD, 1, (short)0)); // rare
            else items.add(new ItemStack(Material.STONE_SWORD, 1, (short)0)); // default

            // tools: mostly basic, tiny chance diamond pickaxe
            if(r.nextInt(100) < 5) items.add(new ItemStack(Material.DIAMOND_PICKAXE, 1, (short)0));
            else items.add(new ItemStack(Material.IRON_AXE, 1, (short)0));

            // armor: mostly iron, 1 piece max
            ItemStack[] armors = {
                    new ItemStack(Material.IRON_HELMET, 1, (short)0),
                    new ItemStack(Material.IRON_CHESTPLATE, 1, (short)0),
                    new ItemStack(Material.IRON_LEGGINGS, 1, (short)0),
                    new ItemStack(Material.IRON_BOOTS, 1, (short)0)
            };
            Collections.shuffle(Arrays.asList(armors));
            items.add(armors[0]); // only 1 piece

            // consumables & throwables
            items.add(new ItemStack(Material.SNOW_BALL, 8 + r.nextInt(8)));
            items.add(new ItemStack(Material.EGG, 8 + r.nextInt(8)));

        } else {
            // mid chest: stronger loot, 1-2 armor pieces
            ItemStack[] armors = {
                    new ItemStack(Material.DIAMOND_HELMET, 1, (short)0),
                    new ItemStack(Material.DIAMOND_CHESTPLATE, 1, (short)0),
                    new ItemStack(Material.DIAMOND_LEGGINGS, 1, (short)0),
                    new ItemStack(Material.DIAMOND_BOOTS, 1, (short)0),
                    new ItemStack(Material.IRON_HELMET, 1, (short)0),
                    new ItemStack(Material.IRON_CHESTPLATE, 1, (short)0),
                    new ItemStack(Material.IRON_LEGGINGS, 1, (short)0),
                    new ItemStack(Material.IRON_BOOTS, 1, (short)0)
            };
            List<ItemStack> armorList = new ArrayList<>(Arrays.asList(armors));
            Collections.shuffle(armorList);
            items.add(armorList.get(0));
            if(r.nextBoolean()) items.add(armorList.get(1)); // 50% chance 2nd piece

            if(r.nextInt(100) < 50) items.add(new ItemStack(Material.DIAMOND_SWORD, 1, (short)0));
            if(r.nextInt(100) < 15) items.add(createFireSword());

            if(r.nextInt(100) < 50) items.add(new ItemStack(Material.DIAMOND_PICKAXE, 1, (short)0));
            if(r.nextInt(100) < 40) items.add(new ItemStack(Material.DIAMOND_AXE, 1, (short)0));
            if(r.nextInt(100) < 30) items.add(new ItemStack(Material.FISHING_ROD, 1, (short)0));

            // blocks
            items.add(new ItemStack(Material.WOOD, 16 + r.nextInt(16)));
            items.add(new ItemStack(Material.STONE, 8 + r.nextInt(8)));

            // consumables
            items.add(new ItemStack(Material.GOLDEN_APPLE, 1 + r.nextInt(3)));
            items.add(new ItemStack(Material.SNOW_BALL, 8 + r.nextInt(8)));
            items.add(new ItemStack(Material.EGG, 8 + r.nextInt(8)));

            // refill-only items
            if(refill1 || refill2) {
                if(r.nextInt(100) < 25) items.add(new ItemStack(Material.TNT, 4 + r.nextInt(6)));
                //if(r.nextInt(100) < 35) items.add(new ItemStack(Material.ENDER_PEARL, 1 + r.nextInt(3)));
            }
        }

        // random slots
        boolean[] filled = new boolean[27];
        List<ItemStack> finalItems = new ArrayList<>();
        for(ItemStack i : items) {
            int slot;
            do slot = r.nextInt(27);
            while(filled[slot]);
            filled[slot] = true;
            finalItems.add(i);
        }

        return finalItems;
    }


    private ItemStack createFireSword() {
        return new ItemStack(Material.DIAMOND_SWORD, 1, (short)0);
    }

    public void endGame() {
        if(players.isEmpty()) return;
        if(players.size()==1) {
            Player winner = players.get(0);
            Bukkit.broadcastMessage("§6SkyWars winner: §a" + winner.getName());
        }
        //for(Player p : players) p.sendMessage("§cGame over!");
    }

    public void onMove(Player p) {
        if(p.getLocation().getY()<30) onPlayerDeath(p);
    }

    public void onMoveInQueue(Player p) {}

    public void onQuit(Player p) {
        MinigameManager.remove(p);
        if(players.size() < minPlayers()) MinigameManager.end(this);
    }

    public void onDeath(Player p, EntityDeathEvent e) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            ((CraftPlayer) p).getHandle().playerConnection.a(new Packet9Respawn());
            onPlayerDeath(p);
        }, 2L);
    }

    private void onPlayerDeath(Player p) {
        //for(Player pl : players) pl.sendMessage("§c" + p.getName() + " died!");

        if(players.size()==1) {
            Player winner = players.get(0);
            //Bukkit.broadcastMessage("§6SkyWars winner: §a" + winner.getName());
            MinigameManager.end(this);
        }
        MinigameManager.remove(p);
    }

    @Override
    public boolean canBreak(Player p, BlockBreakEvent e) {
        return canDoStuff;
    }

    @Override
    public boolean canPlace(Player p, BlockPlaceEvent e) {
        return canDoStuff;
    }

    public void onDamage(Player p, EntityDamageEvent e) {
        if(e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            Long start = fallImmunity.get(p);
            if(start == null) {
                // start immunity now
                fallImmunity.put(p, System.currentTimeMillis());
                e.setCancelled(true);
                return;
            } else {
                long elapsed = System.currentTimeMillis() - start;
                if(elapsed < 3000) {
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }

    private void spawnNoteBlocksOnce() {
        for (Player p : players) {
            Location noteLoc = placeNoteBlockBehind(p);
            playerNoteBlocks.put(p, noteLoc);
        }
    }

    private void removeNoteBlocks() {
        for (Location loc : playerNoteBlocks.values()) {
            loc.getBlock().setType(Material.AIR);
        }
        playerNoteBlocks.clear();
    }

    private Location placeNoteBlockBehind(Player p) {
        Location l = p.getLocation();
        float yaw = l.getYaw();
        int dx = 0;
        int dz = 0;

        if(yaw >= -45 && yaw < 45) dz = -1;
        else if(yaw >= 45 && yaw < 135) dx = -1;
        else if(yaw >= -135 && yaw < -45) dx = 1;
        else dz = 1;

        Location b = l.clone().add(dx, 4, dz);
        b.getBlock().setType(Material.NOTE_BLOCK);
        return b;
    }
}
