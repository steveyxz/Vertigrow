package me.partlysunny.vertigrow.world.systems.physics;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import de.damios.guacamole.gdx.pool.Vector2Pool;
import me.partlysunny.vertigrow.util.constants.Mappers;
import me.partlysunny.vertigrow.util.utilities.LateMover;
import me.partlysunny.vertigrow.world.components.collision.RigidBodyComponent;
import me.partlysunny.vertigrow.world.components.tile.MovementComponent;

import static me.partlysunny.vertigrow.util.utilities.Util.GLOBAL_POOL;

public class BodyMovementSystem extends IteratingSystem {

    private final Vector2 v = new Vector2();

    public BodyMovementSystem() {
        super(Family.all(MovementComponent.class, RigidBodyComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MovementComponent movement = Mappers.movementMapper.get(entity);
        RigidBodyComponent body = Mappers.bodyMapper.get(entity);

        //Basic delay stuff
        if (movement.delayTimer() >= 0) {
            movement.setDelayTimer(movement.delayTimer() - deltaTime);
            if (movement.delayTimer() <= 0) {
                movement.setDelayTimer(-1);
            }
            return;
        }

        v.set(body.rigidBody().getPosition());
        //Calculate the real distance between start and end, note no sqrt cuz there's no difference
        float realDiffX = movement.endPosition().x - movement.startPosition().x;
        float realDiffY = movement.endPosition().y - movement.startPosition().y;
        float realMovementDist = realDiffX * realDiffX + realDiffY * realDiffY;

        //Calculate the current distance between start and current position
        float currentDiffX = v.x - movement.startPosition().x;
        float currentDiffY = v.y - movement.startPosition().y;
        float currentMovementDist = currentDiffX * currentDiffX + currentDiffY * currentDiffY;

        //If the current distance is further to the start, it means the platform needs to move back to start position
        if (currentMovementDist > realMovementDist) {
            movement.setMovingToEnd(false);
            movement.setDelayTimer(movement.delay());
            LateMover.tagToMove(body.rigidBody(), movement.endPosition());
        } else {
            //This means now we have to check if the platform is still in a good position or if it's going backwards too far
            //This is done by first getting the difference between the end platform and the start platform
            v.set(movement.endPosition());
            v.sub(movement.startPosition());
            //Check positives
            boolean xPositive = v.x >= 0;
            boolean yPositive = v.y >= 0;
            //If the values are both
            boolean xValid = false;
            boolean yValid = false;
            if (xPositive && currentDiffX >= 0) {
                xValid = true;
            }
            if (!xPositive && currentDiffX <= 0) {
                xValid = true;
            }
            if (yPositive && currentDiffY >= 0) {
                yValid = true;
            }
            if (!yPositive && currentDiffY <= 0) {
                yValid = true;
            }
            if (xValid && yValid) {
                v.set(0, 0);
                movement.type().getEndPositionWithoutCopy(v, movement.speed() * deltaTime);
                if (!movement.movingToEnd()) {
                    v.scl(-1);
                }
                LateMover.tagToMove(body.rigidBody(), v.add(body.rigidBody().getPosition()));
            } else {
                movement.setMovingToEnd(true);
                movement.setDelayTimer(movement.delay());
                LateMover.tagToMove(body.rigidBody(), movement.startPosition());
            }

        }


    }
}
