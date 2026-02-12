package sylvessa.cb1337.commands;


import org.bukkit.command.CommandSender;
import sylvessa.cb1337.Main;
import sylvessa.cb1337.Teams.TeamManager;
import sylvessa.cb1337.Types.PluginCommand;
import sylvessa.cb1337.Types.Team;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;

@SuppressWarnings("unused")
public class TeamsCommand implements PluginCommand {
    public String name() { return "teams"; }
    public String description() { return "View all teams"; }

    private TeamManager manager = Main.getInstance().getTeamManager();

    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
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

        sender.sendMessage("§7Use §e/team join <tag/name> §7to join a team (Only if its not invite-only).");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            String prefix = args[0].toLowerCase();
            int totalPages = (int) Math.ceil(manager.getTeams().size() / 7.0);
            for (int i = 1; i <= totalPages; i++) {
                String str = String.valueOf(i);
                if (str.startsWith(prefix)) suggestions.add(str);
            }
        }
        return suggestions;
    }
}
