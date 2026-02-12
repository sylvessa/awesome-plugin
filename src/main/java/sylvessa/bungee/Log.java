package sylvessa.bungee;

import net.md_5.bungee.BungeeCord;

public class Log {
    public static String prefix = "[Sylvessa] ";

    public static void info(String msg) {
        BungeeCord.getInstance().getLogger().info(prefix + msg);
    }
}