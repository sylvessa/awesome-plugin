package sylvessa.cb1337.Util;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import sylvessa.cb1337.Main;
import sylvessa.cb1337.Types.Team;
import sylvessa.cb1337.UserConfig;

public class Helpers {
    public static int freezeWorldTime(World w, long time) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> w.setTime(time), 0L, 20L);
    }

    public static String buildDisplayName(String name) {
        return buildDisplayName(name, true);
    }

    public static String buildDisplayName(String name, boolean includeTeamTag) {
        Main plugin = Main.getInstance();

        UserConfig uc = plugin.getUserConfigs().get(name.toLowerCase());
        if(uc == null) {
            Player player = Bukkit.getPlayerExact(name);
            if(player != null) {
                uc = new UserConfig(player, plugin);
                plugin.getUserConfigs().put(name.toLowerCase(), uc);
            }
        }

        String playerColor = uc != null ? uc.getString("color", "f") : "f";

        String prefix = "";
        if(includeTeamTag) {
            Team team = plugin.getTeamManager().getPlayerTeam(name.toLowerCase());
            if(team != null && team.getTag() != null && !team.getTag().isEmpty()) {
                String tagColor = team.getColor() != null ? team.getColor() : "f";
                prefix = "§" + tagColor + "[" + team.getTag() + "] §f";
            }
        }

        return prefix + "§" + playerColor + name;
    }


}
