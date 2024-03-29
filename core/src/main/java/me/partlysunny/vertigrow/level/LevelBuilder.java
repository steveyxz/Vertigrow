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
import me.partlysunny.vertigrow.util.constants.Mappers;
import me.partlysunny.vertigrow.util.constants.Screens;
import me.partlysunny.vertigrow.util.utilities.LateMover;
import me.partlysunny.vertigrow.util.utilities.TextureManager;
import me.partlysunny.vertigrow.util.utilities.Util;
import me.partlysunny.vertigrow.world.components.collision.RigidBodyComponent;
import me.partlysunny.vertigrow.world.components.collision.TransformComponent;
import me.partlysunny.vertigrow.world.components.render.TextComponent;
import me.partlysunny.vertigrow.world.components.render.TextureComponent;
import me.partlysunny.vertigrow.world.components.render.TintComponent;
import me.partlysunny.vertigrow.world.components.render.ZComponent;
import me.partlysunny.vertigrow.world.components.tile.*;
import me.partlysunny.vertigrow.world.objects.type.TileMapCollisionFactory;

import java.util.Arrays;
import java.util.List;

import static me.partlysunny.vertigrow.screens.InGameScreen.playerManager;

public class LevelBuilder {


    private final LevelManager levelManager;

    public LevelBuilder(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    public void build(TiledMap map) {
        PooledEngine engine = InGameScreen.world.gameWorld();
        MapObjects collisions = map.getLayers().get("Collisions").getObjects();
        //Make all flags have animations
        Util.makeTilesAnimated("Environment", "Level", 15, map, 0.3f, 15, 23, 31, 39);
        Util.makeTilesAnimated("Environment", "Level", 14, map, 0.3f, 14, 22, 30);
        Util.makeTilesAnimated("Environment", "Level", 32, map, 0.3f, 32, 33, 34);
        Util.makeTilesAnimated("Environment", "Level", 40, map, 0.3f, 40, 41, 42);
        //Go through collisions, generate collision maps
        for (int i = 0; i < collisions.getCount(); i++) {
            MapObject mapObject = collisions.get(i);
            if (mapObject.getName() != null) {
                if (mapObject.getName().equals("PlayerSpawn")) {
                    RectangleMapObject rectangleObject = (RectangleMapObject) mapObject;
                    Rectangle rectangle = rectangleObject.getRectangle();
                    Vector2 newSpawn = new Vector2(rectangle.x + rectangle.getWidth() / 2f, rectangle.y + rectangle.getHeight() / 2f);
                    playerManager.setSpawnPoint(newSpawn);
                    LateMover.tagToMove(Mappers.bodyMapper.get(playerManager.player()).rigidBody(), newSpawn);
                    continue;
                }
                if (mapObject.getName().equals("Image")) {
                    Entity e = engine.createEntity();
                    RectangleMapObject rectangleObject = (RectangleMapObject) mapObject;
                    Rectangle rectangle = rectangleObject.getRectangle();

                    String textureId = mapObject.getProperties().get("Texture", String.class);
                    TextureComponent texture = engine.createComponent(TextureComponent.class);
                    texture.init(new TextureRegion(TextureManager.getTexture(textureId)));
                    e.add(texture);

                    TransformComponent transform = engine.createComponent(TransformComponent.class);
                    transform.position.set(rectangle.x + rectangle.getWidth() / 2f, rectangle.y + rectangle.getHeight() / 2f, 0);
                    transform.init(rectangle.getWidth(), rectangle.getHeight());
                    e.add(transform);

                    ZComponent z = engine.createComponent(ZComponent.class);
                    z.init(-1);
                    e.add(z);

                    engine.addEntity(e);
                    continue;

                }
                if (mapObject.getName().equals("Text")) {
                    Entity e = engine.createEntity();
                    RectangleMapObject rectangleObject = (RectangleMapObject) mapObject;
                    Rectangle rectangle = rectangleObject.getRectangle();

                    String content = mapObject.getProperties().get("Content", String.class);
                    float size = mapObject.getProperties().get("Size", Float.class);
                    TextComponent text = engine.createComponent(TextComponent.class);
                    text.init(content, size);
                    e.add(text);

                    TransformComponent transform = engine.createComponent(TransformComponent.class);
                    transform.position.set(rectangle.x, rectangle.y + rectangle.getHeight(), 0);
                    transform.init(rectangle.getWidth(), rectangle.getHeight());
                    e.add(transform);

                    ZComponent z = engine.createComponent(ZComponent.class);
                    z.init(-1);
                    e.add(z);

                    TintComponent tint = engine.createComponent(TintComponent.class);
                    tint.setTint(0.2f, 0.2f, 0.2f, 1);
                    e.add(tint);

                    engine.addEntity(e);
                    continue;
                }
            }
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

                if (types.contains("Portal")) {
                    String destination = mapObject.getProperties().get("WhereTo", String.class);
                    PortalComponent portal = engine.createComponent(PortalComponent.class);
                    portal.init(destination, levelManager.screen());
                    e.add(portal);
                }
            }
            engine.addEntity(e);

        }
    }
}
