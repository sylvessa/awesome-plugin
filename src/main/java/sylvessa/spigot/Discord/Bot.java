package sylvessa.spigot.Discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.hooks.InterfacedEventManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import sylvessa.spigot.Discord.Events.MessageListener;
import sylvessa.spigot.Log;
import sylvessa.spigot.Main;

public class Bot {
    private final Main plugin;
    private JDA jda;

    public Bot(Main plugin) {
        this.plugin = plugin;
    }

    public void start() {

        String token = plugin.getPluginConfig().getString("discord.bot-token", "").trim();
        String logChannel = plugin.getPluginConfig().getString("discord.channel-id", "").trim();
        if(token.isEmpty() || logChannel.isEmpty()) {
            Log.info("Discord bot token or log channel not configured, bot will not start.");
            return;
        }

        new Thread(() -> {
            try {
                Log.info("Starting Discord bot...");

                CommandManager manager = new CommandManager();

                jda = JDABuilder.createDefault(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                        .setAutoReconnect(true)
                        .setStatus(OnlineStatus.ONLINE)
                        .setEventManager(new InterfacedEventManager())
                        .addEventListeners(
                                new MessageListener(logChannel),
                                manager
                        )
                        .build();

                jda.awaitReady();
                Log.info("Discord bot online!");
                manager.registerToDiscord(jda);

                BotUtil.init(Main.getInstance(), jda);

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
            String logChannel = plugin.getPluginConfig().getString("discord.channel-id", "").trim();
            if(!logChannel.isEmpty()) {
                jda.getTextChannelById(logChannel).sendMessage("**Server shutting down..**").queue(
                        success -> Log.info("Startup message sent to Discord."),
                        error -> Log.info("Failed to send startup message: " + error)
                );
            }

            jda.shutdown();
            Log.info("Discord bot shut down.");
        }
    }
}