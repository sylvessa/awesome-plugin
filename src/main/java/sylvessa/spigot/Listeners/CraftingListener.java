package sylvessa.spigot.Listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import sylvessa.spigot.CustomRecipes;

public class CraftingListener implements Listener {
    @EventHandler
    public void onCraft(CraftItemEvent e) {
        ItemStack out = e.getCurrentItem();
        if (out == null) return;

        ItemStack result =
                CustomRecipes.matchAndApply(
                        e.getInventory().getMatrix(),
                        out
                );

        e.setCurrentItem(result);
    }

    @EventHandler
    public void onPrepare(PrepareItemCraftEvent e) {
        ItemStack out = e.getRecipe() != null ? e.getRecipe().getResult() : null;
        if (out == null) return;

        ItemStack result =
                CustomRecipes.matchAndApply(
                        e.getInventory().getMatrix(),
                        out.clone()
                );

        e.getInventory().setResult(result);
    }

    @EventHandler
    public void onCraftClick(InventoryClickEvent e) {
        if (!(e.getInventory() instanceof CraftingInventory)) return;
        if (e.getRawSlot() != 0) return;

        ItemStack current = e.getCurrentItem();
        if (current == null) return;

        CraftingInventory inv = (CraftingInventory) e.getInventory();
        ItemStack[] matrix = inv.getMatrix();

        ItemStack modified = CustomRecipes.matchAndApply(matrix, current.clone());
        if (modified == current) return;

        e.setCancelled(true);

        if (e.getCursor() == null || e.getCursor().getType() == Material.AIR) {
            e.setCursor(modified);
        } else {
            return;
        }

        for (int i = 0; i < matrix.length; i++) {
            ItemStack it = matrix[i];
            if (it == null) continue;
            it.setAmount(it.getAmount() - 1);
            if (it.getAmount() <= 0) matrix[i] = null;
        }

        inv.setMatrix(matrix);
    }
}
