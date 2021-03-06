package me.partlysunny.vertigrow.world.components.collision;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.steer.SteerableAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import me.partlysunny.vertigrow.util.utilities.Util;

public class RigidBodyComponent implements Component, Pool.Poolable {

    private Body rigidBody = null;

    public RigidBodyComponent() {
    }

    public void initBody(World physicsWorld, float x, float y, float rotation, FixtureDef fixture, BodyDef.BodyType type, float radius) {
        initBody(physicsWorld, x, y, rotation, fixture, type, radius, false);
    }

    public void initBody(World physicsWorld, float x, float y, float rotation, FixtureDef fixture, BodyDef.BodyType type, float radius, boolean rotationFixed) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type;
        bodyDef.position.set(x, y);
        bodyDef.angle = rotation * MathUtils.degreesToRadians;
        rigidBody = physicsWorld.createBody(bodyDef);
        rigidBody.setFixedRotation(rotationFixed);

        rigidBody.setUserData(new SteerableAdapter<Vector2>() {
            @Override
            public Vector2 getPosition() {
                return rigidBody.getPosition();
            }

            @Override
            public float getOrientation() {
                return rigidBody.getAngle();
            }

            @Override
            public Vector2 getLinearVelocity() {
                return rigidBody.getLinearVelocity();
            }

            @Override
            public float getAngularVelocity() {
                return rigidBody.getAngularVelocity();
            }

            @Override
            public float getBoundingRadius() {
                return radius;
            }

            @Override
            public float vectorToAngle(Vector2 vector) {
                return Util.vectorToAngle(vector);
            }

            @Override
            public Vector2 angleToVector(Vector2 outVector, float angle) {
                return Util.angleToVector(outVector, angle);
            }
        });

        rigidBody.createFixture(fixture);
        fixture.shape.dispose();
    }

    public Body rigidBody() {
        return rigidBody;
    }

    @Override
    public void reset() {
        rigidBody = null;
    }
}
