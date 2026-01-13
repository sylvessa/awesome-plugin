// UNUSED

package sylvessa.plugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import sylvessa.plugin.Log;
import sylvessa.plugin.Main;
import sylvessa.plugin.Types.FakePlayer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;

public class FakePlayerClickListener implements Listener {

    private final Map<Integer, FakePlayer> npcs = new ConcurrentHashMap<>();

    public FakePlayerClickListener() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    public void registerNPC(FakePlayer npc) {
        npcs.put(npc.npc.id, npc);
    }

    public void unregisterNPC(FakePlayer npc) {
        npcs.remove(npc.npc.id);
    }

//    @EventHandler
//    public void onRightClick(PlayerInteractEntityEvent e) {
//        int id = e.getRightClicked().getEntityId();
//        Log.info(Integer.toString(id));
//        FakePlayer npc = npcs.get(id);
//        if (npc != null) {
//            e.setCancelled(true);
//            for (FakePlayer.ClickCallback cb : npc.clickCallbacks) {
//                cb.onClick(e.getPlayer(), npc);
//            }
//        }
//    }
}
