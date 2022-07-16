package me.partlysunny.vertigrow.world.components.player;

public enum PlayerAction {

    ;

    private final int defaultKey;

    PlayerAction(int defaultKey) {
        this.defaultKey = defaultKey;
    }

    public int defaultKey() {
        return defaultKey;
    }
}
