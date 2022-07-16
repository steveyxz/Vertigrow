package me.partlysunny.game.util.constants;

import me.partlysunny.game.screens.IntroScreen;
import me.partlysunny.game.MainGame;

public final class Screens {

    public static void init(MainGame game) {
        game.getScreenManager().addScreen("intro", new IntroScreen(game));
    }

}
