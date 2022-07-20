package me.partlysunny.vertigrow.level;

import com.badlogic.gdx.maps.tiled.TiledMap;

public class Level {

    private final TiledMap map;

    public Level(TiledMap map) {
        this.map = map;
    }

    public TiledMap map() {
        return map;
    }
}
