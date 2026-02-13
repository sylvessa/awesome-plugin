package sylvessa.spigot.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import sylvessa.spigot.Log;
import sylvessa.spigot.Main;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DeathListener implements Listener {

    @EventHandler
    public void onEntityDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();

        String msg = event.getDeathMessage();
        if (msg == null) return;

        String world = p.getWorld().getName();

        if (world.equals("world") || world.equals("world_nether") || world.equals("world_the_end")) {
            String webhook = Main.getInstance().getPluginConfig().getString("discord.webhook-url", "");
            if (!webhook.isEmpty()) {
                sendDeathWebhook(webhook, stripColorCodes(msg), p.getName());
            }
        } else {
            event.setDeathMessage(null);
        }
    }

    private void sendDeathWebhook(String url, String message, String playerName) {
        new Thread(() -> {
            try {
                URL u = new URL(url);
                HttpURLConnection con = (HttpURLConnection) u.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", "Sylvessa/1.0");
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true);

                int color = 16711680;

                String json =
                        "{\"username\":\"" + escape(playerName) + "\"," +
                                "\"avatar_url\":\"https://mc-heads.net/avatar/" + escape(playerName) + "\"," +
                                "\"embeds\":[{" +
                                "\"description\":\"" + escape(message) + "\"," +
                                "\"color\":" + color +
                                "}]}";

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
