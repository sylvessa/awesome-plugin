package sylvessa.spigot.Util;

import sylvessa.spigot.Log;
import sylvessa.spigot.Main;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DiscordWebhook {

    private final String webhookUrl;
    private String username;
    private String avatarUrl;

    public DiscordWebhook() {
        this.webhookUrl = Main.getInstance().getPluginConfig().getString("discord.webhook-url", "");
        this.username = "Minecraft";
        this.avatarUrl = "https://mc-heads.net/avatar/";
    }

    public DiscordWebhook setUsername(String username) {
        this.username = username;
        return this;
    }

    public DiscordWebhook setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public void sendMessage(String message, int color) {
        if (webhookUrl.isEmpty()) return;

        String cleanMessage = stripColorCodes(message);

        new Thread(() -> {
            try {
                URL url = new URL(webhookUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", "Sylvessa/1.0");
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true);

                String json = "{"
                        + "\"username\":\"" + escape(username) + "\","
                        + "\"avatar_url\":\"" + escape(avatarUrl) + "\","
                        + "\"embeds\":[{"
                        + "\"description\":\"" + escape(cleanMessage) + "\","
                        + "\"color\":" + color
                        + "}]}";

                OutputStream os = con.getOutputStream();
                os.write(json.getBytes(StandardCharsets.UTF_8));
                os.flush();
                os.close();

                con.getInputStream().close();
            } catch (Exception e) {
                Log.info("webhook error: " + e);
            }
        }).start();
    }

    private String stripColorCodes(String s) {
        return s.replaceAll("§.", "");
    }

    private String escape(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");
    }
}
