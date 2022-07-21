package me.partlysunny.vertigrow.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
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
import me.partlysunny.vertigrow.util.utilities.Util;
import me.partlysunny.vertigrow.world.components.tile.BouncyComponent;
import me.partlysunny.vertigrow.world.components.tile.CheckpointComponent;
import me.partlysunny.vertigrow.world.components.tile.KillPlayerOnTouchComponent;
import me.partlysunny.vertigrow.world.objects.type.TileMapCollisionFactory;

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
            String name = mapObject.getName();
            boolean killPlayer = false;
            boolean checkpoint = false;
            boolean bouncy = false;
            if (name != null) {
                killPlayer = name.equals("KillPlayer");
                checkpoint = name.equals("Checkpoint");
                bouncy = name.equals("Bouncy");
            }
            int checkpointNumber = -1;
            float bouncyStrength = -1f;
            if (checkpoint) {
                checkpointNumber = mapObject.getProperties().get("CheckpointNumber", Integer.class);
            }
            if (bouncy) {
                bouncyStrength = mapObject.getProperties().get("Strength", Float.class);
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
            PooledEngine engine = InGameScreen.world.gameWorld();
            if (e != null) {
                if (checkpoint) {
                    CheckpointComponent checkpointComponent = engine.createComponent(CheckpointComponent.class);
                    checkpointComponent.init(new Vector2(initialX, initialY + 16), checkpointNumber);
                    e.add(checkpointComponent);
                }

                if (killPlayer) {
                    KillPlayerOnTouchComponent killPlayerComponent = engine.createComponent(KillPlayerOnTouchComponent.class);
                    e.add(killPlayerComponent);
                }

                if (bouncy) {
                    BouncyComponent bouncyComponent = engine.createComponent(BouncyComponent.class);
                    bouncyComponent.init(bouncyStrength);
                    e.add(bouncyComponent);
                }
            }
            engine.addEntity(e);

        }
    }
}
