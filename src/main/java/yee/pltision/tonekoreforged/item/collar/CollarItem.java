package yee.pltision.tonekoreforged.item.collar;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.collar.*;
import yee.pltision.tonekoreforged.curios.CuriosInterface;

import java.util.function.Supplier;

public interface CollarItem extends MenuProviderItem {

    //内部使用，获取CollarState应使用能力系统。
    CollarState asState(ItemStack item);

    AbstractContainerMenu getMenu(int id, @NotNull Inventory inventory, @NotNull Player player,CollarState state,ItemStack item);

    default ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ICapabilityProvider() {
            final Supplier<CollarState> state=new Supplier<>() {
                boolean uncreated =true;
                CollarState state;
                @Override
                public CollarState get() {
                    if(uncreated) {
                        uncreated =false;
                        state=asState(stack);
                    }
                    return state;
                }
            };
            final LazyOptional<?> curiosOptional= ToNeko.useCuriosApi()? CuriosInterface.createCollarItemOptional(stack,state):LazyOptional.empty();
            final LazyOptional<CollarStateHandlerItem> optional=LazyOptional.of(()->new CollarStateHandlerItem() {
                @Nullable
                @Override
                public AbstractContainerMenu createMenu(int p_39954_, @NotNull Inventory p_39955_, @NotNull Player p_39956_) {
                    return state.get()==null?null: getMenu(p_39954_,p_39955_,p_39956_,state.get(),stack);
                }

                @Override
                public @NotNull Component getDisplayName() {
                    return stack.getHoverName();
                }
                @Override
                public CollarState getState() {
                    return state.get();
                }

                @Override
                public ItemStack getCollarItem() {
                    return CollarStateHandler.addTagToItem(state.get(),stack);
                }
            });
            @Override
            public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                if(cap==ToNeko.curiosItemCapability) return curiosOptional.cast();
                return cap== CollarCapabilityProvider.COLLAR_HANDLER_ITEM||cap==CollarCapabilityProvider.MENU_PROVIDER_ITEM?optional.cast():LazyOptional.empty();
            }
        };
    }

}
