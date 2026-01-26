package sylvessa.cb1337.Types;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class GameTypes {
    public static class SavedState {
        public Location loc;
        public ItemStack[] inv;
        public ItemStack[] armor;
        public Float experience;

        public SavedState(Location l, ItemStack[] i, ItemStack[] a, Float exp) {
            loc = l;
            inv = i;
            armor = a;
            experience = exp;
        }
    }
}
