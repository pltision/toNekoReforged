package yee.pltision.tonekoreforged.collar.bauble;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.client.collar.CollarBaubleRenderer;
import yee.pltision.tonekoreforged.client.collar.InitCollarScreenContext;
import yee.pltision.tonekoreforged.collar.CollarState;

import java.util.function.Consumer;

public interface CollarBaubleState extends MenuProvider {
    String COLLAR_BAUBLE_TAG="CollarBauble";

    ItemStack asItem();

    @Nullable
    default  <T> CollarBaubleRenderer<T,?> getRenderer(T entity, CollarState collar){
        return null;
    }

    default void entityTick(LivingEntity entity){
    }

    default void initEntity(@Nullable LivingEntity entity, CollarState state, int slot){
    }

    /*@Override
    default CompoundTag serializeNBT(){
        return new CompoundTag();
    }

    @Override
    default void deserializeNBT(CompoundTag nbt){}*/

    default boolean mayPlace(BaublesAccessor baublesAccessor,Object slotAccessor,int slot){
        return true;
    }

    default Consumer<InitCollarScreenContext> initScreenButtonConsumer(){
        return v->{};
    }

    @Override
    default @NotNull Component getDisplayName(){
        return Component.empty();
    }

    @Nullable
    @Override
    default AbstractContainerMenu createMenu(int p_39954_, @NotNull Inventory p_39955_, @NotNull Player p_39956_) {
        return null;
    }
}
