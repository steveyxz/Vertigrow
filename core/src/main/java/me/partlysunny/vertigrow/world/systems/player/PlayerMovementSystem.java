package me.partlysunny.vertigrow.world.systems.player;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import me.partlysunny.vertigrow.util.constants.Mappers;
import me.partlysunny.vertigrow.world.components.collision.RigidBodyComponent;
import me.partlysunny.vertigrow.world.components.player.PlayerControlComponent;

public class PlayerMovementSystem extends IteratingSystem {


    public PlayerMovementSystem() {
        super(Family.all(PlayerControlComponent.class, RigidBodyComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlayerControlComponent controller = Mappers.controlMapper.get(entity);
        RigidBodyComponent velocity = Mappers.bodyMapper.get(entity);

    }
}
