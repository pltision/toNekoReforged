package yee.pltision.tonekoreforged.collar;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class PlayerCollarHandler implements CollarHandler {
    ItemStack collarItem;
    CollarState collarState;

    @Override
    public @Nullable CollarState getState() {
        return null;
    }

    @Override
    public ItemStack getCollarSlot() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof CollarItem;
    }

    @Override
    public void setCollarSlot(ItemStack item) {

    }
}
