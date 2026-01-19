package sylvessa.cb1337.Util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DamageCalculator {

    public boolean wouldKill(Player p, double rawDamage) {
        double damage = rawDamage;
        damage = applyArmor(p, damage);
        damage = applyResistance(p, damage);
        damage = applyAbsorption(p, damage);
        return damage >= p.getHealth();
    }

    private double applyArmor(Player p, double damage) {
        int armor = 0;
        for (ItemStack item : p.getInventory().getArmorContents()) {
            if (item == null || item.getType() == Material.AIR) continue;
            armor += getArmorPoints(item);
        }
        double reduction = armor / 25.0; // simplified approximation
        if (reduction > 0.8) reduction = 0.8;
        return damage * (1 - reduction);
    }

    private int getArmorPoints(ItemStack item) {
        switch (item.getType()) {
            case DIAMOND_HELMET:
            case DIAMOND_CHESTPLATE:
            case DIAMOND_LEGGINGS:
            case DIAMOND_BOOTS: return 8;
            case IRON_HELMET:
            case IRON_CHESTPLATE:
            case IRON_LEGGINGS:
            case IRON_BOOTS: return 6;
            case GOLD_HELMET:
            case GOLD_CHESTPLATE:
            case GOLD_LEGGINGS:
            case GOLD_BOOTS: return 5;
            case CHAINMAIL_HELMET:
            case CHAINMAIL_CHESTPLATE:
            case CHAINMAIL_LEGGINGS:
            case CHAINMAIL_BOOTS: return 5;
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS: return 3;
            default: return 0;
        }
    }

    private double applyResistance(Player p, double damage) {
//        for (Potion effect : p.getActivePotionEffects()) {
//            if (effect.getType() == PotionEffectType.DAMAGE_RESISTANCE) {
//                int level = effect.getAmplifier() + 1;
//                damage *= 1 - (0.2 * level);
//            }
//        }
        return damage;
    }

    private double applyAbsorption(Player p, double damage) {
//        double absorption = p.getAbsorptionAmount();
//        if (absorption >= damage) return 0;
        return damage - 0; // absorption
    }
}
