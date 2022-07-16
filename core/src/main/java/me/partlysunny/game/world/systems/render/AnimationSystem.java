package me.partlysunny.game.world.systems.render;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import me.partlysunny.game.util.constants.Mappers;
import me.partlysunny.game.world.components.player.state.StateComponent;
import me.partlysunny.game.world.components.render.AnimationComponent;
import me.partlysunny.game.world.components.render.TextureComponent;

public class AnimationSystem extends IteratingSystem {

    public AnimationSystem() {
        super(Family.all(TextureComponent.class,
                AnimationComponent.class,
                StateComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        AnimationComponent ani = Mappers.animationMapper.get(entity);
        StateComponent state = Mappers.stateMapper.get(entity);

        if (ani.animations().containsKey(state.get())) {
            TextureComponent tex = Mappers.textureMapper.get(entity);
            tex.setTexture(ani.animations().get(state.get()).getKeyFrame(state.time, state.isLooping));
        }

        state.time += deltaTime;
    }
}
