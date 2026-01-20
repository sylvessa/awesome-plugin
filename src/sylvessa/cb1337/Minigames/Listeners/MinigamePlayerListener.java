package sylvessa.cb1337.Minigames.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.player.*;
import sylvessa.cb1337.Minigames.Minigame;
import sylvessa.cb1337.Minigames.MinigameManager;

public class MinigamePlayerListener extends PlayerListener {
    @Override
    public void onPlayerMove(PlayerMoveEvent e) {
        Minigame g = MinigameManager.get(e.getPlayer());
        Minigame q = MinigameManager.getQueued(e.getPlayer());
        if(g != null) g.onMove(e.getPlayer());
        if(q != null) q.onMoveInQueue(e.getPlayer());
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        Minigame g = MinigameManager.get(p);
        if(g != null) {
            g.onQuit(p);
        }

        MinigameManager.remove(p);
    }

    @Override
    public void onPlayerChat(PlayerChatEvent e) {
        Minigame g = MinigameManager.get(e.getPlayer());
        if (g != null) g.onChat(e.getPlayer(), e);
    }

    @Override
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent e) {
        Minigame g = MinigameManager.get(e.getPlayer());
        if (g != null) {
            e.setCancelled(true);
        }
    }
}
