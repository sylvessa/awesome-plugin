package sylvessa.cb1337.Listeners.Lobby;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerPortalEvent;
import sylvessa.cb1337.Minigames.MinigameManager;
import sylvessa.cb1337.Minigames.MinigameType;
import sylvessa.cb1337.Util.SurvivalHelper;

public class LobbyPlayerListener extends PlayerListener {
    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            // this shopuld ONLY work in lobby/any minigame
            Block block = event.getClickedBlock();
            if(block == null) return;
            if(!(block.getState() instanceof Sign)) return;

            Player player = event.getPlayer();
            Sign sign = (Sign) block.getState();

            String[] lines = sign.getLines();
            switch(player.getWorld().getName()) {
                case "lobby":
                    // lil bro in lobby
                    switch(lines[0]) {
                        case "SKYWARS":
                            MinigameManager.queue(player, MinigameType.SKYWARS);
                            break;
                        case "GUESS THE BUILD":
                            MinigameManager.queue(player, MinigameType.GTB);
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
            //player.sendMessage("you clicked a sign");
        }
    }

    @Override
    public void onPlayerPortal(PlayerPortalEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld().getName().equals("lobby")) {
            event.setCancelled(true);
            World target = Bukkit.getWorld("world");

            SurvivalHelper.ignoreNextWorldChange(player);
            player.teleport(target.getSpawnLocation());
            SurvivalHelper.enterSurvival(player);
        }
    }
}
