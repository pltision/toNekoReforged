package yee.pltision.tonekoreforged.collar.bauble;

import net.minecraft.world.Container;
import yee.pltision.tonekoreforged.ToNeko;

import java.util.*;

public interface BaublesAccessor {
    List<CollarBaubleState> baubles();

    static BaublesAccessor of(Container container){
        return new BaublesAccessor() {
            final List<CollarBaubleState> states=getList(container);
            @Override
            public List<CollarBaubleState> baubles() {
                return states;
            }
        };
    }

    static List<CollarBaubleState> getList(Container container){
        return new AbstractList<>(){
            @Override
            public int size() {
                return container.getContainerSize();
            }

            @Override
            public CollarBaubleState get(int index) {
                return ToNeko.getCollarBaubleState(container.getItem(index));
            }

            @Override
            public CollarBaubleState set(int index, CollarBaubleState element) {
                CollarBaubleState origin=ToNeko.getCollarBaubleState(container.getItem(index));
                container.setItem(index,element.asItem());
                return origin;
            }
        };
    }
}
