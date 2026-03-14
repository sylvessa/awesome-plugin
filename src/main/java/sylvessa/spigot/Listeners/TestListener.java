package sylvessa.spigot.Listeners;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftMinecart;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class TestListener implements Listener {
//    private final Map<String, Minecart> sitting = new HashMap<>();
//
//    @EventHandler
//    public void onInteract(PlayerInteractEvent e) {
//        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
//
//        Player p = e.getPlayer();
//        if (sitting.containsKey(p.getName())) return;
//
//        Block b = e.getClickedBlock();
//        if (b == null) return;
//
//        if (!b.getType().name().endsWith("_STAIRS")) return;
//
//        e.setCancelled(true);
//
//        Location l = b.getLocation().add(0.5, 0.0, 0.5);
//
//        Minecart cart = b.getWorld().spawn(l, Minecart.class);
//
//        EntityMinecart nms = ((CraftMinecart) cart).getHandle();
//        nms.setInvisible(true);
//
//        cart.setDamage(0);
//        cart.setMaxSpeed(0);
//        cart.setPassenger(p);
//        cart.setVelocity(new Vector(0, 0, 0));
//
//        sitting.put(p.getName(), cart);
//    }
//
//    @EventHandler
//    public void onSneak(PlayerToggleSneakEvent e) {
//        if (!e.isSneaking()) return;
//
//        Player p = e.getPlayer();
//        Minecart cart = sitting.remove(p.getName());
//        if (cart == null) return;
//
//        cart.remove();
//    }
//
//    @EventHandler
//    public void onQuit(PlayerQuitEvent e) {
//        Player p = e.getPlayer();
//        Minecart cart = sitting.remove(p.getName());
//        if (cart != null) cart.remove();
//    }
}
