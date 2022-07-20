package me.partlysunny.vertigrow.world.objects.type;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import me.partlysunny.vertigrow.screens.InGameScreen;
import me.partlysunny.vertigrow.world.components.collision.CheckpointComponent;
import me.partlysunny.vertigrow.world.components.collision.KillPlayerOnTouchComponent;
import me.partlysunny.vertigrow.world.components.collision.RigidBodyComponent;

public class TileMapCollisionFactory {

    public static Entity create(float initialX, float initialY, FixtureDef fixture, boolean checkpoint, boolean killPlayer) {
        PooledEngine e = InGameScreen.world.gameWorld();

        Entity tile = e.createEntity();

        RigidBodyComponent body = e.createComponent(RigidBodyComponent.class);
        body.initBody(InGameScreen.world.physicsWorld(), initialX, initialY, 0, fixture, BodyDef.BodyType.KinematicBody, 0);
        tile.add(body);

        if (checkpoint) {
            CheckpointComponent c = e.createComponent(CheckpointComponent.class);
            c.init(new Vector2(initialX, initialY + 16));
            tile.add(c);
        }

        if (killPlayer) {
            KillPlayerOnTouchComponent killPlayerComponent = e.createComponent(KillPlayerOnTouchComponent.class);
            tile.add(killPlayerComponent);
        }

        e.addEntity(tile);

        return tile;
    }
}
