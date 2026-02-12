package sylvessa.bungee;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {
    @Override
    public void onEnable() {
        BungeeCord.getInstance().getLogger().info("IT WORKS");
    }
}
