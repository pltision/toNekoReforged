package yee.pltision.tonekoreforged.collar.bauble;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CollarBaubleHandel extends MenuProvider {
    CollarBaubleState getBaubleState();
    ItemStack getCollarBaubleItem();

    @Override
    default @NotNull Component getDisplayName(){
        CollarBaubleState state=getBaubleState();
        return state==null? Component.empty():state.getDisplayName();
    }

    @Nullable
    @Override
    default AbstractContainerMenu createMenu(int p_39954_, @NotNull Inventory p_39955_, @NotNull Player p_39956_) {
        CollarBaubleState state=getBaubleState();
        return state==null?null:state.createMenu(p_39954_,p_39955_,p_39956_);
    }
}
