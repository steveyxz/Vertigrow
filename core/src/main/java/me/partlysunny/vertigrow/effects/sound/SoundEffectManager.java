package me.partlysunny.vertigrow.effects.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import me.partlysunny.vertigrow.Vertigrow;

import java.util.HashMap;
import java.util.Map;

public class SoundEffectManager {

    private static final Map<String, Sound> sounds = new HashMap<>();

    static {
        loadWavRegular("startup");
        loadWavRegular("checkpoint");
        loadWavRegular("die");
        loadWavRegular("jump");
    }

    public static void registerSound(String id, Sound sound) {
        sounds.put(id, sound);
    }

    public static Sound getSound(String id) {
        return sounds.get(id);
    }

    public static void unregisterSound(String id) {
        sounds.remove(id);
    }

    private static void loadWavRegular(String name) {
        registerSound(name, Gdx.audio.newSound(Gdx.files.internal("assets/sounds/" + name + ".wav")));
    }

    public static void play(String effect, float volume) {
        if (Vertigrow.settings.sound()) getSound(effect).play(volume * Vertigrow.settings.soundVolume());
    }

}
