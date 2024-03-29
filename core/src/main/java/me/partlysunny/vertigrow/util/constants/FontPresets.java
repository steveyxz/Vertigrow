package me.partlysunny.vertigrow.util.constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashMap;
import java.util.Map;

public class FontPresets {

    private static final Map<String, Map<Float, BitmapFont>> sizeMap = new HashMap<>();

    public static final String GAME_FONT = "gamefont.ttf";

    private static BitmapFont loadFont(String fontPath, int size) {
        //Generate a font object for font
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.flip = true;

        //The following settings allow the font to scale smoothly
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.minFilter = Texture.TextureFilter.Linear;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/fonts/" + fontPath));

        BitmapFont myFont = generator.generateFont(parameter);
        myFont.setUseIntegerPositions(false);
        return myFont;
    }

    public static BitmapFont getFontWithSize(String path, float size) {
        if (sizeMap.containsKey(path)) {
            if (sizeMap.get(path).containsKey(size)) {
                return sizeMap.get(path).get(size);
            }
        } else {
            sizeMap.put(path, new HashMap<>());
        }
        BitmapFont newFont = loadFont(path, 30);
        newFont.getData().setScale(size, -size);
        sizeMap.get(path).put(size, newFont);
        return newFont;
    }

}
