package yee.pltision.tonekoreforged.collar;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;
import yee.pltision.tonekoreforged.network.CCollarStateChangePacket;
import yee.pltision.tonekoreforged.network.NekoNetworks;

public interface CollarSlotHandler extends CollarStateHandler{
    default boolean mayReplace(LivingEntity entity, ItemStack stack) {
        return stack.isEmpty()||stack.getCapability(CollarCapabilityProvider.COLLAR_HANDLER_ITEM).isPresent();
    }

    void setCollarSlot(LivingEntity entity,ItemStack item);
    default void sendToClient(ServerPlayer player, LivingEntity entity) {
        NekoNetworks.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new CCollarStateChangePacket(entity.getId(), getCollarItem()));
    }
}
