package sylvessa.plugin.Listeners;

import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerChatEvent;
import sylvessa.plugin.Log;
import sylvessa.plugin.Main;
import sylvessa.plugin.UserConfig;
import sylvessa.plugin.Teams.TeamManager;
import sylvessa.plugin.Types.Team;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ChatListener extends PlayerListener {
    public void onPlayerChat(PlayerChatEvent event) {
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
        event.setFormat(displayName + ": " + message);

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
                        "\"content\":\"" + escape(message) + "\"}";

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
