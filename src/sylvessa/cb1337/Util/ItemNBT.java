package sylvessa.cb1337.Util;

import net.minecraft.server.v1_4_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_4_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public final class ItemNBT {
    public static net.minecraft.server.v1_4_R1.ItemStack nms(ItemStack item) {
        net.minecraft.server.v1_4_R1.ItemStack nms =
                CraftItemStack.asNMSCopy(item);

        if (nms.tag == null) {
            nms.tag = new NBTTagCompound();
        }

        return nms;
    }

    public static ItemStack apply(net.minecraft.server.v1_4_R1.ItemStack nms) {
        return CraftItemStack.asBukkitCopy(nms);
    }

    public static boolean has(ItemStack item, String key) {
        net.minecraft.server.v1_4_R1.ItemStack nms =
                CraftItemStack.asNMSCopy(item);

        return nms.tag != null && nms.tag.hasKey(key);
    }

    public static ItemStack remove(ItemStack item, String key) {
        net.minecraft.server.v1_4_R1.ItemStack nms = nms(item);
        nms.tag.remove(key);
        return apply(nms);
    }

    public static ItemStack setByte(ItemStack item, String key, byte v) {
        net.minecraft.server.v1_4_R1.ItemStack nms = nms(item);
        nms.tag.setByte(key, v);
        return apply(nms);
    }

    public static Byte getByte(ItemStack item, String key) {
        net.minecraft.server.v1_4_R1.ItemStack nms =
                CraftItemStack.asNMSCopy(item);

        if (nms.tag == null || !nms.tag.hasKey(key)) return null;
        return nms.tag.getByte(key);
    }

    public static ItemStack setShort(ItemStack item, String key, short v) {
        net.minecraft.server.v1_4_R1.ItemStack nms = nms(item);
        nms.tag.setShort(key, v);
        return apply(nms);
    }

    public static Short getShort(ItemStack item, String key) {
        net.minecraft.server.v1_4_R1.ItemStack nms =
                CraftItemStack.asNMSCopy(item);

        if (nms.tag == null || !nms.tag.hasKey(key)) return null;
        return nms.tag.getShort(key);
    }

    public static ItemStack setInt(ItemStack item, String key, int v) {
        net.minecraft.server.v1_4_R1.ItemStack nms = nms(item);
        nms.tag.setInt(key, v);
        return apply(nms);
    }

    public static Integer getInt(ItemStack item, String key) {
        net.minecraft.server.v1_4_R1.ItemStack nms =
                CraftItemStack.asNMSCopy(item);

        if (nms.tag == null || !nms.tag.hasKey(key)) return null;
        return nms.tag.getInt(key);
    }

    public static ItemStack setLong(ItemStack item, String key, long v) {
        net.minecraft.server.v1_4_R1.ItemStack nms = nms(item);
        nms.tag.setLong(key, v);
        return apply(nms);
    }

    public static Long getLong(ItemStack item, String key) {
        net.minecraft.server.v1_4_R1.ItemStack nms =
                CraftItemStack.asNMSCopy(item);

        if (nms.tag == null || !nms.tag.hasKey(key)) return null;
        return nms.tag.getLong(key);
    }

    public static ItemStack setFloat(ItemStack item, String key, float v) {
        net.minecraft.server.v1_4_R1.ItemStack nms = nms(item);
        nms.tag.setFloat(key, v);
        return apply(nms);
    }

    public static Float getFloat(ItemStack item, String key) {
        net.minecraft.server.v1_4_R1.ItemStack nms =
                CraftItemStack.asNMSCopy(item);

        if (nms.tag == null || !nms.tag.hasKey(key)) return null;
        return nms.tag.getFloat(key);
    }

    public static ItemStack setDouble(ItemStack item, String key, double v) {
        net.minecraft.server.v1_4_R1.ItemStack nms = nms(item);
        nms.tag.setDouble(key, v);
        return apply(nms);
    }

    public static Double getDouble(ItemStack item, String key) {
        net.minecraft.server.v1_4_R1.ItemStack nms =
                CraftItemStack.asNMSCopy(item);

        if (nms.tag == null || !nms.tag.hasKey(key)) return null;
        return nms.tag.getDouble(key);
    }

    public static ItemStack setString(ItemStack item, String key, String v) {
        net.minecraft.server.v1_4_R1.ItemStack nms = nms(item);
        nms.tag.setString(key, v);
        return apply(nms);
    }

    public static String getString(ItemStack item, String key) {
        net.minecraft.server.v1_4_R1.ItemStack nms =
                CraftItemStack.asNMSCopy(item);

        if (nms.tag == null || !nms.tag.hasKey(key)) return null;
        return nms.tag.getString(key);
    }

    public static ItemStack setByteArray(ItemStack item, String key, byte[] v) {
        net.minecraft.server.v1_4_R1.ItemStack nms = nms(item);
        nms.tag.setByteArray(key, v);
        return apply(nms);
    }

    public static byte[] getByteArray(ItemStack item, String key) {
        net.minecraft.server.v1_4_R1.ItemStack nms =
                CraftItemStack.asNMSCopy(item);

        if (nms.tag == null || !nms.tag.hasKey(key)) return null;
        return nms.tag.getByteArray(key);
    }

    public static ItemStack setIntArray(ItemStack item, String key, int[] v) {
        net.minecraft.server.v1_4_R1.ItemStack nms = nms(item);
        nms.tag.setIntArray(key, v);
        return apply(nms);
    }

    public static int[] getIntArray(ItemStack item, String key) {
        net.minecraft.server.v1_4_R1.ItemStack nms =
                CraftItemStack.asNMSCopy(item);

        if (nms.tag == null || !nms.tag.hasKey(key)) return null;
        return nms.tag.getIntArray(key);
    }

    public static ItemStack setCompound(ItemStack item, String key, net.minecraft.server.v1_4_R1.NBTTagCompound c) {
        net.minecraft.server.v1_4_R1.ItemStack nms = nms(item);
        nms.tag.setCompound(key, c);
        return apply(nms);
    }

    public static net.minecraft.server.v1_4_R1.NBTTagCompound getCompound(ItemStack item, String key) {
        net.minecraft.server.v1_4_R1.ItemStack nms =
                CraftItemStack.asNMSCopy(item);

        if (nms.tag == null || !nms.tag.hasKey(key)) return null;
        return nms.tag.getCompound(key);
    }

    public static ItemStack setList(ItemStack item, String key, net.minecraft.server.v1_4_R1.NBTTagList list) {
        net.minecraft.server.v1_4_R1.ItemStack nms = nms(item);
        nms.tag.set(key, list);
        return apply(nms);
    }

    public static net.minecraft.server.v1_4_R1.NBTTagList getList(ItemStack item, String key) {
        net.minecraft.server.v1_4_R1.ItemStack nms =
                CraftItemStack.asNMSCopy(item);

        if (nms.tag == null || !nms.tag.hasKey(key)) return null;
        return nms.tag.getList(key);
    }
}
