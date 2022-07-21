package me.partlysunny.vertigrow.world.components.tile;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class CheckpointComponent implements Component, Pool.Poolable {

    private Vector2 positionToTeleport = null;
    private int checkpointNumber = -1;

    public Vector2 positionToTeleport() {
        return positionToTeleport;
    }

    public int checkpointNumber() {
        return checkpointNumber;
    }

    public void init(Vector2 positionToTeleport, int checkpointNumber) {
        this.positionToTeleport = positionToTeleport;
        this.checkpointNumber = checkpointNumber;
    }

    @Override
    public void reset() {
        positionToTeleport = null;
        checkpointNumber = -1;
    }
}
