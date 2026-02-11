package sylvessa.cb1337.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.cb1337.Types.PluginCommand;

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

        Player p = (Player) sender;
        p.sendMessage("hi");

        // nbt demo
//        ItemStack inHand = p.getItemInHand();
//
//        if (inHand == null || inHand.getType() == Material.AIR) {
//            ItemStack stick = new ItemStack(Material.STICK, 1);
//            stick = ItemNBT.setInt(stick, "cooltag", 195);
//            stick = ItemNBT.setString(stick, "owner", p.getName());
//            p.setItemInHand(stick);
//            return;
//        }
//
//        Integer tag = ItemNBT.getInt(inHand, "cooltag");
//
//        if (tag != null) {
//            p.sendMessage("cooltag = " + tag);
//        } else {
//            p.sendMessage("no cooltag");
//        }
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
//            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
//
//        Packet70Bed packt = new Packet70Bed(player.getEntityId(), 78);
//
//        for(Player p : getServer().getOnlinePlayers())
//            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packt);
//    }
}
