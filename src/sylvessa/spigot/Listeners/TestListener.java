package sylvessa.spigot.Listeners;

import org.bukkit.event.Listener;

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
//        cart.setDamage(0);
//        cart.setMaxSpeed(0);
//        cart.setPassenger(p);
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
