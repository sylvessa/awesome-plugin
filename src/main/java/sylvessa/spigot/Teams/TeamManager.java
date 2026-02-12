package sylvessa.spigot.Teams;

import sylvessa.spigot.Main;
import sylvessa.spigot.Types.Team;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TeamManager {
    private final Main plugin;
    private final Map<String, Team> teams = new HashMap<>();

    public TeamManager(Main plugin) {
        this.plugin = plugin;
        loadAll();
    }

    public void loadAll() {
        File folder = new File(plugin.getDataFolder(), "teams");
        if(!folder.exists()) folder.mkdirs();

        for(File f : folder.listFiles()) {
            if(!f.getName().endsWith(".yml")) continue;
            Team t = new Team(f);
            teams.put(t.getName().toLowerCase(), t);
        }
    }

    public Team getTeam(String name) {
        return teams.get(name.toLowerCase());
    }

    public void addTeam(Team team) {
        teams.put(team.getName().toLowerCase(), team);
        team.save();
    }

    public void removeTeam(String name) {
        Team t = teams.remove(name.toLowerCase());
        if(t != null && t.file.exists()) t.file.delete();
    }

    public Collection<Team> getTeams() { return teams.values(); }

    public Team getPlayerTeam(String player) {
        for(Team t : teams.values()) if(t.isMember(player)) return t;
        return null;
    }
}
