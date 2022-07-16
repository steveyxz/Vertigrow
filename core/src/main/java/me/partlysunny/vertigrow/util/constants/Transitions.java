package me.partlysunny.vertigrow.util.constants;

import de.eskalon.commons.screen.transition.impl.BlendingTransition;
import me.partlysunny.vertigrow.MainGame;

public final class Transitions {

    public static void init(MainGame shapeWars) {
        shapeWars.getScreenManager().addScreenTransition("blending", new BlendingTransition(shapeWars.batch(), 1));
    }

}
