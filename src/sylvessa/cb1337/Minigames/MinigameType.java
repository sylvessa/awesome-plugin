package sylvessa.cb1337.Minigames;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public enum MinigameType {
    TEST;

    public Minigame create() {
        List<Player> list = new ArrayList<>();
        //if(this == TEST) return new TestMinigame(list);
        return null;
    }
}
