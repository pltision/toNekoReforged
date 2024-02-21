package yee.pltision.tonekoreforged.neko.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.config.Config;
import yee.pltision.tonekoreforged.neko.object.NekoStateObject;

import java.io.IOException;
import java.util.Objects;

@Mod.EventBusSubscriber
public class NekoCapability implements ICapabilityProvider {
    public static final Capability<NekoStateObject> NEKO_STATE = CapabilityManager.get(new CapabilityToken<>(){});
    public final LazyOptional<NekoStateObject> optional;

    public NekoCapability(){
        optional=LazyOptional.of(NekoStateObject::new);
    }

    @SubscribeEvent
    public static void registryCapability(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject()instanceof Player){
            event.addCapability(new ResourceLocation(ToNeko.MODID,"neko_state"), new NekoCapability());
        }
    }

    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(NekoStateObject.class);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == NEKO_STATE ? optional.cast():LazyOptional.empty();
    }


    @SubscribeEvent
    public static void save(PlayerEvent.SaveToFile event){
        if(Config.doSave){
            event.getEntity().getCapability(NEKO_STATE).ifPresent(cap-> {
                try {
                    NbtIo.write(StateSerializeUtil.nekoState(cap),event.getPlayerFile("to_neko.dat"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

    }
    @SubscribeEvent
    public static void load(PlayerEvent.LoadFromFile event){
        if(Config.doSave){
            event.getEntity().getCapability(NEKO_STATE).ifPresent(cap-> {
                try {
                    StateSerializeUtil.nekoState(cap, Objects.requireNonNull(NbtIo.read(event.getPlayerFile("to_neko.dat"))));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (NullPointerException e){
                    ToNeko.LOGGER.info("Try read "+event.getPlayerFile("to_neko.dat")+" return null. Maybe data have not been created.");
                }
            });
        }
    }

    @SubscribeEvent
    public static void clone(PlayerEvent.Clone event){
        event.getOriginal().getCapability(NEKO_STATE).ifPresent(old->
                        event.getEntity().getCapability(NEKO_STATE).ifPresent(cap->StateSerializeUtil.nekoState(cap,StateSerializeUtil.nekoState(old))
                )
        );
    }

}
