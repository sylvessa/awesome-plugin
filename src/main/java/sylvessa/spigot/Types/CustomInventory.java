package sylvessa.spigot.Types;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CustomInventory implements InventoryHolder {

    private final int size;
    private final String title;
    private final Inventory inv;
    private final Map<Integer, Consumer<Player>> callbacks = new HashMap<>();
    private Consumer<Player> closeCallback = null;
    private boolean readOnly = false;

    public CustomInventory(String title, int size) {
        this.size = size;
        this.title = title;
        this.inv = Bukkit.createInventory(this, size, title);
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public void setItem(int slot, ItemStack item) {
        inv.setItem(slot, item);
    }

    public ItemStack getItem(int slot) {
        return inv.getItem(slot);
    }

    public void setCallback(int slot, Consumer<Player> callback) {
        callbacks.put(slot, callback);
    }

    public void setCloseCallback(Consumer<Player> callback) {
        this.closeCallback = callback;
    }

    public void open(Player player) {
        player.openInventory(inv);
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    public static class InventoryListener implements Listener {

        @EventHandler
        public void onClick(InventoryClickEvent e) {
            if(!(e.getInventory().getHolder() instanceof CustomInventory)) return;

            CustomInventory ci = (CustomInventory) e.getInventory().getHolder();
            if(e.getRawSlot() < 0 || e.getRawSlot() >= ci.size) return;

            if(ci.readOnly) e.setCancelled(true);

            Consumer<Player> cb = ci.callbacks.get(e.getRawSlot());
            if(cb != null && e.getWhoClicked() instanceof Player) {
                cb.accept((Player)e.getWhoClicked());
            }
        }

        @EventHandler
        public void onClose(InventoryCloseEvent e) {
            if(!(e.getInventory().getHolder() instanceof CustomInventory)) return;

            CustomInventory ci = (CustomInventory) e.getInventory().getHolder();
            if(ci.closeCallback != null && e.getPlayer() instanceof Player) {
                ci.closeCallback.accept((Player)e.getPlayer());
            }
        }
    }
}
