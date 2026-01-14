package sylvessa.plugin.Minigames;

import org.bukkit.entity.Player;
import sylvessa.plugin.Minigames.Games.TestMinigame;

import java.util.ArrayList;
import java.util.List;

public enum MinigameType {
    TEST;

    public Minigame create() {
        List<Player> list = new ArrayList<>();
        if(this == TEST) return new TestMinigame(list);
        return null;
    }
}
