package me.partlysunny.vertigrow.world.systems.render;

import com.badlogic.ashley.core.Entity;
import me.partlysunny.vertigrow.util.constants.Mappers;
import me.partlysunny.vertigrow.world.components.render.TextureComponent;
import me.partlysunny.vertigrow.world.components.render.ZComponent;

import java.util.Comparator;

public class ZComparator implements Comparator<Entity> {

    @Override
    public int compare(Entity o1, Entity o2) {
        ZComponent texture1 = Mappers.zMapper.get(o1);
        ZComponent texture2 = Mappers.zMapper.get(o2);
        int index1 = texture1 == null ? 0 : texture1.index();
        int index2 = texture2 == null ? 0 : texture2.index();
        return Integer.compare(index1, index2);
    }
}
