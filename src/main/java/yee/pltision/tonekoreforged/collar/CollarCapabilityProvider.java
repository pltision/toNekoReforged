package yee.pltision.tonekoreforged.collar;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.collar.bauble.CollarBaubleHandel;
import yee.pltision.tonekoreforged.collar.lead.LeadItemHandel;
import yee.pltision.tonekoreforged.curios.CuriosInterface;
import yee.pltision.tonekoreforged.network.CCollarStateChangePacket;
import yee.pltision.tonekoreforged.network.NekoNetworks;

@Mod.EventBusSubscriber
public class CollarCapabilityProvider implements ICapabilitySerializable<CompoundTag> {
    public static final Capability<CollarSlotHandler> COLLAR_HANDLER = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<CollarStateHandlerItem> COLLAR_HANDLER_ITEM = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<CollarBaubleHandel> COLLAR_BAUBLE_HANDEL_ITEM = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<MenuProvider> MENU_PROVIDER_ITEM = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<LeadItemHandel> LEAD_ITEM_HANDEL = CapabilityManager.get(new CapabilityToken<>(){});

    public final LazyOptional<CollarSlotHandler> optional;
    public PlayerCollarStateHandler handler;

    public CollarCapabilityProvider(){
        optional= LazyOptional.of(this::createHandle);
        /*if(player instanceof ServerPlayer){
            optional.orElse(FALLBACK_CAPABILITY).setCollarSlot(player,new ItemStack(ToNeko.COLLAR.get()));
        }*/
    }

    public PlayerCollarStateHandler createHandle(){
        handler=new PlayerCollarStateHandler();
        return handler;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == COLLAR_HANDLER ? optional.cast():LazyOptional.empty();
    }
    public static CollarSlotHandler FALLBACK_CAPABILITY=new CollarSlotHandler() {
        @Override
        public CollarState getState() {
            return null;
        }

        @Override
        public ItemStack getCollarItem() {
            return ItemStack.EMPTY;
        }

        @Override
        public boolean mayReplace(LivingEntity entity, ItemStack stack) {
            return false;
        }

        @Override
        public void setCollarSlot(ItemStack item) {
        }
    };

    @SubscribeEvent
    public static void registryCapability(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject()instanceof Player entity){
            ICapabilityProvider provider=null;
            if (ToNeko.useCuriosApi()) provider = CuriosInterface.tryCreateCuriosHandel(entity);
            if(provider==null)provider=new CollarCapabilityProvider();
            event.addCapability(ToNeko.location("collar_state"), provider);
        }
    }


    @Override
    public CompoundTag serializeNBT() {
        CompoundTag main=new CompoundTag();

        if(handler!=null){  //理应不会为null罢，不过以防万一喵
            main.put("collar",handler.getCollarItem().serializeNBT());
        }

        return main;
    }

    @Override
    public void deserializeNBT(CompoundTag main) {
        @SuppressWarnings("SimplifyOptionalCallChains")
        CollarSlotHandler handler= optional.resolve().orElse(null); //调用optional也是为了以防万一
        if (handler != null) {
            handler.setCollarSlot(ItemStack.of(main.getCompound("collar")));
        }
    }

    @SubscribeEvent
    public static void clone(PlayerEvent.Clone event){
//        System.out.println(event.getOriginal().getCapability(COLLAR_HANDLER,null)+" "+event.getEntity().getCapability(COLLAR_HANDLER,null)+" "+LazyOptional.empty());
        event.getOriginal().reviveCaps();
        event.getOriginal().getCapability(COLLAR_HANDLER,null).ifPresent(origin->{
            if(!event.isWasDeath() || origin.cloneWhenRespawn(event.getOriginal())){
                event.getEntity().getCapability(COLLAR_HANDLER,null).ifPresent(
                        cap->cap.setCollarSlot(origin.getCollarItem())
                );
            }
        });
        event.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    public static void respawn(PlayerEvent.PlayerRespawnEvent event){
        event.getEntity().getCapability(COLLAR_HANDLER,null).ifPresent(cap->
                NekoNetworks.INSTANCE.send(
                        PacketDistributor.TRACKING_ENTITY_AND_SELF.with(event::getEntity),
                        new CCollarStateChangePacket(event.getEntity().getId(), cap.getCollarItem())
                )
        );
    }
}
