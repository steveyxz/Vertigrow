package me.partlysunny.vertigrow.player;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import me.partlysunny.vertigrow.effects.particle.ParticleEffectManager;
import me.partlysunny.vertigrow.effects.sound.SoundEffectManager;
import me.partlysunny.vertigrow.util.constants.Mappers;
import me.partlysunny.vertigrow.util.utilities.LateMover;
import me.partlysunny.vertigrow.world.components.player.state.PlayerState;
import me.partlysunny.vertigrow.world.systems.render.TextureRenderingSystem;

public class PlayerManager {

    private final Entity player;
    private Vector2 spawnPoint = new Vector2(0, 0);
    private int deathCount = 0;
    private int currentCheckpointNumber = -1;

    public PlayerManager(Entity player) {
        this.player = player;
    }

    public void kill() {
        Mappers.stateMapper.get(player).setState(PlayerState.DYING.value());
        Mappers.bodyMapper.get(player).rigidBody().setLinearVelocity(0, 0);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                Vector2 pos = Mappers.bodyMapper.get(player).rigidBody().getPosition();
                ParticleEffectManager.startEffect("death", (int) TextureRenderingSystem.metersToPixels(pos.x), (int) TextureRenderingSystem.metersToPixels(pos.y), 200);

                LateMover.tagToMove(Mappers.bodyMapper.get(player).rigidBody(), spawnPoint);
                deathCount++;
                Mappers.stateMapper.get(player).setState(PlayerState.PASSIVE.value());
                SoundEffectManager.getSound("die").play(1);
            }
        }, 0.95f);
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

    public int currentCheckpointNumber() {
        return currentCheckpointNumber;
    }

    public void setCurrentCheckpointNumber(int currentCheckpointNumber) {
        this.currentCheckpointNumber = currentCheckpointNumber;
    }
}
