package me.partlysunny.vertigrow.util.constants;

import me.partlysunny.vertigrow.screens.IntroScreen;
import me.partlysunny.vertigrow.MainGame;

public final class Screens {

    public static void init(MainGame game) {
        game.getScreenManager().addScreen("intro", new IntroScreen(game));
    }

}
