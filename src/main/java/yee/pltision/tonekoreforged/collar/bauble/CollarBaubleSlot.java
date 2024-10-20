package yee.pltision.tonekoreforged.collar.bauble;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import yee.pltision.tonekoreforged.ToNekoCapabilityHelper;

public class CollarBaubleSlot extends Slot {
    Object accessor;
    BaublesAccessor baublesAccessor;
    public CollarBaubleSlot(Container p_40223_, int p_40224_, int p_40225_, int p_40226_,BaublesAccessor baublesAccessor,Object slotAccessor) {
        super(p_40223_, p_40224_, p_40225_, p_40226_);
        this.accessor=slotAccessor;
        this.baublesAccessor=baublesAccessor;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack p_40231_) {
        CollarBaubleState state= ToNekoCapabilityHelper.getCollarBaubleState(p_40231_);
        return state!=null&&state.mayPlace(baublesAccessor, accessor, getSlotIndex());
    }

}
