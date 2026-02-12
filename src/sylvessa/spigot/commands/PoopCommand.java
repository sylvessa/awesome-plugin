package sylvessa.spigot.commands;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sylvessa.spigot.Main;
import sylvessa.spigot.Types.PluginCommand;
import sylvessa.spigot.UserConfig;
import org.bukkit.command.Command;

public class PoopCommand implements PluginCommand {
    public String name() { return "poop"; }
    public String description() { return "Ewwwwwww"; }

    private static final long COOLDOWN = 30 * 60 * 1000;

    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return;
        }

        Player p = (Player) sender;
        World w = p.getWorld();

        if(!w.getName().equals("world") && !w.getName().equals("world_nether") && !w.getName().equals("world_the_end")) {
            sender.sendMessage("§cYou can only use this command in the overworld or nether.");
            return;
        }

        UserConfig uc = Main.getInstance().getUserConfig(p.getName());
        if (uc == null) {
            p.sendMessage("§cYour user config is not loaded.");
            return;
        }

        long now = System.currentTimeMillis();
        long last = uc.getLong("poop.last", 0);

        if (now - last < COOLDOWN) {
            long remaining = (COOLDOWN - (now - last)) / 1000;
            long minutes = remaining / 60;
            long seconds = remaining % 60;
            p.sendMessage("§eyou must wait §6" + minutes + "m " + seconds + "s §ebe fore pooping agian.");
            return;
        }

        p.getInventory().addItem(new ItemStack(Material.DIRT, 2));
        uc.set("poop.last", now);
        uc.save();

        p.sendMessage("§ahehe you pooped!");
    }
}

