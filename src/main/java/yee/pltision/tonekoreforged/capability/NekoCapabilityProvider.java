package yee.pltision.tonekoreforged.capability;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.api.NekoState;
import yee.pltision.tonekoreforged.object.NekoStateObject;

public class NekoCapabilityProvider implements ICapabilityProvider {
    public static final Capability<NekoState> NEKO_STATE = CapabilityManager.get(new CapabilityToken<>(){});
    public static final LazyOptional<NekoState> optional =LazyOptional.of(NekoStateObject::new);

    @SubscribeEvent
    public static void registryCapability(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject()instanceof Player){
            event.addCapability(new ResourceLocation(ToNeko.MODID,"neko_state"), new NekoCapabilityProvider());
        }
    }
    @SubscribeEvent
    public void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(NekoState.class);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return optional.cast();
    }
}
