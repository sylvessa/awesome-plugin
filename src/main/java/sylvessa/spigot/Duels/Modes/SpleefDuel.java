package sylvessa.spigot.Duels.Modes;

import net.minecraft.server.v1_7_R4.MobEffect;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sylvessa.spigot.ChunkGenerators.Void;
import sylvessa.spigot.Duels.DuelGame;
import sylvessa.spigot.Duels.DuelManager;
import sylvessa.spigot.Duels.DuelType;
import sylvessa.spigot.Main;
import sylvessa.spigot.Util.DiscordWebhook;

import java.util.Random;

import static sylvessa.spigot.Util.Helpers.freezeWorldTime;

public class SpleefDuel extends DuelGame {

    private int countdown = 5;
    private int taskId = -1;

    private boolean p1Fell;
    private boolean p2Fell;

    private final int baseY = 64;
    private final int layers = 3;
    private final int layerHeight = 1;
    private final int airHeight = 4;
    private final int width = 20;
    private final int length = 20;

    public SpleefDuel(Player p1, Player p2) {
        super(p1, p2);
    }

    public DuelType getType() {
        return DuelType.SPLEEF;
    }

    public void start() {
        createWorld();
        buildArena();
        freezeWorldTime(world, 6000);
        preparePlayers();
        startCountdown();
    }

    private void createWorld() {
        String name = "duel_spleef_" + new Random().nextInt(1000000);
        world = Bukkit.createWorld(
                new WorldCreator(name)
                        .environment(World.Environment.NORMAL)
                        .generator(new Void()));

        int halfWidth = width / 2;
        int halfLength = length / 2;

        for(int l = 0; l < layers; l++) {
            int y = baseY + l * (layerHeight + airHeight);
            for(int x = -halfWidth; x < halfWidth; x++) {
                for(int z = -halfLength; z < halfLength; z++) {
                    world.getBlockAt(x, y, z).setType(Material.SNOW_BLOCK);
                    for(int ay = 1; ay <= airHeight; ay++) {
                        world.getBlockAt(x, y + ay, z).setType(Material.AIR);
                    }
                }
            }
        }
    }

    private void buildArena() {
        for(int x = -10; x < 10; x++) {
            for(int z = -10; z < 10; z++) {
                world.getBlockAt(x, 64, z).setType(Material.SNOW_BLOCK);
            }
        }
    }

    private void preparePlayers() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(
                Main.getInstance(),
                () -> p1.setItemInHand(new ItemStack(277, 1, (short)0)),
                1L
        );
        Bukkit.getScheduler().scheduleSyncDelayedTask(
                Main.getInstance(),
                () -> p2.setItemInHand(new ItemStack(277, 1, (short)0)),
                1L
        );

        int topLayerY = baseY + (layers - 1) * (layerHeight + airHeight);
        double centerX = 0.0;
        double zOffset = length / 2.0 - 1;

        Location l1 = new Location(world, centerX, topLayerY + 1, -zOffset, 0f, 0f);
        Location l2 = new Location(world, centerX, topLayerY + 1, zOffset, 180f, 0f);

        p1.teleport(l1);
        p2.teleport(l2);

        p1.setHealth(20);
        p2.setHealth(20);

        p1.setFoodLevel(20);
        p1.setSaturation(20);

        p2.setFoodLevel(20);
        p2.setSaturation(20);

        p1.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20 * 30, 2));
        p2.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20 * 30, 2));
    }

    private void startCountdown() {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                Main.getInstance(),
                () -> {
                    if(countdown == 0) {
                        started = true;
                        p1.sendMessage("§aGO");
                        p2.sendMessage("§aGO");

                        Bukkit.getScheduler().cancelTask(taskId);

                        playNoteAll(p1.getLocation(), p2.getLocation(), 1.35f);
                        return;
                    }

                    p1.sendMessage("§e" + countdown);
                    p2.sendMessage("§e" + countdown);

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
        if(!started && isParticipant(p)) {
            p.teleport(p.getLocation());
            return;
        }

        if(started && !finished && p.getWorld() == world && p.getLocation().getY() < 50) {
            markFall(p);
        }
    }

    private void markFall(Player p) {
        if(finished) return;

        if(p == p1) p1Fell = true;
        if(p == p2) p2Fell = true;

        if(p1Fell && p2Fell) {
            finished = true;
            Bukkit.broadcastMessage("§eSpleef duel ended in a tie");
            cleanup();
            return;
        }

        if(p1Fell || p2Fell) {
            finished = true;
            Player loser = p1Fell ? p1 : p2;
            Player winner = loser == p1 ? p2 : p1;

            Bukkit.broadcastMessage("§a" + winner.getName() + " won a spleef duel against " + loser.getName());

            new DiscordWebhook()
                    .setUsername(winner.getName())
                    .setAvatarUrl("https://mc-heads.net/avatar/" + winner.getName())
                    .sendMessage(winner.getName() + " won a spleef duel against " + loser.getName(), 16776960);

            cleanup();
        }
    }

    public void onDamage(Player p, EntityDamageEvent e) {
        e.setCancelled(true);
    }

    public boolean canBreak(Player p, BlockBreakEvent e) {
        return started;
    }

    public boolean canPlace(Player p, BlockPlaceEvent e) {
        return false;
    }

    public void onQuit(Player p) {
        markFall(p);
    }

    @Override
    public void onFoodLevelChange(Player p, FoodLevelChangeEvent event) { event.setCancelled(true); }

    private void cleanup() {
        p1.removePotionEffect(PotionEffectType.FAST_DIGGING);
        p2.removePotionEffect(PotionEffectType.FAST_DIGGING);

        DuelManager.end(this);
    }

    private void playNoteAll(Location l1, Location l2, float pitch) {
        p1.playSound(l1, Sound.NOTE_PLING, 1.0f, pitch);
        p2.playSound(l2, Sound.NOTE_PLING, 1.0f, pitch);
    }
}
