package me.partlysunny.vertigrow.world.objects.type;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import me.partlysunny.vertigrow.screens.InGameScreen;
import me.partlysunny.vertigrow.util.utilities.TextureManager;
import me.partlysunny.vertigrow.util.utilities.Util;
import me.partlysunny.vertigrow.world.components.collision.RigidBodyComponent;
import me.partlysunny.vertigrow.world.components.collision.TransformComponent;
import me.partlysunny.vertigrow.world.components.player.PlayerCameraFollowComponent;
import me.partlysunny.vertigrow.world.components.player.PlayerControlComponent;
import me.partlysunny.vertigrow.world.components.player.state.PlayerState;
import me.partlysunny.vertigrow.world.components.player.state.StateComponent;
import me.partlysunny.vertigrow.world.components.render.AnimationComponent;
import me.partlysunny.vertigrow.world.components.render.TextureComponent;
import me.partlysunny.vertigrow.world.objects.GameObject;

public class PlayerObject implements GameObject {
    @Override
    public Entity build(PooledEngine e, float initialX, float initialY) {
        float radius = 6;
        Entity player = e.createEntity();

        TransformComponent transform = e.createComponent(TransformComponent.class);
        transform.init(radius * 2, radius * 2);
        player.add(transform);

        TextureComponent texture = e.createComponent(TextureComponent.class);
        texture.init(new TextureRegion(TextureManager.getTexture("playerPassive")));
        player.add(texture);

        AnimationComponent animation = e.createComponent(AnimationComponent.class);
        animation.animations().put(1, Util.getAnimations(0.1f, 16, 16, 5, "playerMoveRight", Animation.PlayMode.LOOP_PINGPONG));
        animation.animations().put(2, Util.getAnimations(0.1f, 16, 16, 5, "playerMoveLeft", Animation.PlayMode.LOOP_PINGPONG));
        animation.animations().put(3, Util.getAnimations(1, 16, 16, 1, "playerJump", Animation.PlayMode.LOOP_PINGPONG));
        animation.animations().put(4, Util.getAnimations(1, 16, 16, 1, "playerJump", Animation.PlayMode.LOOP_PINGPONG));
        animation.animations().put(5, Util.getAnimations(0.4f, 16, 16, 5, "playerDeath", Animation.PlayMode.NORMAL));
        animation.animations().put(6, Util.getAnimations(0.5f, 16, 16, 2, "playerPassive", Animation.PlayMode.LOOP));
        player.add(animation);

        PlayerCameraFollowComponent cameraFollowComponent = e.createComponent(PlayerCameraFollowComponent.class);
        cameraFollowComponent.init(10);
        player.add(cameraFollowComponent);

        player.add(e.createComponent(PlayerControlComponent.class));

        StateComponent state = e.createComponent(StateComponent.class);
        state.init(PlayerState.PASSIVE.value());
        player.add(state);

        RigidBodyComponent body = e.createComponent(RigidBodyComponent.class);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(radius, radius);
        FixtureDef def = new FixtureDef();
        def.shape = shape;
        def.friction = 0.1f;
        body.initBody(InGameScreen.world.physicsWorld(), initialX, initialY, 0, def, BodyDef.BodyType.DynamicBody, radius);
        player.add(body);

        return player;
    }
}
