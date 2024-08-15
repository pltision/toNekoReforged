package yee.pltision.tonekoreforged.collar;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface CollarHandler
{
    @Nullable
    CollarState getState();
    ItemStack getCollarSlot();
    boolean mayPlace(ItemStack stack);
    void setCollarSlot(ItemStack item);
}
