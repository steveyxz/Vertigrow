package me.partlysunny.vertigrow.util.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

public class TextureManager {

    private static final Map<String, Texture> textures = new HashMap<>();

    public static void registerTexture(String id, Texture t) {
        textures.put(id, t);
    }

    public static void unregisterTexture(String id) {
        textures.remove(id);
    }

    public static Texture getTexture(String id) {
        return textures.get(id);
    }

    public static void initTextures() {
        doPngTexture("logo");
        //Player states
        doPngTexture("playerPassive");
        doPngTexture("playerMoveRight");
        doPngTexture("playerMoveLeft");
        doPngTexture("playerJump");
        doPngTexture("playerDeath");
        //Moving platforms
        doPngTexture("horizontal");
        doPngTexture("vertical");
        doPngTexture("killerWallVertical");
        doPngTexture("horizontalBouncy");
        //Key displays
        doPngTexture("adKeys");
        doPngTexture("spaceKey");
    }

    private static void doPngTexture(String value) {
        registerTexture(value, new Texture(Gdx.files.internal("assets/textures/" + value + ".png")));
    }

}
