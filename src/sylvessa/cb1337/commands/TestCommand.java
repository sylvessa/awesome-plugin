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

        //Player p = (Player) sender;
        //int ping = ((CraftPlayer)p).getHandle().ping;
        //p.sendMessage("§aPing: §f" + ping + "ms");

//        FakePlayer npc = new FakePlayer("gawg", p.getLocation());
//        npc.spawn();

//        Packet41MobEffect packet = new Packet41MobEffect(p.getEntityId(), new MobEffect(14, 20*30, 5));
//        ((CraftPlayer)p).getHandle().netServerHandler.sendPacket(packet);


        //p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 30, 5));
        //Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), npc::lookAtNearestPlayer, 0L, 5L);
    }
}
