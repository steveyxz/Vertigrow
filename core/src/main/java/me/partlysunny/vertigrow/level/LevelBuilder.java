package me.partlysunny.vertigrow.level;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import me.partlysunny.vertigrow.util.utilities.Util;
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
            if (mapObject instanceof RectangleMapObject) {
                RectangleMapObject rectangleObject = (RectangleMapObject) mapObject;
                Rectangle rectangle = rectangleObject.getRectangle();
                PolygonShape polygonShape = new PolygonShape();
                polygonShape.setAsBox(rectangle.getWidth() / 2f, rectangle.getHeight() / 2f);
                FixtureDef def = new FixtureDef();
                def.shape = polygonShape;
                TileMapCollisionFactory.create(rectangle.x + rectangle.getWidth() / 2f, rectangle.y + rectangle.getHeight() / 2f, def, checkpoint, killPlayer, checkpointNumber);
            } else if (mapObject instanceof EllipseMapObject) {
                EllipseMapObject circleMapObject = (EllipseMapObject) mapObject;
                Ellipse ellipse = circleMapObject.getEllipse();
                CircleShape circleShape = new CircleShape();
                circleShape.setRadius(ellipse.width / 2f);
                FixtureDef def = new FixtureDef();
                def.shape = circleShape;
                TileMapCollisionFactory.create(ellipse.x, ellipse.y, def, checkpoint, killPlayer, checkpointNumber);
            } else if (mapObject instanceof PolygonMapObject) {
                PolygonMapObject polygonMapObject = (PolygonMapObject) mapObject;
                Polygon polygon = polygonMapObject.getPolygon();
                PolygonShape polygonShape = new PolygonShape();
                polygonShape.set(polygon.getVertices());
                FixtureDef def = new FixtureDef();
                def.shape = polygonShape;
                TileMapCollisionFactory.create(polygon.getX(), polygon.getY(), def, checkpoint, killPlayer, checkpointNumber);
            }
        }
    }
}
