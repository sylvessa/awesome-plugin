package sylvessa.cb1337.Minigames;

import org.bukkit.entity.Player;
import sylvessa.cb1337.Minigames.Games.GuessTheBuildMinigame;

import java.util.ArrayList;
import java.util.List;

public enum MinigameType {
    GUESS;

    public Minigame create() {
        List<Player> list = new ArrayList<>();
        if(this == GUESS) return new GuessTheBuildMinigame(list);
        return null;
    }
}
