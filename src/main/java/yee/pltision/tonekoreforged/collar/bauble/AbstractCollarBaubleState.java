package yee.pltision.tonekoreforged.collar.bauble;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class AbstractCollarBaubleState implements CollarBaubleState {

    ItemStack item;

    public AbstractCollarBaubleState(ItemStack item){
        this.item=item;
    }

    @Override
    public ItemStack asItem() {
        CompoundTag tag=serializeNBT();
        if(!tag.isEmpty())
            item.addTagElement(CollarBaubleState.COLLAR_BAUBLE_TAG,tag);
        return item;
    }

}
