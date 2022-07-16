package me.partlysunny.game.util.utilities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.StringBuilder;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.building.utilities.Alignment;
import com.kotcrab.vis.ui.widget.Tooltip;
import me.partlysunny.game.util.constants.Mappers;
import me.partlysunny.game.world.components.collision.RigidBodyComponent;
import me.partlysunny.game.world.components.collision.TransformComponent;

import java.util.concurrent.ThreadLocalRandom;

public class Util {

    public static final ThreadLocalRandom RAND = ThreadLocalRandom.current();
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
}
