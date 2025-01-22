package yee.pltision.tonekoreforged.collar;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.network.CCollarStateChangePacket;
import yee.pltision.tonekoreforged.network.NekoNetworks;

public interface CollarSlotHandler extends CollarStateHandler{
    default boolean mayReplace(LivingEntity entity, ItemStack stack) {
        return ( canTake(null,entity)&&(stack.isEmpty()||stack.getCapability(CollarCapabilityProvider.COLLAR_HANDLER_ITEM).isPresent()) ) ||(entity instanceof Player entityPlayer&&entityPlayer.isCreative());
    }

    void setCollarSlot(ItemStack item);

    default void setCollarSlotAndSend(LivingEntity entity, ItemStack item){
        this.setCollarSlot(item);
    }

    default void sendToClient(ServerPlayer player, LivingEntity entity) {
    }

    default void tracking(LivingEntity entity) {
    }

    default boolean canTake(@Nullable ServerPlayer taker,LivingEntity entity){
        CollarState state=getState();
        return state == null || state.canTake(taker, entity);
    }
    default void entityTick(LivingEntity entity){
        CollarState state=getState();
        if(state!=null){
            state.entityTick(entity);
        }
    }
    default boolean disableSlotUi(){return true;}

    default ItemEntity dropWhenDeath(LivingEntity entity){
        CollarState state=getState();
        if(state!=null&&state.doDropWhenDeath(entity)){
            ItemStack item= getCollarItem();
            if (!item.isEmpty()) {
                return entity.spawnAtLocation(item);
            }
            setCollarSlot(ItemStack.EMPTY);
        }
        return null;
    }
    default boolean cloneWhenRespawn(LivingEntity entity){
        CollarState state=getState();
        return state!=null&& (
                !(
                        entity instanceof Player && !entity.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)
                )
                || !state.doDropWhenDeath(entity)
        );
    }

}
