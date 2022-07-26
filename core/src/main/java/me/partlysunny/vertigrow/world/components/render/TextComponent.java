package me.partlysunny.vertigrow.world.components.render;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class TextComponent implements Component, Pool.Poolable {

    private String text;
    private float size = 0;

    public String text() {
        return text;
    }

    public float size() {
        return size;
    }

    public void init(String text, float size) {
        this.text = text;
        this.size = size;
    }

    @Override
    public void reset() {
        text = null;
        size = 0;
    }
}
