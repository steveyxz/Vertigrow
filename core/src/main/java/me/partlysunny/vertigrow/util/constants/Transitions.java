package me.partlysunny.vertigrow.util.constants;

import de.eskalon.commons.screen.transition.impl.BlendingTransition;
import me.partlysunny.vertigrow.Vertigrow;

public final class Transitions {

    public static void init(Vertigrow shapeWars) {
        shapeWars.getScreenManager().addScreenTransition("blending", new BlendingTransition(shapeWars.batch(), 1));
    }

}
