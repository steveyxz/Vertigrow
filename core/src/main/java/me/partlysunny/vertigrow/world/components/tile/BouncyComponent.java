package me.partlysunny.vertigrow.world.components.tile;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class BouncyComponent implements Component, Pool.Poolable {

    private float strength = 0;

    public float strength() {
        return strength;
    }

    public void init(float strength) {
        this.strength = strength;
    }

    @Override
    public void reset() {
        strength = 0;
    }
}
