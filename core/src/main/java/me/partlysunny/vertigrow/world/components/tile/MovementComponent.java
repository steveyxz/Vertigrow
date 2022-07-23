package me.partlysunny.vertigrow.world.components.tile;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class MovementComponent implements Component, Pool.Poolable {

    private float delay;
    private int moveDistance;
    private Vector2 startPosition = new Vector2();
    private float speed;
    private String texture;
    private MoveType type;
    private Entity parent;
    private float delayTimer = 0;
    private Vector2 endPosition = new Vector2();
    private boolean movingToEnd = true;

    public void init(float delay, int moveDistance, Vector2 startPosition, float speed, String texture, MoveType type, Entity parent) {
        this.delay = delay;
        this.parent = parent;
        this.moveDistance = moveDistance;
        this.startPosition.set(startPosition);
        this.endPosition = type.getEndPosition(startPosition, moveDistance);
        this.speed = speed;
        this.texture = texture;
        this.type = type;
        this.movingToEnd = true;
    }

    public float delay() {
        return delay;
    }

    public int moveDistance() {
        return moveDistance;
    }

    public Vector2 startPosition() {
        return startPosition;
    }

    public float speed() {
        return speed;
    }

    public String texture() {
        return texture;
    }

    public MoveType type() {
        return type;
    }

    public Entity parent() {
        return parent;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    public Vector2 endPosition() {
        return endPosition;
    }

    public boolean movingToEnd() {
        return movingToEnd;
    }

    public void setMovingToEnd(boolean movingToEnd) {
        this.movingToEnd = movingToEnd;
    }

    public float delayTimer() {
        return delayTimer;
    }

    public void setDelayTimer(float delayTimer) {
        this.delayTimer = delayTimer;
    }

    @Override
    public void reset() {
        texture = null;
        speed = 0;
        startPosition.set(0, 0);
        endPosition.set(0, 0);
        delayTimer = 0;
        moveDistance = 0;
        delay = 0;
        parent = null;
        type = MoveType.UP;
        movingToEnd = true;
    }
}
