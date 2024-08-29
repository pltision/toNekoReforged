package yee.pltision.tonekoreforged.collar.bauble;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.client.collar.CollarBaubleRenderer;
import yee.pltision.tonekoreforged.collar.CollarState;

public interface CollarBaubleState extends INBTSerializable<CompoundTag> {
    String COLLAR_BAUBLE_TAG="CollarBauble";

    ItemStack asItem();

    @Nullable
    default  <T> CollarBaubleRenderer<T,?> getRenderer(T entity, CollarState collar){
        return null;
    }

    default void entityTick(LivingEntity entity){
    }

    default void entityInit(LivingEntity entity, CollarState state, int slot){
    }

    @Override
    default CompoundTag serializeNBT(){
        return new CompoundTag();
    }

    @Override
    default void deserializeNBT(CompoundTag nbt){}

    default boolean mayPlace(Object slotAccessor,int slot){
        return true;
    }

}
