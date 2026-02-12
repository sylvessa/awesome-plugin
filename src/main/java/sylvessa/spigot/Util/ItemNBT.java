package sylvessa.spigot.Util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.craftbukkit.v1_4_R1.inventory.CraftItemStack;
import net.minecraft.server.v1_4_R1.NBTTagCompound;

public class ItemNBT {
    public static ItemStack setInt(ItemStack item, String key, int value) {
        net.minecraft.server.v1_4_R1.ItemStack nms = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nms.getTag();
        if (tag == null) tag = new NBTTagCompound();
        tag.setInt(key, value);
        nms.setTag(tag);
        return CraftItemStack.asCraftMirror(nms);
    }

    public static Integer getInt(ItemStack item, String key) {
        if (item == null) return null;
        net.minecraft.server.v1_4_R1.ItemStack nms = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nms.getTag();
        if (tag == null || !tag.hasKey(key)) return null;
        return tag.getInt(key);
    }

    public static ItemStack setString(ItemStack item, String key, String value) {
        net.minecraft.server.v1_4_R1.ItemStack nms = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nms.getTag();
        if (tag == null) tag = new NBTTagCompound();
        tag.setString(key, value);
        nms.setTag(tag);
        return CraftItemStack.asCraftMirror(nms);
    }

    public static ItemStack setDouble(ItemStack item, String key, double value) {
        net.minecraft.server.v1_4_R1.ItemStack nms = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nms.getTag();
        if (tag == null) tag = new NBTTagCompound();
        tag.setDouble(key, value);
        nms.setTag(tag);
        return CraftItemStack.asCraftMirror(nms);
    }

    public static ItemStack setBoolean(ItemStack item, String key, boolean value) {
        net.minecraft.server.v1_4_R1.ItemStack nms = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nms.getTag();
        if (tag == null) tag = new NBTTagCompound();
        tag.setBoolean(key, value);
        nms.setTag(tag);
        return CraftItemStack.asCraftMirror(nms);
    }

    public static String getString(ItemStack item, String key) {
        if (item == null) return null;
        net.minecraft.server.v1_4_R1.ItemStack nms = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nms.getTag();
        if (tag == null || !tag.hasKey(key)) return null;
        return tag.getString(key);
    }

    public static Double getDouble(ItemStack item, String key) {
        if (item == null) return null;
        net.minecraft.server.v1_4_R1.ItemStack nms = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nms.getTag();
        if (tag == null || !tag.hasKey(key)) return null;
        return tag.getDouble(key);
    }

    public static boolean getBoolean(ItemStack item, String key) {
        if (item == null) return false;
        net.minecraft.server.v1_4_R1.ItemStack nms = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nms.getTag();
        if (tag == null || !tag.hasKey(key)) return false;
        return tag.getBoolean(key);
    }

    public static boolean hasKey(ItemStack item, String key) {
        if (item == null) return false;
        net.minecraft.server.v1_4_R1.ItemStack nms = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nms.getTag();
        return tag != null && tag.hasKey(key);
    }

    public static ItemStack removeKey(ItemStack item, String key) {
        if (item == null) return item;
        net.minecraft.server.v1_4_R1.ItemStack nms = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nms.getTag();
        if (tag != null && tag.hasKey(key)) {
            tag.remove(key);
            nms.setTag(tag);
        }
        return CraftItemStack.asCraftMirror(nms);
    }

    public static ItemStack setByte(ItemStack item, String key, byte value) {
        net.minecraft.server.v1_4_R1.ItemStack nms = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nms.getTag();
        if (tag == null) tag = new NBTTagCompound();
        tag.setByte(key, value);
        nms.setTag(tag);
        return CraftItemStack.asCraftMirror(nms);
    }

    public static Byte getByte(ItemStack item, String key) {
        if (item == null) return null;
        net.minecraft.server.v1_4_R1.ItemStack nms = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nms.getTag();
        if (tag == null || !tag.hasKey(key)) return null;
        return tag.getByte(key);
    }

    public static ItemStack setByteArray(ItemStack item, String key, byte[] value) {
        net.minecraft.server.v1_4_R1.ItemStack nms = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nms.getTag();
        if (tag == null) tag = new NBTTagCompound();
        tag.setByteArray(key, value);
        nms.setTag(tag);
        return CraftItemStack.asCraftMirror(nms);
    }

    public static byte[] getByteArray(ItemStack item, String key) {
        if (item == null) return null;
        net.minecraft.server.v1_4_R1.ItemStack nms = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nms.getTag();
        if (tag == null || !tag.hasKey(key)) return null;
        return tag.getByteArray(key);
    }

    public static ItemStack setCompound(ItemStack item, String key, NBTTagCompound value) {
        net.minecraft.server.v1_4_R1.ItemStack nms = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nms.getTag();
        if (tag == null) tag = new NBTTagCompound();
        tag.set(key, value);
        nms.setTag(tag);
        return CraftItemStack.asCraftMirror(nms);
    }

    public static NBTTagCompound getCompound(ItemStack item, String key) {
        if (item == null) return null;
        net.minecraft.server.v1_4_R1.ItemStack nms = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nms.getTag();
        if (tag == null || !tag.hasKey(key)) return null;
        return tag.getCompound(key);
    }

    public static ItemStack setIntArray(ItemStack item, String key, int[] value) {
        net.minecraft.server.v1_4_R1.ItemStack nms = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nms.getTag();
        if (tag == null) tag = new NBTTagCompound();
        tag.setIntArray(key, value);
        nms.setTag(tag);
        return CraftItemStack.asCraftMirror(nms);
    }

    public static int[] getIntArray(ItemStack item, String key) {
        if (item == null) return null;
        net.minecraft.server.v1_4_R1.ItemStack nms = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nms.getTag();
        if (tag == null || !tag.hasKey(key)) return null;
        return tag.getIntArray(key);
    }

    public static ItemStack setFloat(ItemStack item, String key, Float value) {
        net.minecraft.server.v1_4_R1.ItemStack nms = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nms.getTag();
        if (tag == null) tag = new NBTTagCompound();
        tag.setFloat(key, value);
        nms.setTag(tag);
        return CraftItemStack.asCraftMirror(nms);
    }

    public static Float getFloat(ItemStack item, String key) {
        if (item == null) return null;
        net.minecraft.server.v1_4_R1.ItemStack nms = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nms.getTag();
        if (tag == null || !tag.hasKey(key)) return null;
        return tag.getFloat(key);
    }
}