package sylvessa.spigot.Types;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class GameTypes {
    public static class SavedState {
        public Location loc;
        public ItemStack[] inv;
        public ItemStack[] armor;
        public int level;
        public float experience;

        public SavedState(Location l, ItemStack[] i, ItemStack[] a, int lvl, float exp) {
            loc = l;
            inv = i;
            armor = a;
            level = lvl;
            experience = exp;
        }
    }
}
