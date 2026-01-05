package sylvessa.plugin.commands;
import me.devcody.uberbukkit.util.math.Vec3i;
import net.minecraft.server.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class TestCommand implements PluginCommand {
    public String name() {
        return "testc";
    }

    public boolean hidden() {
        return true;
    }

    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("Whats Good " + sender.getName());
        // pls work

    }
}
