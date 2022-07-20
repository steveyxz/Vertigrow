package me.partlysunny.vertigrow.world.objects;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public final class ObjectFactory {

    private static final ObjectFactory INSTANCE = new ObjectFactory();

    public static ObjectFactory instance() {
        return INSTANCE;
    }

    private final Map<Class<? extends GameObject>, GameObject> instances = new HashMap<>();

    public <T extends GameObject> void insertObject(PooledEngine engine, float initialX, float initialY, Class<T> type) {
        if (instances.containsKey(type)) {
            instances.get(type).insert(engine, initialX, initialY);
            return;
        }
        GameObject obj = null;
        try {
            obj = type.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Gdx.app.setLogLevel(Application.LOG_ERROR);
            Gdx.app.log("Vertigrow", "Internal error occurred, contact developers! : " + e.getMessage());
            return;
        }
        instances.put(type, obj);
        obj.insert(engine, initialX, initialY);
    }

}
