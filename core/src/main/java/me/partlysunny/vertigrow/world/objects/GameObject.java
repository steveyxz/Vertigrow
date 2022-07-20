package me.partlysunny.vertigrow.world.objects;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

public interface GameObject {

    Entity build(PooledEngine e, float initialX, float initialY);

    default Entity insert(PooledEngine engine, float initialX, float initialY) {
        Entity build = build(engine, initialX, initialY);
        engine.addEntity(build);
        return build;
    }

}
