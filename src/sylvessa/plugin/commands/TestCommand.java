package sylvessa.plugin.commands;

import net.minecraft.server.*;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import org.bukkit.inventory.Inventory;
import sylvessa.plugin.Main;
import sylvessa.plugin.Types.CustomInventory;
import sylvessa.plugin.Types.FakePlayer;

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

        CustomInventory menu = new CustomInventory("AWESOME UI", 9);
        menu.setItem(4, new ItemStack(264, 1, 0));
        menu.setCallback(4, player -> player.sendMessage("clicked!"));
        menu.setCloseCallback(player -> player.sendMessage("closed inventory"));
        menu.setReadOnly(true);
        menu.open(p);

    }
}
