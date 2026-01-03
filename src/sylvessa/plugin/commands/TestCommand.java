package sylvessa.plugin.commands;
import org.bukkit.command.CommandSender;

public class TestCommand implements PluginCommand {
    public String name() {
        return "testc";
    }

    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("Whats Good " + sender.getName());
    }
}
