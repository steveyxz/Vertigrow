package me.partlysunny.vertigrow.world;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import me.partlysunny.vertigrow.effects.particle.ParticleEffectManager;
import me.partlysunny.vertigrow.effects.sound.SoundEffectManager;
import me.partlysunny.vertigrow.screens.InGameScreen;
import me.partlysunny.vertigrow.util.classes.Pair;
import me.partlysunny.vertigrow.util.constants.Mappers;
import me.partlysunny.vertigrow.util.utilities.Util;
import me.partlysunny.vertigrow.world.systems.render.TextureRenderingSystem;

public class CollisionHandler implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Pair<Entity, Entity> handle = Util.handlePlayerCollision(contact);

        if (handle != null) {
            Entity other = handle.a();

            if (Mappers.checkpointMapper.has(other)) {
                int checkpointNumber = Mappers.checkpointMapper.get(other).checkpointNumber();
                if (checkpointNumber > InGameScreen.playerManager.currentCheckpointNumber()) {
                    InGameScreen.playerManager.setCurrentCheckpointNumber(checkpointNumber);
                    InGameScreen.playerManager.setSpawnPoint(Mappers.checkpointMapper.get(other).positionToTeleport());
                    Vector2 pos = Mappers.bodyMapper.get(other).rigidBody().getPosition();
                    ParticleEffectManager.startEffect("checkpointReached", (int) TextureRenderingSystem.metersToPixels(pos.x), (int) TextureRenderingSystem.metersToPixels(pos.y), 200);
                    SoundEffectManager.getSound("checkpoint").play(1);
                }
            }

            if (Mappers.killPlayerMapper.has(other)) {
                InGameScreen.playerManager.kill();
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
