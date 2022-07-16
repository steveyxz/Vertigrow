package me.partlysunny.game.util.constants;

import com.badlogic.ashley.core.ComponentMapper;
import me.partlysunny.game.world.components.collision.DeletionListenerComponent;
import me.partlysunny.game.world.components.collision.RigidBodyComponent;
import me.partlysunny.game.world.components.collision.TransformComponent;
import me.partlysunny.game.world.components.player.PlayerCameraFollowComponent;
import me.partlysunny.game.world.components.player.PlayerControlComponent;
import me.partlysunny.game.world.components.player.state.StateComponent;
import me.partlysunny.game.world.components.render.ActorComponent;
import me.partlysunny.game.world.components.render.AnimationComponent;
import me.partlysunny.game.world.components.render.TextureComponent;
import me.partlysunny.game.world.components.render.TintComponent;

public final class Mappers {

    public static final ComponentMapper<RigidBodyComponent> bodyMapper = ComponentMapper.getFor(RigidBodyComponent.class);
    public static final ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
    public static final ComponentMapper<AnimationComponent> animationMapper = ComponentMapper.getFor(AnimationComponent.class);
    public static final ComponentMapper<TintComponent> tintMapper = ComponentMapper.getFor(TintComponent.class);
    public static final ComponentMapper<TextureComponent> textureMapper = ComponentMapper.getFor(TextureComponent.class);
    public static final ComponentMapper<PlayerCameraFollowComponent> cameraFollowMapper = ComponentMapper.getFor(PlayerCameraFollowComponent.class);
    public static final ComponentMapper<ActorComponent> actorMapper = ComponentMapper.getFor(ActorComponent.class);
    public static final ComponentMapper<DeletionListenerComponent> deleteListenerMapper = ComponentMapper.getFor(DeletionListenerComponent.class);
    public static final ComponentMapper<PlayerControlComponent> controlMapper = ComponentMapper.getFor(PlayerControlComponent.class);
    public static final ComponentMapper<StateComponent> stateMapper = ComponentMapper.getFor(StateComponent.class);
}
