package me.partlysunny.vertigrow.util.utilities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import me.partlysunny.vertigrow.util.constants.Mappers;
import me.partlysunny.vertigrow.world.GameWorld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LateMover {

    private static final Map<Body, Vector2> toRemove = new HashMap<>();
    private static final List<Body> tempRemove = new ArrayList<>();

    public static void tagToMove(Body e, Vector2 whereTo) {
        toRemove.put(e, whereTo);
    }

    public static void process() {
        tempRemove.clear();
        tempRemove.addAll(toRemove.keySet());
        for (Body e : tempRemove) {
            e.setTransform(toRemove.get(e), 0);
        }
        toRemove.clear();
    }

}
