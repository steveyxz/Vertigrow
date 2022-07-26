package me.partlysunny.vertigrow.util.constants;

import com.badlogic.ashley.core.ComponentMapper;
import me.partlysunny.vertigrow.world.components.collision.DeletionListenerComponent;
import me.partlysunny.vertigrow.world.components.collision.RigidBodyComponent;
import me.partlysunny.vertigrow.world.components.collision.TransformComponent;
import me.partlysunny.vertigrow.world.components.player.PlayerCameraFollowComponent;
import me.partlysunny.vertigrow.world.components.player.PlayerControlComponent;
import me.partlysunny.vertigrow.world.components.player.state.StateComponent;
import me.partlysunny.vertigrow.world.components.render.*;
import me.partlysunny.vertigrow.world.components.tile.*;

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
    public static final ComponentMapper<KillPlayerOnTouchComponent> killPlayerMapper = ComponentMapper.getFor(KillPlayerOnTouchComponent.class);
    public static final ComponentMapper<CheckpointComponent> checkpointMapper = ComponentMapper.getFor(CheckpointComponent.class);
    public static final ComponentMapper<BouncyComponent> bouncyMapper = ComponentMapper.getFor(BouncyComponent.class);
    public static final ComponentMapper<MovementComponent> movementMapper = ComponentMapper.getFor(MovementComponent.class);
    public static final ComponentMapper<PortalComponent> portalMapper = ComponentMapper.getFor(PortalComponent.class);
    public static final ComponentMapper<TextComponent> textMapper = ComponentMapper.getFor(TextComponent.class);
    public static final ComponentMapper<ZComponent> zMapper = ComponentMapper.getFor(ZComponent.class);
}
