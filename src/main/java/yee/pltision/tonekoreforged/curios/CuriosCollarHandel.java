package yee.pltision.tonekoreforged.curios;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.collar.CollarSlotHandler;
import yee.pltision.tonekoreforged.collar.CollarState;
import yee.pltision.tonekoreforged.collar.CollarStateHandlerItem;

import java.util.function.Supplier;

public class CuriosCollarHandel implements CollarSlotHandler {
    Supplier<ICurioStacksHandler> curiosItemHandler;

    public CuriosCollarHandel(Supplier<ICurioStacksHandler> curiosItemHandler){
        this.curiosItemHandler=curiosItemHandler;
    }

    @Override
    public void setCollarSlot(ItemStack item) {
        if(curiosItemHandler.get()==null)return;
        if(curiosItemHandler.get().getStacks().getSlots()>0)
            curiosItemHandler.get().getStacks().setStackInSlot(0,item);
    }

    @Override
    public @Nullable CollarState getState() {
        if(curiosItemHandler.get()==null)return null;
        CollarStateHandlerItem handler;
        return curiosItemHandler.get().getStacks().getSlots()>0
                && ( handler= ToNeko.getItemCollarHandel(curiosItemHandler.get().getStacks().getStackInSlot(0)) )!=null
                ?handler.getState():null;
    }

    @Override
    public ItemStack getCollarItem() {
        if(curiosItemHandler.get()==null)return ItemStack.EMPTY;
        return curiosItemHandler.get().getStacks().getSlots()>0?curiosItemHandler.get().getStacks().getStackInSlot(0):null;
    }
}
