package sylvessa.plugin.Listeners;

import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerChatEvent;
import sylvessa.plugin.Main;
import sylvessa.plugin.UserConfig;

public class ChatListener extends PlayerListener {
    public void onPlayerChat(PlayerChatEvent event) {
        String username = event.getPlayer().getName();

        UserConfig uc = Main.getInstance().getUserConfig(username);

        String color = "f";
        if (uc != null) {
            color = uc.getString("color", "f");
        }

        String displayName = "§" + color + username + "§f";

        event.setFormat("<" + displayName + "> " + event.getMessage());
    }
}
