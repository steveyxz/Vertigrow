package me.partlysunny.vertigrow.util.utilities;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.building.utilities.Alignment;
import com.kotcrab.vis.ui.widget.Tooltip;
import de.damios.guacamole.gdx.pool.Vector2Pool;
import me.partlysunny.vertigrow.screens.InGameScreen;
import me.partlysunny.vertigrow.util.classes.Pair;
import me.partlysunny.vertigrow.util.constants.Mappers;
import me.partlysunny.vertigrow.world.GameWorld;
import me.partlysunny.vertigrow.world.components.collision.RigidBodyComponent;
import me.partlysunny.vertigrow.world.components.collision.TransformComponent;

import java.util.concurrent.ThreadLocalRandom;

public class Util {

    public static final ThreadLocalRandom RAND = ThreadLocalRandom.current();
    public static final Vector2Pool GLOBAL_POOL = new Vector2Pool();
    private static final GlyphLayout layout = new GlyphLayout();
    private static final Vector2 vec = new Vector2();

    public static int getRandomBetween(int a, int b) {
        return RAND.nextInt(a, b + 1);
    }

    public static double getRandomBetween(double min, double max) {
        if (min == max) {
            return min;
        }
        return RAND.nextDouble(min, max);
    }

    public static float getRandomBetween(float min, float max) {
        if (min == max) {
            return min;
        }
        return (float) RAND.nextDouble(min, max);
    }

    public static void loadVisUI() {
        if (!VisUI.isLoaded()) {
            //VisUI.load(new Skin(Gdx.files.internal("assets/flatEarth/flat-earth-ui.json")));
        }
    }

    public static void formatTooltip(Tooltip t) {
        t.getContentCell().align(Alignment.CENTER.getAlignment());
        if (t.getContent() instanceof Label) {
            ((Label) t.getContent()).setAlignment(Alignment.CENTER.getAlignment());
            ((Label) t.getContent()).setWrap(true);
            StringBuilder text = ((Label) t.getContent()).getText();
            layout.setText(((Label) t.getContent()).getStyle().font, text);
            float height = layout.height;
            t.getContentCell().padBottom(height * 3f);
        }
        if (t.getContent() instanceof Table) {
            for (Actor a : ((Table) t.getContent()).getChildren()) {
                ((Label) a).setAlignment(Alignment.CENTER.getAlignment());
                ((Label) a).setWrap(true);
            }
            t.getContentCell().padBottom(t.getContent().getHeight());
        }
        t.setSize(30, 30);
        t.getContentCell().width(26);
        t.setAppearDelayTime(0);
    }

    public static void doKnockback(Entity from, Entity target, float force) {
        if (Mappers.bodyMapper.has(target)) {
            RigidBodyComponent r = Mappers.bodyMapper.get(target);

            TransformComponent playerPos = Mappers.transformMapper.get(from);

            vec.set(r.rigidBody().getPosition().x - playerPos.position.x, r.rigidBody().getPosition().y - playerPos.position.y);
            vec.nor();
            vec.scl(force);

            r.rigidBody().setLinearVelocity(vec);
        }
    }

    public static float getVolumeOfSoundFromPos(float playerX, float playerY, float soundX, float soundY, float initialVolume) {
        float distX = soundX - playerX;
        float distY = soundY - playerY;
        float dist = (float) Math.sqrt(Math.abs(distX * distX + distY * distY));
        return 1 / dist * initialVolume;
    }

    public static Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.set(MathUtils.cos(angle), MathUtils.sin(angle));
        return outVector;
    }

    public static float vectorToAngle(Vector2 angle) {
        return MathUtils.atan2(angle.y, angle.x);
    }

    public static void scaleDownVelocity(Entity e, float by) {
        if (Mappers.bodyMapper.has(e)) {
            RigidBodyComponent body = Mappers.bodyMapper.get(e);
            Body rigidBody = body.rigidBody();
            rigidBody.setLinearVelocity(rigidBody.getLinearVelocity().x * by, rigidBody.getLinearVelocity().y * by);
        }
    }

    public static void getRandomPosAround(Vector2 out, float x, float y, float maxDistance, float minDistance) {
        float xDist = getRandomBetween(minDistance, maxDistance) * (RAND.nextBoolean() ? 1 : -1);
        float yDist = getRandomBetween(minDistance, maxDistance) * (RAND.nextBoolean() ? 1 : -1);
        out.set(x + xDist, y + yDist);
    }

    public static Shape generateShape(int sideCount, float radius) {
        Shape shape;
        if (sideCount == 0) {
            shape = new CircleShape();
            shape.setRadius(radius / 2f);
        } else {
            shape = new PolygonShape();
            float[] points = new float[sideCount * 2];
            for (int i = 0; i < sideCount * 2; i += 2) {
                float angle = (i / 2f) * (360f / sideCount) - ((360f / sideCount) / 2f);
                float x = radius * MathUtils.cos(angle);
                float y = radius * MathUtils.sin(angle);
                points[i] = x;
                points[i + 1] = y;
            }
            ((PolygonShape) shape).set(points);
        }
        return shape;
    }

    public static float getDistanceBetween(TransformComponent a, TransformComponent b) {
        float xDiff = a.position.x - b.position.x;
        float yDiff = a.position.y - b.position.y;

        return (float) Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }

    public static Animation<TextureRegion> getAnimations(float frameDuration, int width, int height, int numberOfTextures, String textureId, Animation.PlayMode mode) {
        Array<TextureRegion> frames = new Array<>();
        Texture t = TextureManager.getTexture(textureId);
        for (int i = 0; i < numberOfTextures; i++) {
            frames.add(new TextureRegion(t, i * width, 0, width, height));
        }
        Animation<TextureRegion> animation = new Animation<>(frameDuration, frames);
        animation.setPlayMode(mode);
        return animation;
    }

    public static void makeTilesAnimated(String tileSet, String layerName, int tileId, TiledMap map, float interval, int... animationIds) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerName);
        TiledMapTileSet tileset = map.getTileSets().getTileSet(tileSet);

        Array<StaticTiledMapTile> at = new Array<>();
        for (int i : animationIds) {
            if (tileset.getTile(i + 1) != null) at.add((StaticTiledMapTile) tileset.getTile(i + 1));
        }

        for (int i = 0; i < layer.getWidth(); i++) {
            for (int j = 0; j < layer.getHeight(); j++) {
                TiledMapTileLayer.Cell cell = layer.getCell(i, j);
                if (cell == null) continue;
                if (cell.getTile().getId() == tileId + 1) {
                    cell.setTile(new AnimatedTiledMapTile(interval, at));
                }
            }
        }
    }

    public static Pair<Entity, Entity> handlePlayerCollision(Contact contact) {
        Pair<Entity, Entity> conversion = convertContact(contact, Mappers.controlMapper, false);
        if (conversion == null) {
            return null;
        }
        Entity other = conversion.a();
        if (!Mappers.checkpointMapper.has(other) && !Mappers.killPlayerMapper.has(other) && !Mappers.bouncyMapper.has(other) && !Mappers.portalMapper.has(other)) {
            return null;
        }
        return conversion;
    }

    /**
     * Converts a contact into two entities, with one focused one
     *
     * @param contact           The contact
     * @param focusedMapper     The mapper that this is focused (the check for if this is suitable), usually the most prominent component
     * @param deleteIfIdentical If you should delete the entities if they both have the focused component
     * @return The converted contact
     */
    private static Pair<Entity, Entity> convertContact(Contact contact, ComponentMapper focusedMapper, boolean deleteIfIdentical) {
        GameWorld world = InGameScreen.world;
        Entity a = world.getEntityWithRigidBody(contact.getFixtureA().getBody());
        Entity b = world.getEntityWithRigidBody(contact.getFixtureB().getBody());
        if (a == null || b == null) {
            return null;
        }
        if (a.isScheduledForRemoval() || a.isRemoving() || b.isScheduledForRemoval() || b.isRemoving()) {
            return null;
        }
        Entity focus = null;
        Entity other = null;
        if (focusedMapper.has(b)) {
            other = a;
            focus = b;
        }
        if (focusedMapper.has(a)) {
            if (focus != null) {
                if (deleteIfIdentical) {
                    LateRemover.tagToRemove(a);
                    LateRemover.tagToRemove(b);
                }
                return null;
            }
            other = b;
            focus = a;
        }
        if (focus == null) {
            return null;
        }
        return new Pair<>(other, focus);
    }
}
