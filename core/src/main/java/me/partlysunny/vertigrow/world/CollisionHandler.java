package me.partlysunny.vertigrow.world;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import me.partlysunny.vertigrow.screens.InGameScreen;
import me.partlysunny.vertigrow.util.classes.Pair;
import me.partlysunny.vertigrow.util.constants.Mappers;
import me.partlysunny.vertigrow.util.utilities.Util;

public class CollisionHandler implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Pair<Entity, Entity> handle = Util.handlePlayerCollision(contact);

        if (handle != null) {
            Entity player = handle.b();
            Entity other = handle.a();

            if (Mappers.checkpointMapper.has(other)) {
                InGameScreen.playerManager.setSpawnPoint(Mappers.checkpointMapper.get(other).positionToTeleport());
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
