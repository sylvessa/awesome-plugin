package sylvessa.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sylvessa.plugin.Main;
import sylvessa.plugin.Teams.TeamManager;
import sylvessa.plugin.Types.Team;

@SuppressWarnings("unused")
public class TeamCommand implements PluginCommand {
    public String name() { return "team"; }
    public String description() { return "Manage or view teams"; }

    private final TeamManager manager = Main.getInstance().getTeamManager();

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return;
        }
        Player p = (Player) sender;

        if (args.length == 0) {
            Team t = manager.getPlayerTeam(p.getName());
            if (t == null) {
                sender.sendMessage("§7You are not in a team.");
                sender.sendMessage("§7Create one with §e/team create <name>");
                sender.sendMessage("§7Use §e/team help §7for all commands.");
                return;
            }

            showTeamInfo(sender, p, t);
            return;
        }

        String sub = args[0].toLowerCase();

        switch(sub) {
            case "help":
                sendHelp(sender);
                break;

            case "create":
                if (args.length < 2) { sender.sendMessage("§7Usage: /team create <team name>"); return; }
                if (manager.getPlayerTeam(p.getName()) != null) {
                    sender.sendMessage("§cYou are already in a team.");
                    return;
                }
                String teamName = joinArgs(args);
                if (teamName.length() > 32) { sender.sendMessage("§cMax 32 chars."); return; }
                if (manager.getTeam(teamName) != null) { sender.sendMessage("§cTeam exists."); return; }

                Team t = new Team(teamName, p.getName());
                manager.addTeam(t);
                sender.sendMessage("§aTeam §b" + teamName + "§a created! You are the owner.");
                break;

            case "disband":
                Team dt = manager.getPlayerTeam(p.getName());
                if (dt == null || !dt.getOwner().equals(p.getName())) { sender.sendMessage("§cYou do not own a team."); return; }
                manager.removeTeam(dt.getName());
                sender.sendMessage("§aTeam §b" + dt.getName() + "§a disbanded.");
                break;

            case "members":
                Team mt = manager.getPlayerTeam(p.getName());
                if (mt == null) { sender.sendMessage("§cYou are not in a team."); return; }
                sender.sendMessage("§eTeam §b" + mt.getName() + " §7[" + mt.getTag() + "] members:");
                for (String m : mt.getMembers()) sender.sendMessage("§7- " + m);
                break;

            case "settings":
                Team st = manager.getPlayerTeam(p.getName());
                if (st == null || !st.getOwner().equals(p.getName())) {
                    sender.sendMessage("§cYou do not own a team.");
                    return;
                }

                if (args.length < 2) {
                    showAllSettings(sender, st);
                } else {
                    handleSettings(st, sender, args);
                }
                break;

            case "invite":
                Team it = manager.getPlayerTeam(p.getName());
                if (it == null || !it.getOwner().equals(p.getName())) { sender.sendMessage("§cYou do not own a team."); return; }
                if (args.length < 2) { sender.sendMessage("§7Usage: /team invite <player>"); return; }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) { sender.sendMessage("§cPlayer not online."); return; }
                it.invite(target.getName());

                sender.sendMessage("§aInvited §b" + target.getName() + " §ato your team.");

                target.sendMessage("§eYou have been invited to join the team §b" + it.getName());
                target.sendMessage("§7Use §e/team join " + it.getName() + " §7or §e/team join " + it.getTag());
                break;

            case "kick":
                Team kt = manager.getPlayerTeam(p.getName());
                if (kt == null || !kt.getOwner().equals(p.getName())) { sender.sendMessage("§cYou do not own a team."); return; }
                if (args.length < 2) { sender.sendMessage("§7Usage: /team kick <player>"); return; }
                String kicked = args[1];
                if (!kt.isMember(kicked)) { sender.sendMessage("§cPlayer is not in your team."); return; }
                kt.removeMember(kicked);
                sender.sendMessage("§aRemoved §b" + kicked + " §afrom your team.");
                break;

            case "join":
                String input = joinArgs(args);
                Team jt = findTeamByNameOrTag(input);
                if (jt == null) {
                    sender.sendMessage("§cTeam not found by name or tag.");
                    return;
                }
                if (manager.getPlayerTeam(p.getName()) != null) { sender.sendMessage("§cYou are already in a team."); return; }
                if (!jt.isFreeJoin() && !jt.isInvited(p.getName())) { sender.sendMessage("§cTeam is invite-only."); return; }
                jt.addMember(p.getName());
                sender.sendMessage("§aYou joined §b" + jt.getName() + "§a!");
                break;

            case "leave":
                Team lt = manager.getPlayerTeam(p.getName());
                if (lt == null) { sender.sendMessage("§cYou are not in a team."); return; }
                if (lt.getOwner().equals(p.getName())) { sender.sendMessage("§cOwner cannot leave team. Disband instead."); return; }
                lt.removeMember(p.getName());
                sender.sendMessage("§aYou left §b" + lt.getName() + "§a.");
                break;

            default:
                sender.sendMessage("§7Unknown subcommand. Use /team help");
                break;
        }
    }

    private void showAllSettings(CommandSender sender, Team st) {
        sender.sendMessage("§e--- Team Settings for §b" + st.getName() + " §7[" + st.getTag() + "] ---");
        sender.sendMessage("§7Tag: §e" + (st.getTag().isEmpty() ? "None" : st.getTag()));
        sender.sendMessage("§7Color: §" + st.getColor() + st.getColor());
        sender.sendMessage("§7Free Join: §e" + st.isFreeJoin());
        sender.sendMessage("§7Members: §e" + String.join(", ", st.getMembers()));
        sender.sendMessage("§7Invited: §e" + (st.getInvited().isEmpty() ? "None" : String.join(", ", st.getInvited())));
        sender.sendMessage("§7PvP Enabled: §e" + (st.isPvpEnabled() ? "true" : "false"));
        sender.sendMessage("§7Tag Last Changed: §e" + (st.getLastTagChange() == 0 ? "Never" : new java.util.Date(st.getLastTagChange())));

        sender.sendMessage("§e--- Change Settings ---");
        sender.sendMessage("§7/team settings tag <tag>");
        sender.sendMessage("§7/team settings color <0-9a-f>");
        sender.sendMessage("§7/team settings freejoin <true|false>");
        sender.sendMessage("§7/team settings pvp <true|false>");
    }

    private void handleSettings(Team st, CommandSender sender, String[] args) {
        String setting = args[1].toLowerCase();
        switch(setting) {
            case "tag":
                if (args.length < 3) {
                    sender.sendMessage("§7Current tag: " + st.getTag());
                    return;
                }

                String newTag = args[2];

                if (isTagTaken(newTag)) {
                    sender.sendMessage("§cThat tag is already taken by another team.");
                    return;
                }

                if (newTag.length() > 5) {
                    sender.sendMessage("§cTags can only be up to 5 characters long.");
                    return;
                }

                if (st.setTag(newTag)) {
                    sender.sendMessage("§aTag set to §e[" + newTag + "]");
                } else {
                    sender.sendMessage("§cYou can only change the tag once per week.");
                }
                break;

            case "color":
                if (args.length < 3) { sender.sendMessage("§7Current color: " + st.getColor()); return; }
                st.setColor(args[2]);
                sender.sendMessage("§aTeam color set to §" + args[2] + "this color");
                break;
            case "freejoin":
                if (args.length < 3) { sender.sendMessage("§7Current freejoin: " + st.isFreeJoin()); return; }
                st.setFreeJoin(Boolean.parseBoolean(args[2]));
                sender.sendMessage("§aTeam freejoin set to " + args[2]);
                break;
            case "pvp":
                if (args.length < 3) { sender.sendMessage("§7Current PvP: " + st.isPvpEnabled()); return; }
                st.setPvpEnabled(Boolean.parseBoolean(args[2]));
                sender.sendMessage("§aTeam PvP set to " + args[2]);
                break;
            default:
                sender.sendMessage("§7Unknown setting.");
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§e--- Team Commands ---");
        sender.sendMessage("§7/team create <name> §7- create a new team");
        sender.sendMessage("§7/team disband §7- disband your team");
        sender.sendMessage("§7/team members §7- view members of your team");
        sender.sendMessage("§7/team settings <tag|color|freejoin> [value] §7- manage team settings");
        sender.sendMessage("§7/team invite <player> §7- invite player");
        sender.sendMessage("§7/team kick <player> §7- remove player");
        sender.sendMessage("§7/team join <team> §7- join a team");
        sender.sendMessage("§7/team leave §7- leave your team");
        sender.sendMessage("§7/team help §7- show this message");
    }

    private void showTeamInfo(CommandSender sender, Player p, Team t) {
        sender.sendMessage("§e--- Team Info ---");
        sender.sendMessage("§7Name: §b" + t.getName());
        sender.sendMessage("§7Tag: §e" + (t.getTag().isEmpty() ? "None" : "[" + t.getTag() + "]"));
        sender.sendMessage("§7Color: §" + t.getColor() + t.getColor());
        sender.sendMessage("§7Owner: §e" + t.getOwner());
        sender.sendMessage("§7Members (§e" + t.getMembers().size() + "§7): §e" + String.join(", ", t.getMembers()));
        sender.sendMessage("§7Free Join: §e" + t.isFreeJoin());
        sender.sendMessage("§7PvP Enabled: §e" + (t.isPvpEnabled() ? "true" : "false"));

        if (t.getOwner().equals(p.getName())) {
            sender.sendMessage("§e--- Owner Commands ---");
            sender.sendMessage("§7/team settings tag <tag>");
            sender.sendMessage("§7/team settings color <0-9a-f>");
            sender.sendMessage("§7/team settings freejoin <true|false>");
            sender.sendMessage("§7/team settings pvp <true|false>");
            sender.sendMessage("§7/team help - view all other commands");
        } else {
            sender.sendMessage("§7Use §e/team leave §7to leave this team.");
        }
    }

    private Team findTeamByNameOrTag(String input) {
        String needle = input.toLowerCase();

        for (Team t : manager.getTeams()) {
            if (t.getName().toLowerCase().equals(needle)) return t;
            if (!t.getTag().isEmpty() && t.getTag().toLowerCase().equals(needle)) return t;
        }

        return null;
    }

    private String joinArgs(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            if (i > 1) sb.append(" ");
            sb.append(args[i]);
        }
        return sb.toString();
    }

    private boolean isTagTaken(String tag) {
        if (tag.isEmpty()) return false;

        for (Team t : manager.getTeams()) {
            if (!t.getTag().isEmpty() && t.getTag().equalsIgnoreCase(tag)) return true;
        }
        return false;
    }
}
