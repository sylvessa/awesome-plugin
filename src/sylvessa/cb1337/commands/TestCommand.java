package sylvessa.cb1337.commands;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.Packet17EntityLocationAction;
import net.minecraft.server.Packet70Bed;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import sylvessa.cb1337.Types.PluginCommand;

import static org.bukkit.Bukkit.getServer;

public class TestCommand implements PluginCommand {
    public String name() {
        return "testc";
    }

    public String description() {
        return "Makes the current item you're holding your helmet.";
    }

    public boolean hidden() {
        return true;
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;

        //Player p = (Player) sender;

        //layDown(p, p.getLocation());
        //int ping = ((CraftPlayer)p).getHandle().ping;
        //p.sendMessage("§aPing: §f" + ping + "ms");

//        FakePlayer npc = new FakePlayer("gawg", p.getLocation());
//        npc.spawn();
//        Packet41MobEffect packet = new Packet41MobEffect(p.getEntityId(), new MobEffect(14, 20*30, 5));
//        ((CraftPlayer)p).getHandle().netServerHandler.sendPacket(packet);


        //p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 30, 5));
        //Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), npc::lookAtNearestPlayer, 0L, 5L);
    }

//    public void layDown(Player player, Location block) {
//        player.setAllowFlight(true);
//        player.setFlying(true);
//        player.teleport(block.add(0.5, 0.5, 0.5));
//
//        EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
//        Packet17EntityLocationAction packet = new Packet17EntityLocationAction(entityPlayer, 0, block.getBlockX(), block.getBlockY(), block.getBlockZ());
//
//        for(Player p : getServer().getOnlinePlayers())
//            ((CraftPlayer)p).getHandle().netServerHandler.sendPacket(packet);
//
//        Packet70Bed packt = new Packet70Bed(player.getEntityId(), 78);
//
//        for(Player p : getServer().getOnlinePlayers())
//            ((CraftPlayer)p).getHandle().netServerHandler.sendPacket(packt);
//    }
}
