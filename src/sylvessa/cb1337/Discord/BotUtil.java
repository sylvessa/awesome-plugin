package sylvessa.cb1337.Discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import sylvessa.cb1337.Log;
import sylvessa.cb1337.Main;

public class BotUtil {

    private static Main plugin;
    private static JDA jda;
    private static String channelId;

    public static void init(Main pluginInstance, JDA jdaInstance) {
        plugin = pluginInstance;
        jda = jdaInstance;
        if (plugin != null) channelId = plugin.getPluginConfig().getString("discord.channel-id", "").trim();
    }

    public static void sendBotMessage(String message) {
        if (jda == null || channelId == null || channelId.isEmpty()) return;

        TextChannel channel = jda.getTextChannelById(channelId);
        if (channel == null) {
            Log.info("BotMessageCreator: could not find channel with ID " + channelId);
            return;
        }

        channel.sendMessage(message).queue(
                success -> Log.info("Bot message sent: " + message),
                error -> Log.info("Failed to send bot message: " + error)
        );
    }

    public static void sendBotMessage(String format, Object... args) {
        sendBotMessage(String.format(format, args));
    }

    public static void updateChannelDescription(String description) {
        if (jda == null || channelId == null || channelId.isEmpty()) return;

        TextChannel channel = jda.getTextChannelById(channelId);
        if (channel == null) {
            Log.info("BotUtil: could not find channel with ID " + channelId);
            return;
        }

        channel.getManager().setTopic(description).queue(
                success -> Log.info("Channel description updated"),
                error -> Log.info("Failed to update channel description: " + error)
        );
    }

}
