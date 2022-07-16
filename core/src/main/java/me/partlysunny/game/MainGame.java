package me.partlysunny.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.eskalon.commons.core.ManagedGame;
import de.eskalon.commons.screen.ManagedScreen;
import de.eskalon.commons.screen.transition.ScreenTransition;
import me.partlysunny.game.effects.particle.ParticleEffectManager;
import me.partlysunny.game.effects.visual.VisualEffectManager;
import me.partlysunny.game.util.constants.Screens;
import me.partlysunny.game.util.constants.Transitions;
import me.partlysunny.game.util.utilities.TextureManager;

public class MainGame extends ManagedGame<ManagedScreen, ScreenTransition> {

    public static final SettingsManager settings = new SettingsManager();
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    @Override
    public void create() {
        super.create();

        reload();

        screenManager.pushScreen("intro", "blending");
    }

    public void reload() {
        settings.load();
        TextureManager.initTextures();
        ParticleEffectManager.init();
        VisualEffectManager.init();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        batch = new SpriteBatch();
        Screens.init(this);
        Transitions.init(this);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }

    public SpriteBatch batch() {
        return batch;
    }

    public ShapeRenderer shapeRenderer() {
        return shapeRenderer;
    }

    public BitmapFont font() {
        return font;
    }

    public static final class SettingsManager {

        private boolean music = true;
        private float musicVolume = 1;
        private boolean sound = true;
        private float soundVolume = 1;

        public boolean music() {
            return music;
        }

        public void setMusic(boolean music) {
            this.music = music;
            save();
        }

        public float musicVolume() {
            return musicVolume;
        }

        public void setMusicVolume(float musicVolume) {
            this.musicVolume = musicVolume;
            save();
        }

        public boolean sound() {
            return sound;
        }

        public void setSound(boolean sound) {
            this.sound = sound;
            save();
        }

        public float soundVolume() {
            return soundVolume;
        }

        public void setSoundVolume(float soundVolume) {
            this.soundVolume = soundVolume;
            save();
        }

        public void load() {
            Preferences settings = Gdx.app.getPreferences("userSettings");
            if (!settings.contains("music")) {
                save();
                return;
            }
            music = (settings.getBoolean("music"));
            musicVolume = (settings.getFloat("musicVolume"));
            sound = (settings.getBoolean("sound"));
            soundVolume = (settings.getFloat("soundVolume"));
        }

        public void save() {
            Preferences settings = Gdx.app.getPreferences("userSettings");
            settings.putBoolean("music", music);
            settings.putFloat("musicVolume", musicVolume);
            settings.putBoolean("sound", sound);
            settings.putFloat("soundVolume", soundVolume);
            settings.flush();
        }
    }

}
