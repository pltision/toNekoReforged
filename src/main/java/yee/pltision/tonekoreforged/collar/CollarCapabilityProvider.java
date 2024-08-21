package yee.pltision.tonekoreforged.collar;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.ToNeko;

@Mod.EventBusSubscriber
public class CollarCapabilityProvider implements ICapabilityProvider {
    public static final Capability<CollarStateHandler> COLLAR_HANDLER = CapabilityManager.get(new CapabilityToken<>(){});
    public final LazyOptional<CollarStateHandler> optional;

    public CollarCapabilityProvider(Player player){
        optional= LazyOptional.of(PlayerCollarStateHandler::new);
        if(player instanceof ServerPlayer){
            optional.orElse(FALLBACK_CAPABILITY).setCollarSlot(player,new ItemStack(ToNeko.COLLAR.get()));
        }
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == COLLAR_HANDLER ? optional.cast():LazyOptional.empty();
    }
    public static CollarStateHandler FALLBACK_CAPABILITY=new CollarStateHandler() {
        @Override
        public CollarState getState() {
            return null;
        }

        @Override
        public boolean mayReplace(LivingEntity entity, ItemStack stack) {
            return false;
        }

        @Override
        public void setCollarSlot(LivingEntity entity, ItemStack item) {
        }
    };

    @SubscribeEvent
    public static void registryCapability(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject()instanceof Player player){
            event.addCapability(ToNeko.location("collar_state"), new CollarCapabilityProvider(player));
        }
    }

}
