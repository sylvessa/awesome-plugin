package sylvessa.cb1337.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;
import sylvessa.cb1337.Log;
import sylvessa.cb1337.Main;
import sylvessa.cb1337.UserConfig;
import sylvessa.cb1337.commands.CreativeCommand;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import static sylvessa.cb1337.Util.Helpers.buildDisplayName;

public class JoinListener extends PlayerListener {
    private final Main plugin;

    public JoinListener(Main plugin) {
        this.plugin = plugin;
    }

    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        String key = name.toLowerCase();

        Main plugin = Main.getInstance();

        UserConfig uc = new UserConfig(player, plugin);
        plugin.getUserConfigs().put(key, uc);

        int joins = uc.getInt("joins", 0);
        uc.set("joins", joins + 1);
        uc.save();

        String displayName = buildDisplayName(name, true);


        event.setJoinMessage(
                displayName +
                        "§e joined the game. §8(Joined " +
                        joins + " time" + (joins != 1 ? "s" : "") + ")§f"
        );

        String webhook = plugin.getPluginConfig().getString("discord.webhook-url", "");
        if(!webhook.isEmpty()) {
            sendJoinLeaveWebhook(webhook, name, true);
        }
    }


    public void onPlayerQuit(PlayerQuitEvent event) {
        String name = event.getPlayer().getName();

        CreativeCommand.returnFromCreative(event.getPlayer());

        Log.info("Saving " + name + "'s data.");

        UserConfig uc = plugin.getUserConfigs().get(name.toLowerCase());
        String color = "f"; // default

        if(uc != null) {
            uc.save();
            color = uc.getString("color", "f");
            //plugin.getUserConfigs().remove(name.toLowerCase());
            Log.info("Saved " + name + "'s data!");
        } else {
            Log.info("No user config found for " + name + ", using default color.");
        }

        event.setQuitMessage("§e" + (!color.equals("f") ? "§" + color : "") + name + "§e has left the game.");

        String webhook = plugin.getPluginConfig().getString("discord.webhook-url", "");
        if(!webhook.isEmpty()) {
            sendJoinLeaveWebhook(webhook, name, false);
        }

//        int online = Bukkit.getOnlinePlayers().length - 1; // lol?
//        BotUtil.updateChannelDescription(online + " player" + (online != 1 ? "s" : "") + " online | Site map: https://map.snep.lol/");
    }

    // PRIVATE
    private void sendJoinLeaveWebhook(String url, String username, boolean joined) {
        new Thread(() -> {
            try {
                URL u = new URL(url);
                HttpURLConnection con = (HttpURLConnection) u.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", "Sylvessa/1.0");
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true);

                String message = joined
                        ? username + " joined the game"
                        : username + " left the game";

                int color = joined ? 65280 : 16711680;

                String json =
                        "{\"username\":\"" + escape(username) + "\"," +
                                "\"avatar_url\":\"https://mc-heads.net/avatar/" + escape(username) + "\"," +
                                "\"embeds\":[{" +
                                "\"description\":\"" + escape(message) + "\"," +
                                "\"color\":" + color +
                                "}]}";

                OutputStream os = con.getOutputStream();
                os.write(json.getBytes(StandardCharsets.UTF_8));
                os.flush();
                os.close();

                con.getInputStream().close();
            } catch(Exception e) {
                Log.info("webhook error: " + e);
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