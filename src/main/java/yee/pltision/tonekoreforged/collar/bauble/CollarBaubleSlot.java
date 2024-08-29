package yee.pltision.tonekoreforged.collar.bauble;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import yee.pltision.tonekoreforged.ToNeko;

public class CollarBaubleSlot extends Slot {
    Object accessor;
    public CollarBaubleSlot(Container p_40223_, int p_40224_, int p_40225_, int p_40226_,Object slotAccessor) {
        super(p_40223_, p_40224_, p_40225_, p_40226_);
        this.accessor=slotAccessor;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack p_40231_) {
        CollarBaubleState state= ToNeko.getCollarBaubleState(p_40231_);
        return state!=null&&state.mayPlace(accessor,getSlotIndex());
    }

}
