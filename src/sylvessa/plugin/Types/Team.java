package sylvessa.plugin.Types;

import org.bukkit.util.config.Configuration;
import sylvessa.plugin.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Team {
    private final Configuration config;
    public final File file;

    private final String name;
    private String tag;
    private final String owner;
    private String color;
    private boolean freeJoin;
    private final List<String> members;
    private final List<String> invited;
    private boolean pvpEnabled;
    private long lastTagChange;

    public Team(File file) {
        this.file = file;
        this.config = new Configuration(file);
        config.load();

        this.name = config.getString("name", "Unnamed Team");
        this.tag = config.getString("tag", "");
        this.owner = config.getString("owner", "");
        this.color = config.getString("color", "f");
        this.freeJoin = config.getBoolean("freeJoin", false);
        this.members = config.getStringList("members", new ArrayList<>());
        this.invited = config.getStringList("invited", new ArrayList<>());
        this.pvpEnabled = config.getBoolean("pvp", false);

        String lastTag = config.getString("lastTagChange", "0");
        try { this.lastTagChange = Long.parseLong(lastTag); } catch(Exception e) { this.lastTagChange = 0; }
    }

    public Team(String name, String owner) {
        this.name = name;
        this.owner = owner;
        this.tag = "";
        this.color = "f";
        this.freeJoin = false;
        this.members = new ArrayList<>();
        this.members.add(owner);
        this.invited = new ArrayList<>();
        this.lastTagChange = 0;
        this.pvpEnabled = false;

        File folder = new File(Main.getInstance().getDataFolder(), "/teams");
        if(!folder.exists()) folder.mkdirs();
        this.file = new File(folder, name + ".yml");
        this.config = new Configuration(file);

        save();
    }

    public void save() {
        config.setProperty("name", name);
        config.setProperty("tag", tag);
        config.setProperty("owner", owner);
        config.setProperty("color", color);
        config.setProperty("freeJoin", freeJoin);
        config.setProperty("members", members);
        config.setProperty("pvp", pvpEnabled);
        config.setProperty("invited", invited);
        config.setProperty("lastTagChange", String.valueOf(lastTagChange));
        config.save();
    }

    public String getName() { return name; }
    public String getTag() { return tag; }

    public boolean setTag(String newTag) {
        long now = System.currentTimeMillis();
        if (now - lastTagChange < 7L * 24 * 60 * 60 * 1000) return false; // 1 week cooldown
        this.tag = newTag;
        this.lastTagChange = now;
        save();
        return true;
    }

    public String getOwner() { return owner; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; save(); }
    public boolean isFreeJoin() { return freeJoin; }
    public void setFreeJoin(boolean freeJoin) { this.freeJoin = freeJoin; save(); }
    public boolean isPvpEnabled() { return pvpEnabled; }
    public void setPvpEnabled(boolean pvpEnabled) { this.pvpEnabled = pvpEnabled; save(); }

    public List<String> getMembers() { return members; }
    public void addMember(String player) {
        if(!members.contains(player)) {
            members.add(player);
            invited.remove(player);
            save();
        }
    }
    public void removeMember(String player) {
        if(members.contains(player)) {
            members.remove(player);
            save();
        }
    }
    public boolean isMember(String player) { return members.contains(player); }

    public void invite(String player) {
        if(!invited.contains(player) && !members.contains(player)) {
            invited.add(player);
            save();
        }
    }

    public List<String> getInvited() {
        return invited;
    }

    public boolean isInvited(String player) {
        return invited.contains(player);
    }

    public long getLastTagChange() { return lastTagChange; }
}
