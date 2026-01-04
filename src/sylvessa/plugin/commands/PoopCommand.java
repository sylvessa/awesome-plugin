package sylvessa.plugin.commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sylvessa.plugin.Main;
import sylvessa.plugin.UserConfig;

public class PoopCommand implements PluginCommand {
    public String name() { return "poop"; }
    public String description() { return "Ewwwwwww"; }

    private static final long COOLDOWN = 30 * 60 * 1000; // 30 minutes in ms

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return;
        }

        Player p = (Player) sender;
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
