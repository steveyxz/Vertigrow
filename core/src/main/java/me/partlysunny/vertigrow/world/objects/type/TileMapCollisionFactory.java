package me.partlysunny.vertigrow.world.objects.type;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import me.partlysunny.vertigrow.screens.InGameScreen;
import me.partlysunny.vertigrow.world.components.collision.RigidBodyComponent;

public class TileMapCollisionFactory {

    public static Entity create(float initialX, float initialY, FixtureDef fixture) {
        PooledEngine e = InGameScreen.world.gameWorld();

        Entity tile = e.createEntity();

        RigidBodyComponent body = e.createComponent(RigidBodyComponent.class);
        body.initBody(InGameScreen.world.physicsWorld(), initialX, initialY, 0, fixture, BodyDef.BodyType.KinematicBody, 0);
        tile.add(body);

        return tile;
    }
}
