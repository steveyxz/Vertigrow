package me.partlysunny.vertigrow.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import me.partlysunny.vertigrow.screens.InGameScreen;

import java.util.ArrayList;
import java.util.List;

public class LevelManager {

    private final List<Entity> levelObjects = new ArrayList<>();
    private final LevelBuilder builder = new LevelBuilder(this);

    private final TiledMap level;
    private final TiledMapRenderer renderer;
    private final InGameScreen screen;

    public LevelManager(String levelName, InGameScreen screen) {
        this.level = new TmxMapLoader().load("assets/levels/" + levelName + ".tmx");
        this.screen = screen;
        this.renderer = new OrthogonalTiledMapRenderer(level);
        builder.build(level);
    }

    public void render() {
        renderer.setView(InGameScreen.camera);
        renderer.render();
    }

    public InGameScreen screen() {
        return screen;
    }
}
