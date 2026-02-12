package sylvessa.spigot.Types;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import java.util.Collections;
import java.util.List;

public interface PluginCommand extends CommandExecutor, TabCompleter {
    String name();
    void execute(CommandSender sender, Command cmd, String label, String[] args);

    default String description() { return "No description provided"; }
    default boolean hidden() { return false; }
    default void execute(CommandSender sender, String[] args) {
        execute(sender, null, null, args);
    }

    @Override
    default boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        execute(sender, cmd, label, args);
        return true;
    }

    @Override
    default List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return Collections.emptyList();
    }
}
