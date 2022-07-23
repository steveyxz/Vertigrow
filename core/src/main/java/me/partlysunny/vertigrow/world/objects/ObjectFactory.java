package me.partlysunny.vertigrow.world.objects;

import com.badlogic.ashley.core.Entity;
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

    public <T extends GameObject> Entity insertObject(PooledEngine engine, float initialX, float initialY, Class<T> type) {
        if (instances.containsKey(type)) {
            return instances.get(type).insert(engine, initialX, initialY);
        }
        GameObject obj;
        try {
            obj = type.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Gdx.app.setLogLevel(Application.LOG_ERROR);
            Gdx.app.log("Vertigrow", "Internal error occurred, contact developers! : " + e.getMessage());
            return null;
        }
        instances.put(type, obj);
        return obj.insert(engine, initialX, initialY);
    }

}
