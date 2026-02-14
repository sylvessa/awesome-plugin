package sylvessa.spigot.commands;

import net.minecraft.server.v1_4_R1.EntityPlayer;
import net.minecraft.server.v1_4_R1.NBTTagCompound;
import net.minecraft.server.v1_4_R1.Packet17EntityLocationAction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_4_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_4_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import sylvessa.spigot.Types.PluginCommand;
import sylvessa.spigot.Util.ItemNBT;

import java.util.ArrayList;
import java.util.List;

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

    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return;

        Player player = (Player) sender;

        //player.playSound(player.getLocation(), Sound.CAT_MEOW, 1.0f, 1.0f);

        player.sendMessage("hi");

//        ItemStack inHand = player.getItemInHand();
//
//        if (inHand == null || inHand.getType() == Material.AIR) {
//            ItemStack stick = new ItemStack(Material.STICK, 1);
//            stick = ItemNBT.setInt(stick, "unc", 15);
//            player.setItemInHand(stick);
//            return;
//        }
//
//        Integer tag = ItemNBT.getInt(inHand, "unc");
//        if (tag != null) {
//            player.sendMessage("has unc = " + tag);
//        } else {
//            player.sendMessage("no unc");
//        }


//        ItemStack item = new ItemStack(Material.STICK);
//        ItemMeta meta = item.getItemMeta();
//        meta.setDisplayName("whatever");
//        ArrayList<String> description = new ArrayList<String>();
//        description.add("YAS");
//        meta.setLore(description);
//        item.setItemMeta(meta);
//        player.getInventory().addItem(item);

        // nbt demo
//        ItemStack inHand = player.getItemInHand();
//
//        if (inHand == null || inHand.getType() == Material.AIR) {
//            ItemStack stick = new ItemStack(Material.STICK, 1);
//            stick = ItemNBT.setInt(stick, "unc", 15);
//            player.setItemInHand(stick);
//            return;
//        }

//        Integer tag = ItemNBT.getInt(inHand, "unc");
//        if (tag != null) {
//            player.sendMessage("has unc = " + tag);
//        } else {
//            player.sendMessage("no unc");
//        }
    }

//    public void layDown(Player player, Block block) {
//        player.setAllowFlight(true);
//        player.setFlying(true);
//        player.teleport(block.getLocation().add(0.5, 0.5, 0.5));
//
//        EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
//        Packet17EntityLocationAction packet = new Packet17EntityLocationAction(
//                entityPlayer, 0, block.getX(), block.getY(), block.getZ()
//        );
//        entityPlayer.playerConnection.sendPacket(packet);
//    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (!(sender instanceof Player)) return suggestions;
        if (args.length == 1) {
            String prefix = args[0].toLowerCase();
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(prefix)) suggestions.add(p.getName());
            }
        }
        return suggestions;
    }
}
