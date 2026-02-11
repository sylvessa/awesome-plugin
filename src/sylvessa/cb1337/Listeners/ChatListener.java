package sylvessa.cb1337.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import sylvessa.cb1337.Log;
import sylvessa.cb1337.Main;
import sylvessa.cb1337.Teams.TeamManager;
import sylvessa.cb1337.Types.Team;
import sylvessa.cb1337.UserConfig;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static sylvessa.cb1337.Util.Helpers.buildDisplayName;

public class ChatListener implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String username = event.getPlayer().getName();
        String message = event.getMessage();

        Main plugin = Main.getInstance();
        UserConfig uc = plugin.getUserConfig(username);

        String playerColor = "f";
        if(uc != null) {
            playerColor = uc.getString("color", "f");
        }

        TeamManager teamManager = plugin.getTeamManager();
        Team team = teamManager.getPlayerTeam(username);

        String prefix = "";
        if(team != null) {
            String tag = team.getTag() != null ? team.getTag() : "";
            String tagColor = team.getColor() != null ? team.getColor() : "f";
            if(!tag.isEmpty()) {
                prefix = "§" + tagColor + "[" + tag + "] §f";
            }
        }

        String displayName = prefix + "§" + playerColor + username + "§f";

        String displayName2 = buildDisplayName(username, false);
        if(displayName2.length() > 16) displayName2 = displayName2.substring(0, 16);

        if(!event.getPlayer().getPlayerListName().equals(displayName2))
            event.getPlayer().setPlayerListName(displayName2);

        event.setFormat(displayName + ": %2$s");

        String webhook = plugin.getPluginConfig().getString("discord.webhook-url", "");
        if(!webhook.isEmpty()) {
            sendWebhook(webhook, username, message);
        }
    }

    private void sendWebhook(String url, String username, String message) {
        new Thread(() -> {
            try {
                URL u = new URL(url);
                HttpURLConnection con = (HttpURLConnection) u.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", "Sylvessa/1.0");
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true);

                String json = "{\"username\":\"" + escape(username) + "\"," +
                        "\"avatar_url\":\"https://mc-heads.net/avatar/" + escape(username) + "\"," +
                        "\"content\":\"" + escape(message).replace("@everyone", "BOOYAH!").replace("@here", "BOOYAH!") + "\"}";

                OutputStream os = con.getOutputStream();
                os.write(json.getBytes(StandardCharsets.UTF_8));
                os.flush();
                os.close();

                con.getInputStream().close();
            } catch(Exception e) {
                Log.info("webhook error: " + e);
                for(StackTraceElement el : e.getStackTrace()) {
                    Log.info(el.toString());
                }
            }
        }).start();
    }

    private String escape(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");
    }
}
