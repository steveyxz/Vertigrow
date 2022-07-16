package me.partlysunny.game.effects.visual;

import java.util.HashMap;
import java.util.Map;

public class VisualEffectManager {

    private static final Map<String, VisualEffect> effects = new HashMap<>();

    public static void registerEffect(String id, VisualEffect effect) {
        effects.put(id, effect);
    }

    public static VisualEffect getEffect(String id) {
        return effects.get(id);
    }

    public static void unregisterEffect(String id) {
        effects.remove(id);
    }

    public static void init() {
    }

    public static void update(float delta) {
        for (VisualEffect e : effects.values()) {
            e.update(delta);
        }
    }
}
