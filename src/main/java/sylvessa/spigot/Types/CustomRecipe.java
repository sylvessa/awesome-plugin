package sylvessa.spigot.Types;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.Map;

public class CustomRecipe {
    final String[] shape;
    final Map<Character, MaterialData> map;
    public final NbtApplier nbt;

    public CustomRecipe(String[] shape, Map<Character, MaterialData> map, NbtApplier nbt) {
        this.shape = shape;
        this.map = map;
        this.nbt = nbt;
    }

    public boolean matches(ItemStack[] m) {
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < 3; col++) {
                char c = shape[row].charAt(col);
                ItemStack i = m[row * 3 + col];

                if (c == ' ') {
                    if (i != null && i.getType() != Material.AIR) return false;
                } else {
                    MaterialData md = map.get(c);
                    if (i == null) return false;
                    if (i.getType() != md.getItemType()) return false;
                    if (md.getData() != -1 && i.getData().getData() != md.getData()) return false;
                }
            }
        }
        return true;
    }
}
