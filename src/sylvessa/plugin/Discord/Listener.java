package sylvessa.plugin.Discord;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import sylvessa.plugin.Log;

public class Listener extends ListenerAdapter {
    private final String channelId;

    public Listener(String channelId) {
        this.channelId = channelId;
    }

    public void onMessageReceived(MessageReceivedEvent event) {
        if(!event.getChannel().getId().equals(channelId)) return;
        if(event.getAuthor().isBot()) return;

        String user = event.getAuthor().getGlobalName();
        if(user == null || user.isEmpty() || user == "null")
            user = event.getAuthor().getName();

        String content = event.getMessage().getContentDisplay();

        content = content.replaceAll(
                "https?://(cdn\\.discordapp\\.com|media\\.discordapp\\.net|discord\\.com)/\\S+",
                "[Attachment]"
        );

        if(!event.getMessage().getAttachments().isEmpty()) {
            if(!content.contains("[Attachment]")) {
                content = content + " [Attachment]";
            }
        }

        Log.info("[Discord][" + user + "] " + content);

        // this runs on main thread....
        String discordMessage = "§5[Discord] §d" + user + "§f: " + content;
        Bukkit.getServer().broadcastMessage(discordMessage);
    }
}
