package sylvessa.cb1337;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.material.MaterialData;
import sylvessa.cb1337.Types.CustomRecipe;
import sylvessa.cb1337.Types.NbtApplier;
import sylvessa.cb1337.Util.ItemNBT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomRecipes {
    private static final List<CustomRecipe> CUSTOM = new ArrayList<CustomRecipe>();

    public static void registerAll() {
//        shaped(
//                new ItemStack(Material.EXP_BOTTLE, 4),
//                new String[] {
//                        " L ",
//                        "LBL",
//                        " L "
//                },
//                'B', Material.GLASS_BOTTLE,
//                'L', new MaterialData(Material.INK_SACK, (byte)4)
//        );
//
//        shaped(
//                new ItemStack(Material.SADDLE, 1),
//                new String[] {
//                        " L ",
//                        "LIL"
//                },
//                'L', Material.LEATHER,
//                'I', Material.IRON_INGOT
//        );
//
//        shaped(
//                new ItemStack(Material.MONSTER_EGG, 2, (short)50),
//                new String[] {
//                        " G ",
//                        "GEG",
//                        " G "
//                },
//                'G', Material.SULPHUR,
//                'E', Material.EGG
//        );
//
//        shaped(
//                new ItemStack(Material.MONSTER_EGG, 2, (short)91),
//                new String[] {
//                        " W ",
//                        "WEW",
//                        " W "
//                },
//                'W', Material.WOOL,
//                'E', Material.EGG
//        );
//
//        ItemStack stick = new ItemStack(Material.WHEAT, 1);
//
//        shaped(
//                stick,
//                new String[] {
//                        " W ",
//                        "W  ",
//                },
//                'W', Material.WOOL
//        );

        shaped(
                new ItemStack(Material.SADDLE, 1),
                new String[] {
                        " L ",
                        "LIL",
                },
                null,
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
                null,
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
                null,
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
                null,
                'B', Material.GLASS_BOTTLE,
                'L', new MaterialData(Material.INK_SACK, (byte)4)
        );

        ItemStack wheat = new ItemStack(Material.WHEAT, 1);

        shaped(
                wheat,
                new String[] {
                        " W ",
                        "W  "
                },
                item -> ItemNBT.setInt(item, "cooltag", 195),
                'W', Material.WOOL
        );
    }

    private static void shaped(ItemStack result, String[] shape, NbtApplier nbt, Object... ingredients) {
        ShapedRecipe recipe = new ShapedRecipe(result);
        recipe.shape(shape);

        Map<Character, MaterialData> map = new HashMap<Character, MaterialData>();

        for (int i = 0; i < ingredients.length; i += 2) {
            char key = (Character) ingredients[i];
            Object val = ingredients[i + 1];

            if (val instanceof Material) {
                MaterialData md = new MaterialData((Material) val);
                recipe.setIngredient(key, md);
                map.put(key, md);
            } else if (val instanceof MaterialData) {
                recipe.setIngredient(key, (MaterialData) val);
                map.put(key, (MaterialData) val);
            }
        }

        Bukkit.addRecipe(recipe);
        CUSTOM.add(new CustomRecipe(shape, map, nbt));
    }

    public static ItemStack matchAndApply(ItemStack[] matrix, ItemStack result) {
        for (CustomRecipe r : CUSTOM) {
            if (r.matches(matrix)) {
                if (r.nbt != null) {
                    return r.nbt.apply(result);
                }
                return result;
            }
        }
        return result;
    }
}