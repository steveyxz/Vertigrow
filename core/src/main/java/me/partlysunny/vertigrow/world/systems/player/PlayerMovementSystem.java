package me.partlysunny.vertigrow.world.systems.player;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import me.partlysunny.vertigrow.effects.sound.SoundEffectManager;
import me.partlysunny.vertigrow.util.constants.GameInfo;
import me.partlysunny.vertigrow.util.constants.Mappers;
import me.partlysunny.vertigrow.world.components.collision.RigidBodyComponent;
import me.partlysunny.vertigrow.world.components.player.PlayerAction;
import me.partlysunny.vertigrow.world.components.player.PlayerControlComponent;
import me.partlysunny.vertigrow.world.components.player.PlayerKeyMap;
import me.partlysunny.vertigrow.world.components.player.state.PlayerState;
import me.partlysunny.vertigrow.world.components.player.state.StateComponent;

import static com.badlogic.gdx.Gdx.input;

public class PlayerMovementSystem extends IteratingSystem {


    public PlayerMovementSystem() {
        super(Family.all(PlayerControlComponent.class, RigidBodyComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlayerControlComponent controller = Mappers.controlMapper.get(entity);
        RigidBodyComponent velocity = Mappers.bodyMapper.get(entity);
        StateComponent state = Mappers.stateMapper.get(entity);

        PlayerKeyMap map = controller.keyMap();
        if (map == null) {
            map = new PlayerKeyMap();
        }

        if (state.state() == PlayerState.DYING.value()) {
            return;
        }
        //Movement speed
        float playerSpeed = 3;
        float jumpSpeed = 40;
        Body body = velocity.rigidBody();
        float x = body.getPosition().x;
        float y = body.getPosition().y;
        Vector2 linearVelocity = body.getLinearVelocity();

        PlayerState value = PlayerState.getFromId(state.state());

        if (value == null) {
            Gdx.app.log("Vertigrow", "Invalid player state? " + state.state() + ", please contact devs!");
            return;
        }

        //If the object is in the air (not grounded)
        if (!value.grounded()) {
            //Check for rise / fall
            if (linearVelocity.y > 0) {
                state.setState(PlayerState.RISING.value());
            } else if (linearVelocity.y < 0) {
                state.setState(PlayerState.FALLING.value());
            } else if (state.state() != PlayerState.RISING.value()) {
                //If it is no longer in the air, change state
                if (linearVelocity.x > 0) {
                    state.setState(PlayerState.MOVING_RIGHT.value());
                } else if (linearVelocity.x < 0) {
                    state.setState(PlayerState.MOVING_LEFT.value());
                }
            }
        } else {
            //Also change state is grounded already
            if (linearVelocity.x > 0) {
                state.setState(PlayerState.MOVING_RIGHT.value());
            } else if (linearVelocity.x < 0) {
                state.setState(PlayerState.MOVING_LEFT.value());
            }
        }

        //If no velocity return to passive state
        if (body.getLinearVelocity().x == 0 && body.getLinearVelocity().y == 0 && state.state() != PlayerState.RISING.value()) {
            state.setState(PlayerState.PASSIVE.value());
        }

        //Key Presses
        if (input.isKeyPressed(map.getKey(PlayerAction.JUMP)) && value.grounded()) {
            body.setLinearVelocity(body.getLinearVelocity().x, jumpSpeed);
            state.setState(PlayerState.RISING.value());
            SoundEffectManager.getSound("jump").play(1);
        }
        if (input.isKeyPressed(map.getKey(PlayerAction.LEFT)) && linearVelocity.x >= -GameInfo.MAX_VELOCITY) {
            body.applyLinearImpulse(-playerSpeed, 0, x, y, true);
            if (state.state() == PlayerState.PASSIVE.value()) {
                state.setState(PlayerState.MOVING_LEFT.value());
            }
        }
        if (input.isKeyPressed(map.getKey(PlayerAction.RIGHT)) && linearVelocity.x <= GameInfo.MAX_VELOCITY) {
            body.applyLinearImpulse(playerSpeed, 0, x, y, true);
            if (state.state() == PlayerState.PASSIVE.value()) {
                state.setState(PlayerState.MOVING_RIGHT.value());
            }
        }
    }
}
