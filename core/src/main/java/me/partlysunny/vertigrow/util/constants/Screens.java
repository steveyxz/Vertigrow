package me.partlysunny.vertigrow.util.constants;

import me.partlysunny.vertigrow.Vertigrow;
import me.partlysunny.vertigrow.screens.InGameScreen;
import me.partlysunny.vertigrow.screens.IntroScreen;

public final class Screens {

    public static void init(Vertigrow game) {
        game.getScreenManager().addScreen("intro", new IntroScreen(game));
        game.getScreenManager().addScreen("ingame", new InGameScreen(game));
    }

}
