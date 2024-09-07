package yee.pltision.tonekoreforged.collar.curios;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.collar.CollarSlotHandler;
import yee.pltision.tonekoreforged.collar.CollarStateHandlerItem;

import java.util.function.Supplier;

import static yee.pltision.tonekoreforged.collar.CollarCapabilityProvider.COLLAR_HANDLER;

public class CuriosCollarCapabilityProvider implements ICapabilitySerializable<CompoundTag> {
    public final LazyOptional<CollarSlotHandler> optional;

    public CuriosCollarCapabilityProvider(Supplier<ICurioStacksHandler> curiosItemHandler) {
        optional=LazyOptional.of(()->new CuriosCollarHandel(curiosItemHandler));

    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == COLLAR_HANDLER ? optional.cast():LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(CompoundTag main) {
        CollarSlotHandler handler= optional.resolve().orElse(null);
        if (handler != null) {
            CollarStateHandlerItem itemHandel= ToNeko.getItemCollarHandel(ItemStack.of(main.getCompound("collar")));
            if(itemHandel!=null&&itemHandel.getState() != null)
                handler.setCollarSlot(ItemStack.of(main.getCompound("collar")));

        }
    }
}
