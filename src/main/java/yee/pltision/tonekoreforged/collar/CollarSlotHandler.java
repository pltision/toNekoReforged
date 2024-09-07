package yee.pltision.tonekoreforged.collar;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.network.CCollarStateChangePacket;
import yee.pltision.tonekoreforged.network.NekoNetworks;

public interface CollarSlotHandler extends CollarStateHandler{
    default boolean mayReplace(LivingEntity entity, ItemStack stack) {
        return stack.isEmpty()||stack.getCapability(CollarCapabilityProvider.COLLAR_HANDLER_ITEM).isPresent();
    }

    void setCollarSlot(ItemStack item);

    default void setCollarSlotAndSend(LivingEntity entity, ItemStack item){
        this.setCollarSlot(item);
        NekoNetworks.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(()->entity), new CCollarStateChangePacket(entity.getId(), getCollarItem()));
//        if(entity instanceof ServerPlayer player)
//            sendToClient(player,player);
    }

    default void sendToClient(ServerPlayer player, LivingEntity entity) {
        NekoNetworks.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new CCollarStateChangePacket(entity.getId(), getCollarItem()));
    }

    default boolean canTake(@Nullable ServerPlayer taker,LivingEntity entity){
        CollarState state=getState();
        return state != null && state.canTake(taker, entity);
    }
    default void entityTick(LivingEntity entity){
        CollarState state=getState();
        if(state!=null){
            state.entityTick(entity);
        }
    }
    default boolean disableSlotUi(){return true;}
}
