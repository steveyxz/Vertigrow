package me.partlysunny.vertigrow.world.components.player;

import com.badlogic.gdx.Input.Keys;

public enum PlayerAction {

    JUMP(Keys.SPACE),
    LEFT(Keys.A),
    RIGHT(Keys.D),
    ATTACK(Keys.X);

    private final int defaultKey;

    PlayerAction(int defaultKey) {
        this.defaultKey = defaultKey;
    }

    public int defaultKey() {
        return defaultKey;
    }
}
