package me.partlysunny.vertigrow.world.objects;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

public interface GameObject {

    Entity build(PooledEngine e, float initialX, float initialY);

    default void insert(PooledEngine engine, float initialX, float initialY) {
        engine.addEntity(build(engine, initialX, initialY));
    }

}
