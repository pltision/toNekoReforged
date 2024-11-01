package yee.pltision.tonekoreforged;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.collar.CollarCapabilityProvider;
import yee.pltision.tonekoreforged.collar.CollarSlotHandler;
import yee.pltision.tonekoreforged.collar.CollarState;
import yee.pltision.tonekoreforged.collar.CollarStateHandlerItem;
import yee.pltision.tonekoreforged.collar.bauble.CollarBaubleHandel;
import yee.pltision.tonekoreforged.collar.bauble.CollarBaubleState;

import java.util.function.Predicate;

public class ToNekoCapabilityHelper {
    public static CollarBaubleHandel getCollarBaubleHandel(ItemStack item){
        return getCapability(item, CollarCapabilityProvider.COLLAR_BAUBLE_HANDEL_ITEM);
    }
    public static CollarBaubleState getCollarBaubleState(ItemStack item){
        CollarBaubleHandel handler= getCapability(item, CollarCapabilityProvider.COLLAR_BAUBLE_HANDEL_ITEM);
        return handler==null?null:handler.getBaubleState();
    }
    public static CollarStateHandlerItem getItemCollarHandel(ItemStack item){
        return getCapability(item, CollarCapabilityProvider.COLLAR_HANDLER_ITEM);
    }
    public static CollarState getItemCollarState(ItemStack item){
        CollarStateHandlerItem handler= getCapability(item, CollarCapabilityProvider.COLLAR_HANDLER_ITEM);
        return handler==null?null:handler.getState();
    }
    public static CollarSlotHandler getLocalPlayerCollar(@Nullable Player player){
        return  player==null? null:
                getCapability(player, CollarCapabilityProvider.COLLAR_HANDLER);
    }
    public static CollarState getCollarState(LivingEntity entity){
        CollarSlotHandler handler= getCapability(entity, CollarCapabilityProvider.COLLAR_HANDLER);
        return handler==null?null:handler.getState();
    }
    public static @NotNull CollarSlotHandler getCollar(LivingEntity entity){
        return entity.getCapability(CollarCapabilityProvider.COLLAR_HANDLER,null).orElse(CollarCapabilityProvider.FALLBACK_CAPABILITY);
    }

    public static <C> C getCapability(LivingEntity entity, Capability<C> cap){
        LazyOptional<C> capability=entity.getCapability(cap,null);
        return capability.isPresent()?
                capability.orElseThrow(()->new RuntimeException("LazyOptional is present but still throw exception!"))
                : null;
    }
    public static <C> C getCapability(ItemStack item, Capability<C> cap){
        LazyOptional<C> capability=item.getCapability(cap,null);
        return capability.isPresent()?
                capability.orElseThrow(()->new RuntimeException("LazyOptional is present but still throw exception!"))
                : null;
    }
    @SuppressWarnings("unchecked")
    public static <B extends CollarBaubleState> B getBaubleOnEntity(LivingEntity entity, Class<B> baubleClass, Predicate<? super B> predicate){
        CollarState state=getCollar(entity).getState();
        if(state!=null)
            for(CollarBaubleState bauble:state.baubles()){
                if(baubleClass.isInstance(bauble) &&predicate.test((B)bauble)){
                    return (B) bauble;
                }
            }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <B extends CollarBaubleState> B getBaubleOnEntity(LivingEntity entity, Class<B> baubleClass){
        CollarState state=getCollar(entity).getState();
        if(state!=null)
            for(CollarBaubleState bauble:state.baubles()){
                if(baubleClass.isInstance(bauble)){
                    return (B) bauble;
                }
            }
        return null;
    }
}
