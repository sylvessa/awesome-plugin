package sylvessa.cb1337.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import sylvessa.cb1337.CustomRecipes;

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
}
