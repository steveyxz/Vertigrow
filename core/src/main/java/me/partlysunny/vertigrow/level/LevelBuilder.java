package me.partlysunny.vertigrow.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import me.partlysunny.vertigrow.screens.InGameScreen;
import me.partlysunny.vertigrow.util.utilities.TextureManager;
import me.partlysunny.vertigrow.util.utilities.Util;
import me.partlysunny.vertigrow.world.components.collision.RigidBodyComponent;
import me.partlysunny.vertigrow.world.components.collision.TransformComponent;
import me.partlysunny.vertigrow.world.components.render.TextureComponent;
import me.partlysunny.vertigrow.world.components.tile.*;
import me.partlysunny.vertigrow.world.objects.type.TileMapCollisionFactory;

import java.util.Arrays;
import java.util.List;

public class LevelBuilder {


    private final LevelManager levelManager;

    public LevelBuilder(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    public void build(TiledMap map) {
        MapObjects collisions = map.getLayers().get("Collisions").getObjects();
        //Make all flags have animations
        Util.makeTilesAnimated("Environment", "Level", 15, map, 0.3f, 15, 23, 31, 39);
        Util.makeTilesAnimated("Environment", "Level", 14, map, 0.3f, 14, 22, 30);
        //Go through collisions, generate collision maps
        for (int i = 0; i < collisions.getCount(); i++) {
            MapObject mapObject = collisions.get(i);
            Entity e = null;
            float initialX = 0;
            float initialY = 0;
            if (mapObject instanceof RectangleMapObject) {
                RectangleMapObject rectangleObject = (RectangleMapObject) mapObject;
                Rectangle rectangle = rectangleObject.getRectangle();
                PolygonShape polygonShape = new PolygonShape();
                polygonShape.setAsBox(rectangle.getWidth() / 2f, rectangle.getHeight() / 2f);
                FixtureDef def = new FixtureDef();
                def.shape = polygonShape;
                initialX = rectangle.x + rectangle.getWidth() / 2f;
                initialY = rectangle.y + rectangle.getHeight() / 2f;
                e = TileMapCollisionFactory.create(initialX, initialY, def);
            } else if (mapObject instanceof EllipseMapObject) {
                EllipseMapObject circleMapObject = (EllipseMapObject) mapObject;
                Ellipse ellipse = circleMapObject.getEllipse();
                CircleShape circleShape = new CircleShape();
                circleShape.setRadius(ellipse.width / 2f);
                FixtureDef def = new FixtureDef();
                def.shape = circleShape;
                initialX = ellipse.x;
                initialY = ellipse.y;
                e = TileMapCollisionFactory.create(initialX, initialY, def);
            } else if (mapObject instanceof PolygonMapObject) {
                PolygonMapObject polygonMapObject = (PolygonMapObject) mapObject;
                Polygon polygon = polygonMapObject.getPolygon();
                PolygonShape polygonShape = new PolygonShape();
                polygonShape.set(polygon.getVertices());
                FixtureDef def = new FixtureDef();
                def.shape = polygonShape;
                initialX = polygon.getX();
                initialY = polygon.getY();
                e = TileMapCollisionFactory.create(initialX, initialY, def);
            }
            PooledEngine engine = InGameScreen.world.gameWorld();
            String name = mapObject.getName();
            if (e != null && name != null) {
                List<String> types = Arrays.asList(name.split(" "));
                if (types.contains("Checkpoint")) {
                    int checkpointNumber = mapObject.getProperties().get("CheckpointNumber", Integer.class);
                    CheckpointComponent checkpointComponent = engine.createComponent(CheckpointComponent.class);
                    checkpointComponent.init(new Vector2(initialX, initialY + 16), checkpointNumber);
                    e.add(checkpointComponent);
                }

                if (types.contains("KillPlayer")) {
                    KillPlayerOnTouchComponent killPlayerComponent = engine.createComponent(KillPlayerOnTouchComponent.class);
                    e.add(killPlayerComponent);
                }

                if (types.contains("Bouncy")) {
                    float bouncyStrength = mapObject.getProperties().get("Strength", Float.class);
                    BouncyComponent bouncyComponent = engine.createComponent(BouncyComponent.class);
                    bouncyComponent.init(bouncyStrength);
                    e.add(bouncyComponent);
                }

                if (types.contains("Moving")) {
                    float delay = mapObject.getProperties().get("Delay", Float.class);
                    int moveDistance = mapObject.getProperties().get("MoveDistance", Integer.class);
                    MoveType movementType = MoveType.fromString(mapObject.getProperties().get("Movement", String.class));
                    float speed = mapObject.getProperties().get("Speed", Float.class);
                    String textureId = mapObject.getProperties().get("Texture", String.class);
                    float delayVariation = mapObject.getProperties().get("DelayVariation", 0f, Float.class);

                    TextureComponent texture = engine.createComponent(TextureComponent.class);
                    TextureRegion region = new TextureRegion(TextureManager.getTexture(textureId));
                    texture.init(region);
                    e.add(texture);

                    TransformComponent transform = engine.createComponent(TransformComponent.class);
                    transform.init(region.getRegionWidth(), region.getRegionHeight());
                    e.add(transform);

                    MovementComponent movement = engine.createComponent(MovementComponent.class);
                    movement.init(delay, delayVariation, moveDistance, e.getComponent(RigidBodyComponent.class).rigidBody().getPosition(), speed, textureId, movementType, e);
                    e.add(movement);
                }
            }
            engine.addEntity(e);

        }
    }
}
