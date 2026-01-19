package sylvessa.cb1337.Minigames;

import org.bukkit.entity.Player;
import sylvessa.cb1337.Minigames.Games.GuessTheBuildMinigame;
import sylvessa.cb1337.Minigames.Games.SkywarsMinigame;

import java.util.ArrayList;
import java.util.List;

public enum MinigameType {
    GTB,
    SKYWARS;

    public Minigame create() {
        List<Player> list = new ArrayList<>();
        if(this == GTB) return new GuessTheBuildMinigame(list);
        if(this == SKYWARS) return new SkywarsMinigame(list);
        return null;
    }
}
