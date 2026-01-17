package sylvessa.cb1337;

public class Log {
    public static String prefix = "[Sylvessa] ";

    public static void info(String msg) {
        org.bukkit.Bukkit.getServer().getLogger().info(prefix + msg);
    }
}
