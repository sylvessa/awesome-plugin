package sylvessa.cb1337;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.material.MaterialData;

public class CustomRecipes {

    public static void registerAll() {
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

        shaped(
                new ItemStack(Material.SADDLE, 1),
                new String[] {
                        " L ",
                        "LIL"
                },
                'L', Material.LEATHER,
                'I', Material.IRON_INGOT
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
    }

    private static void shaped(ItemStack result, String[] shape, Object... ingredients) {
        ShapedRecipe recipe = new ShapedRecipe(result);
        recipe.shape(shape);

        for(int i = 0; i < ingredients.length; i += 2) {
            char key = (Character) ingredients[i];
            Object value = ingredients[i + 1];

            if(value instanceof Material) {
                recipe.setIngredient(key, (Material) value);
            } else if(value instanceof MaterialData) {
                recipe.setIngredient(key, (MaterialData) value);
            }
        }

        Bukkit.addRecipe(recipe);
    }
}