package yee.pltision.tonekoreforged.collar;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface CollarStateHandler
{
    @Nullable
    CollarState getState();

    ItemStack getCollarItem();

    static ItemStack addTagToItem(CollarState state,ItemStack item){
        if(item.isEmpty())return ItemStack.EMPTY;
        if(state!=null) state.addTagToItem(item);
        return item;
    }

}
