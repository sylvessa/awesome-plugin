package sylvessa.spigot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import sylvessa.spigot.Types.CustomRecipe;
import sylvessa.spigot.Types.NbtApplier;
import sylvessa.spigot.Util.ItemNBT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomRecipes {
    private static final List<CustomRecipe> CUSTOM = new ArrayList<>();

    public static void registerAll() {
        shaped(
                new ItemStack(Material.SADDLE, 1),
                new String[] {
                        " L ",
                        "LIL",
                },
                'I', Material.IRON_INGOT,
                'L', Material.LEATHER
        );

        shaped(
                new ItemStack(Material.MONSTER_EGG, 2, (short)50),
                new String[] {
                        " G ",
                        "GEG",
                        " G "
                },
                'G', Material.SULPHUR,
                'E', Material.EGG
        );

        shaped(
                new ItemStack(Material.MONSTER_EGG, 2, (short)91),
                new String[] {
                        " W ",
                        "WEW",
                        " W "
                },
                'W', Material.WOOL,
                'E', Material.EGG
        );

        shaped(
                new ItemStack(Material.EXP_BOTTLE, 4),
                new String[] {
                        " L ",
                        "LBL",
                        " L "
                },
                'B', Material.GLASS_BOTTLE,
                'L', new MaterialData(Material.INK_SACK, (byte)4)
        );

        ItemStack shoup = new ItemStack(Material.MUSHROOM_SOUP, 1);

        ItemMeta meta = shoup.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + ChatColor.GOLD.toString() + "Flight Soup");
        ArrayList<String> description = new ArrayList<String>();
        description.add(ChatColor.RESET + ChatColor.DARK_PURPLE.toString() + "Makes you fly for 2 hours");
        meta.setLore(description);
        shoup.setItemMeta(meta);

        //shoup = ItemNBT.setInt(shoup, "unc", 166);
        
        shaped(
                shoup,
                new String[] {
                        "WF",
                        "BF"
                },
                'W', Material.NETHER_STAR,
                'B', Material.BOWL,
                'F', Material.FEATHER
        );
    }

    private static void shaped(ItemStack result, String[] shape, Object... ingredients) {
        ShapedRecipe recipe = new ShapedRecipe(result);
        recipe.shape(shape);

        for (int i = 0; i < ingredients.length; i += 2) {
            char key = (Character) ingredients[i];
            Object val = ingredients[i + 1];

            if (val instanceof Material) {
                MaterialData md = new MaterialData((Material) val);
                recipe.setIngredient(key, md);
            } else if (val instanceof MaterialData) {
                recipe.setIngredient(key, (MaterialData) val);
            }
        }

        Bukkit.addRecipe(recipe);
    }
}