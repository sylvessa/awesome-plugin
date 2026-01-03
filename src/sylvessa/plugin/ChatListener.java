package sylvessa.plugin;

import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerChatEvent;

public class ChatListener extends PlayerListener {
    public static String getPlayerColor(String username) {
        switch(username.toLowerCase()) {
            case "scorner": return "c";  // red
            case "cirrusmutatus": return "d"; // pink
            default: return "f"; // white
        }
    }

    public void onPlayerChat(PlayerChatEvent event) {
        String username = event.getPlayer().getName();
        String color = getPlayerColor(username);

        String displayName = "§" + color + username + "§f";

        event.setFormat("<" + displayName + "> " + event.getMessage());
    }
}
