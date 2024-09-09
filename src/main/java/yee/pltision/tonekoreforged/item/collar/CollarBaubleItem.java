package yee.pltision.tonekoreforged.item.collar;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.collar.CollarCapabilityProvider;
import yee.pltision.tonekoreforged.collar.MenuProviderItem;
import yee.pltision.tonekoreforged.collar.bauble.CollarBaubleHandel;
import yee.pltision.tonekoreforged.collar.bauble.CollarBaubleState;

public interface CollarBaubleItem extends MenuProviderItem {
    CollarBaubleState asCollarBaubleState(ItemStack stack);
    default AbstractContainerMenu getMenu(int id, @NotNull Inventory inventory, @NotNull Player player,CollarBaubleState state,ItemStack item){
        return null;
    }

    default ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ICapabilityProvider() {
            final LazyOptional<CollarBaubleHandel> optional=LazyOptional.of(()->new CollarBaubleHandel() {
                final CollarBaubleState state=asCollarBaubleState(stack);

                @Override
                public CollarBaubleState getBaubleState() {
                    return state;
                }

                @Override
                public ItemStack getCollarBaubleItem() {
                    return state.asItem();
                }

            });
            @Override
            public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                return cap==CollarCapabilityProvider.COLLAR_BAUBLE_HANDEL_ITEM||cap==CollarCapabilityProvider.MENU_PROVIDER_ITEM?optional.cast():LazyOptional.empty();
            }
        };
    }

}
