package yee.pltision.tonekoreforged.collar.curios;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.collar.CollarState;
import yee.pltision.tonekoreforged.config.Config;

import java.util.function.Supplier;

public class CuriosInterface {
    public static Capability<?> curiosItemCapability;

    public static ICapabilityProvider tryCreateCuriosHandel(LivingEntity entity){

        return new CuriosCollarCapabilityProvider(new Supplier<ICurioStacksHandler>() {
            ICurioStacksHandler handler;

            @Override
            public ICurioStacksHandler get() {
                if(handler==null){
                    ICuriosItemHandler curiosItemHandler=ToNeko.getCapability(entity,CuriosCapability.INVENTORY);
                    if(curiosItemHandler!=null){
                        handler= curiosItemHandler.getCurios().get(Config.curiosSlotType);
                    }
                }
                return handler;
            }
        });
    }

    public static LazyOptional<?> createCollarItemOptional(ItemStack stack, Supplier<CollarState> state){
        if(state.get()==null)return LazyOptional.of(()->state);
        else return LazyOptional.of(()->new ICurio() {
            @Override
            public ItemStack getStack() {
                return stack;
            }

            @Override
            public boolean canUnequip(SlotContext slotContext) {
                if(slotContext.entity()instanceof ServerPlayer serverPlayer) state.get().canTake(serverPlayer,slotContext.entity());
                return state.get().canTake(null,slotContext.entity());
            }
        });
    }
}
