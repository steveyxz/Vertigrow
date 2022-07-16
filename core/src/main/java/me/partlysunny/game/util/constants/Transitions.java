package me.partlysunny.game.util.constants;

import de.eskalon.commons.screen.transition.impl.BlendingTransition;
import me.partlysunny.game.MainGame;

public final class Transitions {

    public static void init(MainGame shapeWars) {
        shapeWars.getScreenManager().addScreenTransition("blending", new BlendingTransition(shapeWars.batch(), 1));
    }

}
