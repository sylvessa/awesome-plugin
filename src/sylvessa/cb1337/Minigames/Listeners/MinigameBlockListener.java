package sylvessa.cb1337.Minigames.Listeners;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import sylvessa.cb1337.Minigames.Minigame;
import sylvessa.cb1337.Minigames.MinigameManager;

public class MinigameBlockListener extends BlockListener {
    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        Minigame g = MinigameManager.get(event.getPlayer());
        if (MinigameManager.isQueued(event.getPlayer())) event.setCancelled(true);
        if(g != null && !g.canBreak(event.getPlayer(), event)) event.setCancelled(true);
    }

    @Override
    public void onBlockPlace(BlockPlaceEvent e) {
        Minigame g = MinigameManager.get(e.getPlayer());
        if (MinigameManager.isQueued(e.getPlayer())) e.setCancelled(true);
        if(g != null && !g.canPlace(e.getPlayer(), e)) e.setCancelled(true);
    }
}
