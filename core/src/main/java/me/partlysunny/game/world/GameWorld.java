package me.partlysunny.game.world;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import me.partlysunny.game.util.classes.ContactDispatcher;
import me.partlysunny.game.util.constants.Mappers;
import me.partlysunny.game.world.components.collision.RigidBodyComponent;
import me.partlysunny.game.world.components.collision.TransformComponent;
import me.partlysunny.game.world.systems.physics.PhysicsSystem;
import me.partlysunny.game.world.systems.player.CameraFollowingSystem;
import me.partlysunny.game.world.systems.player.PlayerMovementSystem;
import me.partlysunny.game.world.systems.render.ActorUpdatingSystem;
import me.partlysunny.game.world.systems.render.AnimationSystem;
import me.partlysunny.game.world.systems.render.TextureRenderingSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameWorld {

    private final PooledEngine gameWorld;
    private final World physicsWorld;

    private final Map<Body, Entity> bodyCache = new HashMap<>();

    public GameWorld(Stage stage) {
        this.physicsWorld = new World(new Vector2(0, -1), true);
        ContactDispatcher.init(physicsWorld);
        this.gameWorld = new PooledEngine(100, 10000, 10000, 10000000);
        //Mechanics
        //Physics
        gameWorld.addSystem(new PhysicsSystem(physicsWorld));
        //Player systems
        gameWorld.addSystem(new CameraFollowingSystem(stage.getCamera()));
        gameWorld.addSystem(new PlayerMovementSystem());
        //Rendering systems
        gameWorld.addSystem(new AnimationSystem());
        gameWorld.addSystem(new TextureRenderingSystem(stage.getBatch(), stage.getCamera()));
        gameWorld.addSystem(new ActorUpdatingSystem(stage.getBatch()));

    }

    public Entity getEntityWithRigidBody(Body b) {
        if (bodyCache.containsKey(b)) {
            if (bodyCache.get(b) != null) {
                return bodyCache.get(b);
            } else {
                bodyCache.remove(b);
            }
        }
        for (Entity entity : gameWorld.getEntitiesFor(Family.all(RigidBodyComponent.class).get()).toArray(Entity.class)) {
            RigidBodyComponent rigidBodyComponent = Mappers.bodyMapper.get(entity);
            if (rigidBodyComponent.rigidBody().equals(b)) {
                gameWorld.addEntityListener(new EntityListener() {
                    @Override
                    public void entityAdded(Entity entity) {
                    }

                    @Override
                    public void entityRemoved(Entity e) {
                        if (entity.equals(e)) {
                            bodyCache.remove(b);
                        }
                    }
                });
                bodyCache.put(b, entity);
                return entity;
            }
        }
        return null;
    }

    public PooledEngine gameWorld() {
        return gameWorld;
    }

    public World physicsWorld() {
        return physicsWorld;
    }

    public List<Entity> getEntitiesAroundPosition(float x, float y, float distance, boolean meters) {
        ImmutableArray<Entity> entities = gameWorld.getEntitiesFor(Family.all(TransformComponent.class).get());
        List<Entity> returned = new ArrayList<>();
        float actualX = x;
        float actualY = y;
        if (!meters) {
            actualX = TextureRenderingSystem.pixelsToMeters(x);
            actualY = TextureRenderingSystem.pixelsToMeters(y);
        }
        for (Entity e : entities) {
            TransformComponent transform = Mappers.transformMapper.get(e);
            float xDiff = actualX - transform.position.x;
            float yDiff = actualY - transform.position.y;
            float dist = (float) Math.sqrt(xDiff * xDiff + yDiff * yDiff);

            if (dist <= distance) {
                returned.add(e);
            }
        }
        return returned;
    }
}
