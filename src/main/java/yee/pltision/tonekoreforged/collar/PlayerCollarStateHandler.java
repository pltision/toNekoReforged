package yee.pltision.tonekoreforged.collar;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class PlayerCollarStateHandler implements CollarStateHandler {
    CollarState collarState;

    @Override
    public @Nullable CollarState getState() {
        return collarState;
    }

    @Override
    public boolean mayReplace(LivingEntity entity, ItemStack stack) {
        if(getState()==null)
            return stack.getItem() instanceof CollarItem || stack.isEmpty();
        else return getState().canTake(entity);
    }

    @Override
    public void setCollarSlot(LivingEntity entity, ItemStack item) {
        Item itemClass= item.getItem();
        if(itemClass instanceof CollarItem collar){
            collarState=collar.asState(item);
        }
        else {
            collarState=null;
        }
    }

}
