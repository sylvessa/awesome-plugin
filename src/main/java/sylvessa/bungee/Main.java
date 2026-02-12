package sylvessa.bungee;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;
import sylvessa.bungee.Listeners.ServerKickListener;

public class Main extends Plugin {
    @Override
    public void onEnable() {
        BungeeCord.getInstance().getPluginManager().registerListener(new ServerKickListener());
        Log.info("Up!");
    }
}
