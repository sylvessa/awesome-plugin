package sylvessa.cb1337.Listeners;

import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;

public class SignColorListener extends BlockListener {
    private String translate(String s) {
        return s.replaceAll("&([0-9a-fk-or])", "§$1");
    }

    public void onSignChange(SignChangeEvent event) {
        for(int i = 0; i < 4; i++) {
            String line = event.getLine(i);
            if(line == null) continue;

            event.setLine(i, translate(line));
        }
    }
}
