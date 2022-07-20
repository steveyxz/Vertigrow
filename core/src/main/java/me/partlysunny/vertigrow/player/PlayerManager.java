package me.partlysunny.vertigrow.player;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import me.partlysunny.vertigrow.util.constants.Mappers;
import me.partlysunny.vertigrow.util.utilities.LateMover;

public class PlayerManager {

    private Vector2 spawnPoint = new Vector2(50, 200);
    private int deathCount = 0;
    private final Entity player;

    public PlayerManager(Entity player) {
        this.player = player;
    }

    public void kill() {
        LateMover.tagToMove(Mappers.bodyMapper.get(player).rigidBody(), spawnPoint);
        deathCount++;
    }

    public Vector2 spawnPoint() {
        return spawnPoint;
    }

    public void setSpawnPoint(Vector2 spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    public int deathCount() {
        return deathCount;
    }

    public void setDeathCount(int deathCount) {
        this.deathCount = deathCount;
    }

    public Entity player() {
        return player;
    }
}
