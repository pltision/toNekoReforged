package yee.pltision.tonekoreforged.collar;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.network.CCollarStateChangePacket;
import yee.pltision.tonekoreforged.network.NekoNetworks;

public interface CollarStateHandler
{
    @Nullable
    CollarState getState();
    default ItemStack getCollarSlot(){
        CollarState state=getState();
        return state==null?ItemStack.EMPTY:state.asItem();
    };
    default boolean mayReplace(LivingEntity entity, ItemStack stack) {
        return stack.getItem() instanceof CollarItem || stack.isEmpty();
    }
    void setCollarSlot(LivingEntity entity,ItemStack item);

    default void sendToClient(ServerPlayer player,LivingEntity entity){
        NekoNetworks.INSTANCE.send(PacketDistributor.PLAYER.with(()->player),new CCollarStateChangePacket(entity.getId(),getCollarSlot()));
    }

}
