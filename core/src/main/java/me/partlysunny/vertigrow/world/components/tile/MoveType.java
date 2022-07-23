package me.partlysunny.vertigrow.world.components.tile;

import com.badlogic.gdx.math.Vector2;

import java.util.function.BiFunction;

public enum MoveType {

    LEFT((start, dist) -> start.add(-dist, 0)),
    RIGHT((start, dist) -> start.add(dist, 0)),
    UP((start, dist) -> start.add(0, dist)),
    DOWN((start, dist) -> start.add(0, -dist));

    private final BiFunction<Vector2, Float, Vector2> getEndPos;

    MoveType(BiFunction<Vector2, Float, Vector2> getEndPos) {
        this.getEndPos = getEndPos;
    }

    public static MoveType fromString(String s) {
        return MoveType.valueOf(s.toUpperCase());
    }

    public Vector2 getEndPosition(Vector2 startPosition, float moveDistance) {
        return getEndPos.apply(new Vector2(startPosition), moveDistance);
    }

    public Vector2 getEndPositionWithoutCopy(Vector2 startPosition, float moveDistance) {
        return getEndPos.apply(startPosition, moveDistance);
    }
}
