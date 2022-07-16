package me.partlysunny.game.util.utilities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.physics.box2d.World;
import me.partlysunny.game.util.constants.Mappers;
import me.partlysunny.game.world.GameWorld;

import java.util.ArrayList;
import java.util.List;

public class LateRemover {

    private static final List<Entity> toRemove = new ArrayList<>();
    private static final List<Entity> tempRemove = new ArrayList<>();

    public static void tagToRemove(Entity e) {
        toRemove.add(e);
    }

    public static void process(GameWorld baseWorld) {
        PooledEngine world = baseWorld.gameWorld();
        World physics = baseWorld.physicsWorld();
        tempRemove.clear();
        tempRemove.addAll(toRemove);
        for (Entity e : tempRemove) {
            if (Mappers.bodyMapper.has(e)) {
                physics.destroyBody(Mappers.bodyMapper.get(e).rigidBody());
            }
            if (Mappers.deleteListenerMapper.has(e)) {
                Mappers.deleteListenerMapper.get(e).onDelete().accept(e);
            }
            world.removeEntity(e);
        }
        toRemove.clear();
    }

}
