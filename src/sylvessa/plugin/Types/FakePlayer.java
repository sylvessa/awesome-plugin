package sylvessa.plugin.Types;

import net.minecraft.server.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class FakePlayer {
    public EntityPlayer npc;

    public FakePlayer(String name, Location loc) {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();

        ItemInWorldManager manager = new ItemInWorldManager(world);

        npc = new EntityPlayer(server, world, name, manager, 0);
        npc.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }

    public void spawn(Player player) {
        CraftPlayer cp = (CraftPlayer) player;
        cp.getHandle().netServerHandler.sendPacket(new Packet20NamedEntitySpawn(npc));
    }
}
