package yee.pltision.tonekoreforged.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import yee.pltision.tonekoreforged.ToNekoCapabilityHelper;
import yee.pltision.tonekoreforged.client.ClientNetworkHandler;
import yee.pltision.tonekoreforged.collar.CollarStateHandler;

import java.util.function.Supplier;

public class CCollarStateChangePacket {
    public final int entity;
    public final ItemStack item;

    public CCollarStateChangePacket(FriendlyByteBuf buf){
        this.entity=buf.readVarInt();
        this.item=buf.readItem();
    }

    public CCollarStateChangePacket(int entity, ItemStack stack){
        this.entity = entity;
        this.item=stack;
    }

    public static CCollarStateChangePacket tryBuild(LivingEntity entity){
        CollarStateHandler handler= ToNekoCapabilityHelper.getCollar(entity);
        return new CCollarStateChangePacket(entity.getId(),handler.getCollarItem());
    }

    public static void encode(CCollarStateChangePacket packet, FriendlyByteBuf buf){
        buf.writeVarInt(packet.entity);
        buf.writeItem(packet.item);
    }

    // In Packet class
    public static void handle(CCollarStateChangePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                // Make sure it's only executed on the physical client
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientNetworkHandler.handlePacket(msg, ctx))
        );
        ctx.get().setPacketHandled(true);
    }


}
