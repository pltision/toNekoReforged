package yee.pltision.tonekoreforged.collar;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class PlayerCollarCapability implements CollarCapability {
    ItemStack collarItem;
    CollarState collarState;

    @Override
    public @Nullable CollarState getState() {
        return null;
    }

    @Override
    public ItemStack getCollarSlot() {
        return null;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof CollarItem;
    }

    @Override
    public void setCollarSlot(ItemStack item) {

    }
}
