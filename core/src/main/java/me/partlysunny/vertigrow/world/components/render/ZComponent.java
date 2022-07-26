package me.partlysunny.vertigrow.world.components.render;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class ZComponent implements Component, Pool.Poolable {

    private int index;

    public int index() {
        return index;
    }

    public void init(int index) {
        this.index = index;
    }

    @Override
    public void reset() {
        index = 0;
    }
}
