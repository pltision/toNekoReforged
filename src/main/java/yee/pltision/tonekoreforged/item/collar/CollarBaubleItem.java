package yee.pltision.tonekoreforged.item.collar;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.collar.CollarCapabilityProvider;
import yee.pltision.tonekoreforged.collar.MenuProviderItem;
import yee.pltision.tonekoreforged.collar.bauble.CollarBaubleHandel;
import yee.pltision.tonekoreforged.collar.bauble.CollarBaubleState;

import java.util.Optional;
import java.util.function.Consumer;

public interface CollarBaubleItem extends MenuProviderItem {
    CollarBaubleState asCollarBaubleState(ItemStack stack);

    default ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new CapabilityProvider(this,stack);
    }

    class CapabilityProvider implements ICapabilityProvider {
        public final CollarBaubleItem collarBaubleItem;
        public final ItemStack stack;

        final LazyOptional<CollarBaubleHandel> optional;

        public CapabilityProvider(CollarBaubleItem collarBaubleItem, ItemStack stack) {
            this.collarBaubleItem = collarBaubleItem;
            this.stack = stack;
            optional=LazyOptional.of(()->new CollarBaubleHandel() {

                final CollarBaubleState state=collarBaubleItem.asCollarBaubleState(stack);

                @Override
                public CollarBaubleState getBaubleState() {
                    return state;
                }

            });
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return cap==CollarCapabilityProvider.COLLAR_BAUBLE_HANDEL_ITEM||cap==CollarCapabilityProvider.MENU_PROVIDER_ITEM?optional.cast():LazyOptional.empty();
        }
    }
    abstract class CapabilitySerializable<T extends Tag,S extends CollarBaubleState> extends CapabilityProvider implements INBTSerializable<T>{

        public CapabilitySerializable(CollarBaubleItem collarBaubleItem, ItemStack stack) {
            super(collarBaubleItem, stack);

        }
        public S getState() throws ClassCastException{
            LazyOptional<CollarBaubleHandel> optional=this.optional;
            Optional<CollarBaubleHandel> stateHandelOptional=optional.resolve();
            if(optional.isPresent()){
                CollarBaubleHandel state= stateHandelOptional.orElseThrow();
                //noinspection unchecked
                return (S) state.getBaubleState();
            }
            return null;
        }

        public void ifPresent(Consumer<S> action){
            S state=getState();
            if(state!=null){
                action.accept(state);
            }
        }
    }
}
