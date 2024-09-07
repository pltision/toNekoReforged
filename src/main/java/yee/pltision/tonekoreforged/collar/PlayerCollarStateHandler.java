package yee.pltision.tonekoreforged.collar;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.ToNeko;

public class PlayerCollarStateHandler implements CollarSlotHandler {
    CollarState collarState;
    ItemStack item;

    public PlayerCollarStateHandler(){
        item=ItemStack.EMPTY;
    }

    @Override
    public @Nullable CollarState getState() {
        return collarState;
    }

    @Override
    public ItemStack getCollarItem() {
        return CollarStateHandler.addTagToItem(getState(),item);
    }

    @Override
    public void setCollarSlot(ItemStack item) {
        CollarStateHandlerItem handler= ToNeko.getItemCollarHandel(item);
        if(handler!=null){
            this.collarState= handler.getState();
            this.item=item;
        }
        else {
            this.collarState=null;
            this.item=ItemStack.EMPTY;
        }
    }

    @Override
    public boolean disableSlotUi() {
        return false;
    }
}
