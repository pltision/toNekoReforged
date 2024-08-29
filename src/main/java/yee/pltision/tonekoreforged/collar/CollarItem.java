package yee.pltision.tonekoreforged.collar;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.extensions.IForgeItem;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CollarItem extends IForgeItem,MenuProviderItem{

    //内部使用，获取CollarState应使用能力系统。
    CollarState asState(ItemStack item);

    AbstractContainerMenu getMenu(int id, @NotNull Inventory inventory, @NotNull Player player,CollarState state,ItemStack item);

    default ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ICapabilityProvider() {
            final LazyOptional<CollarStateHandlerItem> optional=LazyOptional.of(()->new CollarStateHandlerItem() {
                final CollarState state=asState(stack);

                @Nullable
                @Override
                public AbstractContainerMenu createMenu(int p_39954_, @NotNull Inventory p_39955_, @NotNull Player p_39956_) {
                    return state==null?null: getMenu(p_39954_,p_39955_,p_39956_,state,stack);
                }

                @Override
                public @NotNull Component getDisplayName() {
                    return Component.literal("项圈");
                }
                @Override
                public CollarState getState() {
                    System.out.println("getState "+state);
                    return state;
                }

                @Override
                public ItemStack getCollarItem() {
                    return CollarStateHandler.addTagToItem(state,stack);
                }
            });
            @Override
            public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                return cap==CollarCapabilityProvider.COLLAR_HANDLER_ITEM||cap==CollarCapabilityProvider.MENU_PROVIDER_ITEM?optional.cast():LazyOptional.empty();
            }
        };
    }
}
