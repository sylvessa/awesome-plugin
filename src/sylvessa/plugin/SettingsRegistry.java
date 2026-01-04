package sylvessa.plugin;

import sylvessa.plugin.Types.Setting;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SettingsRegistry {
    private static final List<Setting> SETTINGS = new ArrayList<>();

    static {
        SETTINGS.add(new Setting("tpa.enabled", "tpa", "Enable or disable teleport requests", "boolean"));
        SETTINGS.add(new Setting("home.public", "public_home", "Allow others to teleport to your home", "boolean"));
    }

    public static List<Setting> getAll() {
        return Collections.unmodifiableList(SETTINGS);
    }

    public static Setting getByAlias(String alias) {
        return SETTINGS.stream()
                .filter(s -> s.alias.equalsIgnoreCase(alias))
                .findFirst()
                .orElse(null);
    }
}
