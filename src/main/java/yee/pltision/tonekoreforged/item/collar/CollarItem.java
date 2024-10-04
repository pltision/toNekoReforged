package yee.pltision.tonekoreforged.item.collar;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.collar.*;
import yee.pltision.tonekoreforged.curios.CuriosInterface;

import java.util.function.Supplier;

public interface CollarItem {

    //内部使用，获取CollarState应使用能力系统。
    CollarState asState(ItemStack item);

    MenuProvider createMenuProvider(CollarState state, ItemStack item,InteractionHand hand);

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
                @Override
                public CollarState getState() {
                    return state.get();
                }

                @Override
                public ItemStack getCollarItem() {
                    return stack;
                }
            });
            @Override
            public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                if(cap==ToNeko.curiosItemCapability) return curiosOptional.cast();
                return cap== CollarCapabilityProvider.COLLAR_HANDLER_ITEM||cap==CollarCapabilityProvider.MENU_PROVIDER_ITEM?optional.cast():LazyOptional.empty();
            }
        };
    }

    default InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack=player.getItemInHand(hand);
        if (level.isClientSide) {
            return InteractionResultHolder.success(itemStack);
        } else {
            CollarState state= ToNeko.getItemCollarHandel(itemStack).getState();
            MenuProvider handler= createMenuProvider(state,itemStack,hand);
            if (handler!=null) {
                player.openMenu(handler);
                PiglinAi.angerNearbyPiglins(player,true);
            }
            return InteractionResultHolder.consume(itemStack);
        }
    }

}
