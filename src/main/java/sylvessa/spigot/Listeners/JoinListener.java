package sylvessa.spigot.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import sylvessa.spigot.Log;
import sylvessa.spigot.Main;
import sylvessa.spigot.UserConfig;
import sylvessa.spigot.commands.CreativeCommand;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import static sylvessa.spigot.Util.Helpers.buildDisplayName;

public class JoinListener implements Listener {
//    @EventHandler
//    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
//        //Main.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> SurvivalHelper.handleWorldChange(event), 1L);
//    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // hook
//        Player p = event.getPlayer();
//        CraftPlayer cp = (CraftPlayer)p;
//        EntityPlayer ep = cp.getHandle();
//
//        playerConnection old = ep.playerConnection;
//        ep.playerConnection = new EnchantPreviewListener(old, ep, p);


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

        String displayName2 = buildDisplayName(name, false);
        if(displayName2.length() > 16) displayName2 = displayName2.substring(0, 16);

        event.getPlayer().setPlayerListName(displayName2);

        String webhook = plugin.getPluginConfig().getString("discord.webhook-url", "");
        if(!webhook.isEmpty()) {
            sendJoinLeaveWebhook(webhook, name, true);
        }

        //Main.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> SurvivalHelper.handleJoin(event.getPlayer()), 1L);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        handleLeave(event.getPlayer(), "has left the game.");
    }

    public static void handleLeave(Player player, String message) {
        String name = player.getName();

        CreativeCommand.returnFromCreative(player);

        Log.info("Saving " + name + "'s data.");

        UserConfig uc = Main.getInstance().getUserConfigs().get(name.toLowerCase());
        String color = "f";

        if(uc != null) {
            uc.save();
            color = uc.getString("color", "f");
            Log.info("Saved " + name + "'s data!");
        } else {
            Log.info("No user config found for " + name + ", using default color.");
        }

        if(message != null) {
            player.getServer().broadcastMessage(
                    "§e" + (!color.equals("f") ? "§" + color : "") + name + "§e " + message
            );
        }

        String webhook = Main.getInstance().getPluginConfig().getString("discord.webhook-url", "");
        if(!webhook.isEmpty()) {
            sendJoinLeaveWebhook(webhook, name, false);
        }
    }

    // PRIVATE
    private static void sendJoinLeaveWebhook(String url, String username, boolean joined) {
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

    private static String escape(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");
    }
}