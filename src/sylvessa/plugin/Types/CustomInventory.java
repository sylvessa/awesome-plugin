package sylvessa.plugin.Types;

import net.minecraft.server.*;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CustomInventory {

    private static int nextWindowId = 1;

    private final int size;
    private final String title;
    private final ItemStack[] items;
    private final Map<Integer, Consumer<Player>> callbacks = new HashMap<>();
    private Consumer<Player> closeCallback = null; // <-- added
    private final IInventory inv;
    private boolean readOnly = false;

    public CustomInventory(String title, int size) {
        this.size = size;
        this.title = title;
        this.items = new ItemStack[size];

        this.inv = new IInventory() {
            public int getSize() { return items.length; }
            public ItemStack getItem(int i) { return items[i]; }
            public void setItem(int i, ItemStack itemstack) { items[i] = itemstack; }
            public String getName() { return title; }
            public boolean c(EntityHuman entityhuman) { return true; }
            public void update() {}
            public boolean a(int i, ItemStack itemstack) { items[i] = itemstack; return true; }
            public void startOpen() {}
            public void closeContainer() {}
            public ItemStack splitStack(int i, int j) {
                if (items[i] != null) {
                    ItemStack stack = items[i];
                    items[i] = null;
                    return stack;
                }
                return null;
            }
            public ItemStack splitWithoutUpdate(int i) { return null; }
            public int getMaxStackSize() { return 64; }
            public boolean a_(EntityHuman entityhuman) { return true; }
            public ItemStack[] getContents() { return items; }
        };
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public void setItem(int slot, ItemStack item) {
        items[slot] = item;
    }

    public void setCallback(int slot, Consumer<Player> callback) {
        callbacks.put(slot, callback);
    }

    public void setCloseCallback(Consumer<Player> callback) {
        this.closeCallback = callback;
    }

    public ItemStack getItem(int slot) {
        return items[slot];
    }

    public void open(Player p) {
        EntityPlayer ep = ((CraftPlayer) p).getHandle();

        ContainerChest container = new ContainerChest(ep.inventory, inv) {
            @Override
            public ItemStack a(int slotNum, int button, boolean shift, EntityHuman human) {
                if (human instanceof EntityPlayer) {
                    Player bukkitPlayer = (Player)((EntityPlayer) human).getBukkitEntity();
                    Consumer<Player> cb = callbacks.get(slotNum);
                    if (cb != null) cb.accept(bukkitPlayer);

                    if (readOnly) {
                        return null;
                    }
                }
                return super.a(slotNum, button, shift, human);
            }

            @Override
            public boolean b(EntityHuman human) {
                boolean b = super.b(human);
                if (closeCallback != null && human instanceof EntityPlayer && !b) {
                    Player p = (Player)((EntityPlayer) human).getBukkitEntity();
                    closeCallback.accept(p);
                }
                return b;
            }
        };


        int windowId = nextWindowId++;
        container.windowId = windowId;
        ep.activeContainer = container;

        Packet100OpenWindow packet = new Packet100OpenWindow(windowId, 0, inv.getName(), inv.getSize());
        ep.netServerHandler.sendPacket(packet);

        for (int i = 0; i < inv.getSize(); i++) {
            Packet103SetSlot slotPacket = new Packet103SetSlot(windowId, i, inv.getItem(i));
            ep.netServerHandler.sendPacket(slotPacket);
        }
    }
}
