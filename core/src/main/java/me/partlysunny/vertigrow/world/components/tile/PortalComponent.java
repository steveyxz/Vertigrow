package me.partlysunny.vertigrow.world.components.tile;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import me.partlysunny.vertigrow.screens.InGameScreen;

public class PortalComponent implements Component, Pool.Poolable {

    private String destination = null;
    private InGameScreen screen;

    public String destination() {
        return destination;
    }

    public void init(String destination, InGameScreen screen) {
        this.destination = destination;
        this.screen = screen;
    }

    public InGameScreen screen() {
        return screen;
    }

    @Override
    public void reset() {
        this.destination = null;
        this.screen = null;
    }
}
