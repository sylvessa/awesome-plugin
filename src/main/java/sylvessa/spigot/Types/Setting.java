package sylvessa.spigot.Types;

public class Setting {
    public final String key;
    public final String alias;
    public final String description;
    public final String type;

    public Setting(String key, String alias, String description, String type) {
        this.key = key;
        this.alias = alias.toLowerCase();
        this.description = description;
        this.type = type;
    }
}