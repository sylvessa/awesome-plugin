package sylvessa.spigot.Discord.Commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import sylvessa.spigot.Discord.Types.SlashCommand;

public class PingCommand implements SlashCommand {
    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public SlashCommandData getCommandData() {
        return Commands.slash("ping", "Replies with pong");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.reply("pong").queue();
    }
}
