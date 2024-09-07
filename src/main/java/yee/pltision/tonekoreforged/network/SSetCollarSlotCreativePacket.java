package yee.pltision.tonekoreforged.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.collar.CollarSlotHandler;

import java.util.function.Supplier;

public class SSetCollarSlotCreativePacket {
    public ItemStack item;

    public SSetCollarSlotCreativePacket(ItemStack item){
        this.item=item;
    }

    public SSetCollarSlotCreativePacket(FriendlyByteBuf buf){
        item=buf.readItem();
    }

    public static void encode(SSetCollarSlotCreativePacket packet, FriendlyByteBuf buf){
        buf.writeItem(packet.item);
    }

    // In Packet class
    public static void handle(SSetCollarSlotCreativePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null||!player.isCreative()|| ToNeko.getCollar(player).disableSlotUi()) return;
            ServerLevel level = player.serverLevel();
            level.getServer().execute(() -> handelPacket(msg,player));
            DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> handelPacket(msg,player));
        });
        ctx.get().setPacketHandled(true);
    }
    public static void handelPacket(SSetCollarSlotCreativePacket msg, ServerPlayer player){
        if(player.isCreative()){
            CollarSlotHandler handler=ToNeko.getCollar(player);
            handler.setCollarSlotAndSend(player,msg.item);
        }
    }
}
