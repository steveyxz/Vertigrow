package me.partlysunny.vertigrow.world.components.collision;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class CheckpointComponent implements Component, Pool.Poolable {

    private Vector2 positionToTeleport;

    public Vector2 positionToTeleport() {
        return positionToTeleport;
    }

    public void init(Vector2 positionToTeleport) {
        this.positionToTeleport = positionToTeleport;
    }

    @Override
    public void reset() {
        positionToTeleport = null;
    }
}
