package sylvessa.plugin.Types;

import net.minecraft.server.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import sylvessa.plugin.Main;

public class FakePlayer {
    public EntityPlayer npc;
    private final WorldServer world;

    public FakePlayer(String name, Location loc) {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        world = ((CraftWorld) loc.getWorld()).getHandle();
        ItemInWorldManager manager = new ItemInWorldManager(world);

        npc = new EntityPlayer(server, world, name, manager, 0);
        npc.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }

    public void spawn() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            CraftPlayer cp = (CraftPlayer) p;
            if (cp.getHandle().netServerHandler == null) continue;
            cp.getHandle().netServerHandler.sendPacket(new Packet20NamedEntitySpawn(npc));
        }

        Bukkit.getServer().getPluginManager().registerEvents(new org.bukkit.event.Listener() {
            @EventHandler
            public void onJoin(PlayerJoinEvent e) {
                CraftPlayer cp = (CraftPlayer) e.getPlayer();
                cp.getHandle().netServerHandler.sendPacket(new Packet20NamedEntitySpawn(npc));
            }
        }, Main.getInstance());
    }

    public void lookAtNearestPlayer() {
        Player nearest = null;
        double closest = Double.MAX_VALUE;

        for (Player p : Bukkit.getOnlinePlayers()) {
            double dist = npc.getBukkitEntity().getLocation().distanceSquared(p.getLocation());
            if (dist < closest) {
                closest = dist;
                nearest = p;
            }
        }

        if (nearest != null) {
            Location from = npc.getBukkitEntity().getLocation();
            Location to = nearest.getLocation();
            float[] rot = getYawPitch(from, to);

            npc.yaw = rot[0];
            npc.pitch = rot[1];
            npc.lastYaw = rot[0];
            npc.lastPitch = rot[1];

            Packet32EntityLook look = new Packet32EntityLook(
                    npc.id,
                    (byte)(rot[0] * 256F / 360F),
                    (byte)(rot[1] * 256F / 360F)
            );

            for (Player p : Bukkit.getOnlinePlayers()) {
                ((CraftPlayer)p).getHandle().netServerHandler.sendPacket(look);
            }
        }
    }

    private static float[] getYawPitch(Location from, Location to) {
        double dx = to.getX() - from.getX();
        double dy = to.getY() - from.getY();
        double dz = to.getZ() - from.getZ();
        double dist = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float)(Math.toDegrees(Math.atan2(-dx, dz)));
        float pitch = (float)(-Math.toDegrees(Math.atan2(dy, dist)));
        return new float[]{yaw, pitch};
    }
}
