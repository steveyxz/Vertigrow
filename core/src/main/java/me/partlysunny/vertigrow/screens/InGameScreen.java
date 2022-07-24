package me.partlysunny.vertigrow.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.eskalon.commons.screen.ManagedScreen;
import me.partlysunny.vertigrow.Vertigrow;
import me.partlysunny.vertigrow.effects.particle.ParticleEffectManager;
import me.partlysunny.vertigrow.effects.sound.MusicManager;
import me.partlysunny.vertigrow.effects.visual.VisualEffectManager;
import me.partlysunny.vertigrow.level.LevelManager;
import me.partlysunny.vertigrow.player.PlayerManager;
import me.partlysunny.vertigrow.util.constants.GameInfo;
import me.partlysunny.vertigrow.util.utilities.LateActionPerformer;
import me.partlysunny.vertigrow.util.utilities.LateMover;
import me.partlysunny.vertigrow.util.utilities.LateRemover;
import me.partlysunny.vertigrow.world.GameWorld;
import me.partlysunny.vertigrow.world.components.player.PlayerControlComponent;
import me.partlysunny.vertigrow.world.objects.ObjectFactory;
import me.partlysunny.vertigrow.world.objects.type.PlayerObject;

import static me.partlysunny.vertigrow.world.systems.render.TextureRenderingSystem.*;

public class InGameScreen extends ManagedScreen {

    public static final Vector2 cameraVelocity = new Vector2(0, 0);
    public static final OrthographicCamera camera = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
    public static final OrthographicCamera guiCamera = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
    public static final Viewport viewport = new ExtendViewport(camera.viewportWidth, camera.viewportHeight, camera);
    public static final Viewport guiViewport = new ExtendViewport(guiCamera.viewportWidth, guiCamera.viewportHeight, guiCamera);
    public static final ScreenGuiManager guiManager = new ScreenGuiManager();
    public static GameWorld world;
    public static LevelManager levelManager;
    public static PlayerManager playerManager;
    private final Vertigrow game;
    private final Box2DDebugRenderer debugRenderer;
    private final Stage stage;
    private final Stage guiStage;
    private float accumulator = 0;

    public InGameScreen(Vertigrow game) {
        this.game = game;
        stage = new Stage(viewport, game.batch());
        guiStage = new Stage(guiViewport, game.batch());
        guiManager.init(guiStage);
        world = new GameWorld(stage);
        debugRenderer = new Box2DDebugRenderer();
        playerManager = new PlayerManager(ObjectFactory.instance().insertObject(world.gameWorld(), 4400, 400, PlayerObject.class));
        levelManager = new LevelManager("level1", this);
    }

    @Override
    protected void create() {
        Gdx.input.setInputProcessor(new InputMultiplexer(guiStage, stage));
        InGameScreen s = this;

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (game.getScreenManager().getCurrentScreen().equals(s)) {
                    if (keycode == Input.Keys.ESCAPE) {
                        game.getScreenManager().pushScreen("paused", null);
                    }
                }
                return false;
            }
        });


        MusicManager.stop();
    }

    @Override
    public void hide() {

    }

    @Override
    public void render(float delta) {
        //Update viewport
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport.apply();
        //Clear screen with background color
        ScreenUtils.clear(GameInfo.BACKGROUND_COLOR);

        camera.update();
        game.batch().setProjectionMatrix(camera.combined);
        game.batch().enableBlending();
        game.batch().begin();

        //Move camera
        camera.position.add(cameraVelocity.x / PPM, cameraVelocity.y / PPM, 0);
        //Ticking and logic
        doPhysicsStep(Gdx.graphics.getDeltaTime());
        //Update AI
        GdxAI.getTimepiece().update(delta);
        //Visual effects (damage, swing)
        VisualEffectManager.update(delta);
        //Act out the current stage
        stage.act(Gdx.graphics.getDeltaTime());
        world.gameWorld().update(delta);


        //Process late removers / movers (so that they don't collide with the physics step)
        LateRemover.process(world);
        LateMover.process();
        LateActionPerformer.process();

        //Rendering
        ParticleEffectManager.render(game.batch(), delta);
        game.batch().end();
        stage.draw();
        //Draw UI
        guiViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        guiViewport.apply();
        //Update UI components
        guiManager.update();
        guiStage.act(delta);
        guiStage.draw();
        levelManager.render();
        //debugRenderer.render(world.physicsWorld(), camera.combined);
    }

    private void doPhysicsStep(float deltaTime) {
        // fixed time step
        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= GameInfo.TIME_STEP) {
            InGameScreen.world.physicsWorld().step(GameInfo.TIME_STEP, GameInfo.VELOCITY_ITERATIONS, GameInfo.POSITION_ITERATIONS);
            accumulator -= GameInfo.TIME_STEP;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void deleteLevel() {
        for (Entity e : world.gameWorld().getEntitiesFor(Family.all().exclude(PlayerControlComponent.class).get())) {
            LateRemover.tagToRemove(e);
        }
    }
}
