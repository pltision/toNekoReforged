package yee.pltision.tonekoreforged.collar.bauble;

import net.minecraft.world.Container;
import yee.pltision.tonekoreforged.ToNekoCapabilityHelper;

import java.util.AbstractList;
import java.util.List;

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
                return ToNekoCapabilityHelper.getCollarBaubleState(container.getItem(index));
            }

            @Override
            public CollarBaubleState set(int index, CollarBaubleState element) {
                CollarBaubleState origin=ToNekoCapabilityHelper.getCollarBaubleState(container.getItem(index));
                container.setItem(index,element.asItem());
                return origin;
            }
        };
    }
}
