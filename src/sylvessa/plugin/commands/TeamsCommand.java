package sylvessa.plugin.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.plugin.Main;
import sylvessa.plugin.Teams.TeamManager;
import sylvessa.plugin.Types.Team;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class TeamsCommand implements PluginCommand {
    public String name() { return "teams"; }
    public String description() { return "View all teams"; }

    private TeamManager manager = Main.getInstance().getTeamManager();

    public void execute(CommandSender sender, String[] args) {
        int page = 1;
        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
        }

        List<Team> teams = new ArrayList<>(manager.getTeams());
        if (teams.isEmpty()) {
            sender.sendMessage("§7No teams have been created yet.");
            return;
        }

        int perPage = 7;
        int totalPages = (int) Math.ceil(teams.size() / (double) perPage);
        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        sender.sendMessage("§e--- Teams §7(Page " + page + "/" + totalPages + ") ---");

        int start = (page - 1) * perPage;
        int end = Math.min(start + perPage, teams.size());

        for (int i = start; i < end; i++) {
            Team t = teams.get(i);
            String color = t.getColor() != null ? t.getColor() : "f";
            sender.sendMessage("§7" + t.getName() + " §" + color + "[" + t.getTag() + "] §f- Owner: §b" + t.getOwner() +
                    " §7| Members: §e" + t.getMembers().size());
        }

        if (totalPages > 1) {
            sender.sendMessage("§7Use §e/teams <page> §7to view more.");
        }
    }
}
