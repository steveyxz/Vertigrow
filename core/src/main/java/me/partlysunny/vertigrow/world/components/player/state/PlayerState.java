package me.partlysunny.vertigrow.world.components.player.state;

public enum PlayerState {

    MOVING_RIGHT(1, true),
    MOVING_LEFT(2, true),
    FALLING(3, false),
    RISING(4, false),
    PASSIVE(5, true);

    private final int value;
    private final boolean grounded;

    PlayerState(int value, boolean grounded) {
        this.value = value;
        this.grounded = grounded;
    }

    public static PlayerState getFromId(int state) {
        for (PlayerState s : PlayerState.values()) {
            if (s.value == state) {
                return s;
            }
        }
        return null;
    }

    public int value() {
        return value;
    }

    public boolean grounded() {
        return grounded;
    }
}
