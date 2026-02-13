package sylvessa.spigot.Duels.Modes;

import net.minecraft.server.v1_4_R1.Packet9Respawn;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_4_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import sylvessa.spigot.ChunkGenerators.Void;
import sylvessa.spigot.Duels.DuelGame;
import sylvessa.spigot.Duels.DuelManager;
import sylvessa.spigot.Duels.DuelType;
import sylvessa.spigot.Main;
import sylvessa.spigot.Util.CustomWorldLoader;
import sylvessa.spigot.Util.DiscordWebhook;

import java.util.Random;

import static sylvessa.spigot.Util.Helpers.freezeWorldTime;

public class PvPDuel extends DuelGame {
    private int countdown = 5;
    private int taskId = -1;

    private static final double MIN_X = -230;
    private static final double MAX_X = -148;
    private static final double MIN_Z = 111;
    private static final double MAX_Z = 191;

    public PvPDuel(Player p1, Player p2) {
        super(p1, p2);
    }

    public DuelType getType() {
        return DuelType.PVP;
    }

    public void start() {
        createWorld();
        freezeWorldTime(world, 6000);
        preparePlayers();
        startCountdown();
    }

    private void createWorld() {
        try {
            String name = "duel_pvp_" + new Random().nextInt(1000000);
            CustomWorldLoader.copyArenaToServerJar("duel_pvp_fancy", name);
            world = Bukkit.createWorld(
                    new WorldCreator(name)
                            .environment(World.Environment.NORMAL)
                            .generator(new Void()));
        } catch (Exception ignored) {}
    }

    private void preparePlayers() {
        Location l1 = new Location(world, -225, 66, 151, -90f, 0f);
        Location l2 = new Location(world, -151, 66, 151, 90f, 0f);

        p1.teleport(l1);
        p2.teleport(l2);

        resetPlayer(p1);
        resetPlayer(p2);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> giveGear(p1), 2L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> giveGear(p2), 2L);
    }

    private void resetPlayer(Player p) {
        p.setHealth(20);
        p.setFoodLevel(20);
        p.setSaturation(20);
        p.setFireTicks(0);
    }

    private void giveGear(Player p) {
        PlayerInventory i = p.getInventory();

        i.setHelmet(new ItemStack(Material.IRON_HELMET, 1, (short)0));
        i.setChestplate(new ItemStack(Material.IRON_CHESTPLATE, 1, (short)0));
        i.setLeggings(new ItemStack(Material.IRON_LEGGINGS, 1, (short)0));
        i.setBoots(new ItemStack(Material.IRON_BOOTS, 1, (short)0));

        i.setItem(0, new ItemStack(Material.IRON_SWORD, 1, (short)0));
        i.setItem(1, new ItemStack(Material.BOW, 1, (short)0));
        i.setItem(2, new ItemStack(Material.FISHING_ROD, 1, (short)0));
        i.setItem(3, new ItemStack(Material.FLINT_AND_STEEL, 1, (short)0));
        i.setItem(4, new ItemStack(Material.GOLDEN_APPLE, 8, (short)0));

        i.setItem(9, new ItemStack(Material.ARROW, 64, (short)0));
    }

    private void startCountdown() {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                Main.getInstance(),
                () -> {
                    if(countdown == 0) {
                        started = true;

                        p1.sendMessage(ChatColor.GREEN + "GO");
                        p2.sendMessage(ChatColor.GREEN +"GO");

                        Bukkit.getScheduler().cancelTask(taskId);

                        playNoteAll(p1.getLocation(), p2.getLocation(), 1.35f);
                        return;
                    }

                    p1.sendMessage(ChatColor.YELLOW.toString() + countdown);
                    p2.sendMessage(ChatColor.YELLOW.toString() + countdown);

                    float basePitch = 0.6f;
                    float maxPitch = 1.15f;
                    int totalCountdown = 4;
                    float pitch = basePitch + ((totalCountdown - countdown) / (float)totalCountdown) * (maxPitch - basePitch);

                    playNoteAll(p1.getLocation(), p2.getLocation(), pitch);

                    countdown--;
                },
                0L,
                20L
        );
    }

    public void onMove(Player p) {
        if(!isParticipant(p)) return;

        if(!started) {
            p.teleport(p.getLocation());
            return;
        }

        Location l = p.getLocation();
        double x = l.getX();
        double z = l.getZ();

        if(x < MIN_X) x = MIN_X;
        if(x > MAX_X) x = MAX_X;
        if(z < MIN_Z) z = MIN_Z;
        if(z > MAX_Z) z = MAX_Z;

        if(x != l.getX() || z != l.getZ()) {
            l.setX(x);
            l.setZ(z);
            p.teleport(l);
        }
    }

    public void onDamage(Player p, EntityDamageEvent e) {
        if(e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            e.setCancelled(true);
        }
    }

    public void onDeath(Player p, PlayerDeathEvent event) {
        event.getDrops().clear();

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            ((CraftPlayer) p).getHandle().playerConnection.a(new Packet9Respawn());

            Player winner = p == p1 ? p2 : p1;
            finish(winner, p);
        }, 2L);
    }

    private void finish(Player winner, Player loser) {
        if(finished) return;
        finished = true;

        Bukkit.broadcastMessage("§a" + winner.getName() + " won a PvP duel against " + loser.getName());

        new DiscordWebhook()
                .setUsername(winner.getName())
                .setAvatarUrl("https://mc-heads.net/avatar/" + winner.getName())
                .sendMessage(winner.getName() + " won a PvP duel against " + loser.getName(), 16776960);

        cleanup();
    }

    public boolean canBreak(Player p, BlockBreakEvent e) {
        return false;
    }

    public boolean canPlace(Player p, BlockPlaceEvent e) {
        return started;
    }

    public void onQuit(Player p) {
        if(!finished && isParticipant(p)) {
            Player winner = p == p1 ? p2 : p1;
            finish(winner, p);
        }
    }

    @Override
    public void onFoodLevelChange(Player p, FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    private void cleanup() {
        p1.setHealth(20);
        p2.setHealth(20);

        DuelManager.end(this);
    }

    private void playNoteAll(Location l1, Location l2, float pitch) {
        p1.playSound(l1, Sound.NOTE_PLING, 1.0f, pitch);
        p2.playSound(l2, Sound.NOTE_PLING, 1.0f, pitch);
    }
}
