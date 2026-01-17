package sylvessa.cb1337.Duels.Listeners;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import sylvessa.cb1337.Duels.DuelGame;
import sylvessa.cb1337.Duels.DuelManager;

public class DuelBlockListener extends BlockListener {
    public void onBlockBreak(BlockBreakEvent e) {
        DuelGame g = DuelManager.get(e.getPlayer());
        if(g != null && !g.canBreak(e.getPlayer(), e)) e.setCancelled(true);
    }

    public void onBlockPlace(BlockPlaceEvent e) {
        DuelGame g = DuelManager.get(e.getPlayer());
        if(g != null && !g.canPlace(e.getPlayer(), e)) e.setCancelled(true);
    }
}