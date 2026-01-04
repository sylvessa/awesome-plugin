package sylvessa.plugin.Discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.hooks.InterfacedEventManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.internal.utils.JDALogger;
import sylvessa.plugin.Log;
import sylvessa.plugin.Main;

public class Bot {
    private final Main plugin;
    private JDA jda;

    public Bot(Main plugin) {
        this.plugin = plugin;
    }

    public void start() {
        JDALogger.setFallbackLoggerEnabled(false);

        String token = plugin.getPluginConfig().getString("discord.bot-token", "").trim();
        String logChannel = plugin.getPluginConfig().getString("discord.channel-id", "").trim();
        if(token.isEmpty() || logChannel.isEmpty()) {
            Log.info("Discord bot token or log channel not configured, bot will not start.");
            return;
        }

        new Thread(() -> {
            try {
                Log.info("Starting Discord bot...");

                jda = JDABuilder.createDefault(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                        .setAutoReconnect(true)
                        .setStatus(OnlineStatus.ONLINE)
                        .setEventManager(new InterfacedEventManager())
                        .addEventListeners(new Listener(logChannel))
                        .build();

                jda.awaitReady();
                Log.info("Discord bot online!");

                jda.getTextChannelById(logChannel).sendMessage("**Server online!**").queue(
                        success -> Log.info("Startup message sent to Discord."),
                        error -> Log.info("Failed to send startup message: " + error)
                );
            } catch (Exception e) {
                Log.info("Failed to login Discord bot: " + e);
            }
        }).start();
    }

    public void stop() {
        if(jda != null) {
            jda.shutdown();
            Log.info("Discord bot shut down.");
        }
    }
}
