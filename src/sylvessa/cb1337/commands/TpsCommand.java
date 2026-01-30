package sylvessa.cb1337.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;
import sylvessa.cb1337.Types.PluginCommand;
import net.minecraft.server.MinecraftServer;

public class TpsCommand implements PluginCommand {
    long total = 0L;
    int count = 0;

    public String name() {
        return "tps";
    }

    public String description() {
        return "Shows server TPS and MSPT.";
    }

    public void execute(CommandSender sender, String[] args) {
        MinecraftServer server = ((CraftServer)Bukkit.getServer()).getServer();
        long[] times = server.f;

        for (long time : times) {
            if (time <= 0L) continue;
            total += time;
            count++;
        }

        if (count == 0) {
            sender.sendMessage("§cTPS: calculating...");
            return;
        }

        double avgNs = total / (double)count;
        double mspt = avgNs / 1_000_000.0;
        double tps = 1000.0 / mspt;
        if (tps > 20.0) tps = 20.0;

        sender.sendMessage("§aTPS: §f" + String.format("%.2f", tps)
                + " §7(§f" + String.format("%.2f", mspt) + " mspt§7)");
    }
}
