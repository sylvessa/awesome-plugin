package sylvessa.plugin.commands;

import net.minecraft.server.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import org.bukkit.inventory.Inventory;
import sylvessa.plugin.ChunkGenerators.Void;
import sylvessa.plugin.Main;
import sylvessa.plugin.Types.CustomInventory;
import sylvessa.plugin.Types.FakePlayer;
import sylvessa.plugin.Util.CustomWorldLoader;
import uk.betacraft.uberbukkit.packet.Packet62Sound;

import java.io.IOException;

@SuppressWarnings("unused")
public class TestCommand implements PluginCommand {
    public String name() {
        return "testc";
    }

    public boolean hidden() {
        return true;
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;

        Player p = (Player) sender;

//        try {
//            CustomWorldLoader.copyArenaToServerJar("duel_bridge_main", "duel_bridge_main");
//            Bukkit.createWorld("duel_bridge_main", World.Environment.NORMAL, new Void());
//            p.teleport(new org.bukkit.Location(Bukkit.getWorld("duel_bridge_main"), -154, 60, -24));
//        } catch (Exception e) {
//            sender.sendMessage(ChatColor.RED + "A CRITICAL ERROR OCCURRED (copyArena Catastrophic Failure)");
//            return;
//        }

        sender.sendMessage("ok");

//        Packet62Sound packet = new Packet62Sound("note.harp", p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ(), 1.0f, 1);
//        ((CraftPlayer)p).getHandle().netServerHandler.sendPacket(packet);

        CustomInventory menu = new CustomInventory("AWESOME UI", 9);
        menu.setItem(4, new ItemStack(264, 1, 0));
        menu.setCallback(4, player -> player.sendMessage("clicked!"));
        menu.setCloseCallback(player -> player.sendMessage("closed inventory"));
        menu.setReadOnly(true);
        menu.open(p);

//        FakePlayer npc = new FakePlayer("npc1", p.getLocation());
//        npc.spawn();
//
//        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), npc::lookAtNearestPlayer, 0L, 5L);
    }
}
