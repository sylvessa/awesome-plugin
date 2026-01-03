package sylvessa.plugin.commands;
import org.bukkit.command.CommandSender;

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
    }
}
