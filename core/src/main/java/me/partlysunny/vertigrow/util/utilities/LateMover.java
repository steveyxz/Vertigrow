package me.partlysunny.vertigrow.util.utilities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import de.damios.guacamole.gdx.pool.Vector2Pool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LateMover {

    private static final Map<Body, Vector2> toRemove = new HashMap<>();
    private static final List<Body> tempRemove = new ArrayList<>();
    private static final Vector2Pool pool = new Vector2Pool();

    public static void tagToMove(Body e, Vector2 whereTo) {
        toRemove.put(e, pool.obtain().set(whereTo));
    }

    public static void tagToMove(Body e, float x, float y) {
        toRemove.put(e, pool.obtain().set(x, y));
    }

    public static void process() {
        tempRemove.clear();
        tempRemove.addAll(toRemove.keySet());
        for (Body e : tempRemove) {
            Vector2 newPos = toRemove.get(e);
            e.setTransform(newPos, 0);
            e.setAwake(true);
            pool.free(newPos);
        }
        toRemove.clear();
    }

}
