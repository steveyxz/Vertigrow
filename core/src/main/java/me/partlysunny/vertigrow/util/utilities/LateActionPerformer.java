package me.partlysunny.vertigrow.util.utilities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.physics.box2d.World;
import me.partlysunny.vertigrow.util.constants.Mappers;
import me.partlysunny.vertigrow.world.GameWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LateActionPerformer {

    private static final List<Runnable> toRemove = new ArrayList<>();
    private static final List<Runnable> tempRemove = new ArrayList<>();

    public static void addRun(Runnable e) {
        toRemove.add(e);
    }

    public static void process() {
        tempRemove.clear();
        tempRemove.addAll(toRemove);
        for (Runnable e : tempRemove) {
            e.run();
        }
        toRemove.clear();
    }

}
