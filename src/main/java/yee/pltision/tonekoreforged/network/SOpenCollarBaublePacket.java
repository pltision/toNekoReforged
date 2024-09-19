package yee.pltision.tonekoreforged.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.collar.CollarState;
import yee.pltision.tonekoreforged.collar.bauble.CollarBaubleState;

import java.util.function.Supplier;

public class SOpenCollarBaublePacket {
    public final int index;

    public SOpenCollarBaublePacket(int index){
        this.index=index;
    }

    public SOpenCollarBaublePacket(FriendlyByteBuf buf){
        index=buf.readVarInt();
    }

    public static void encode(SOpenCollarBaublePacket packet, FriendlyByteBuf buf){
        buf.writeVarInt(packet.index);
    }

    // In Packet class
    public static void handle(SOpenCollarBaublePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            ServerLevel level = player.serverLevel();
            if(player.containerMenu.getSlot(msg.index).container instanceof CollarState){
                CollarBaubleState state= ToNeko.getCollarBaubleState(player.containerMenu.getSlot(msg.index).getItem());
                if(state!=null){
                    level.getServer().execute(() -> handelPacket(msg,player));
//                    DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> handelPacket(msg,player));
                }
            }

        });
        ctx.get().setPacketHandled(true);
    }
    public static void handelPacket(SOpenCollarBaublePacket msg, ServerPlayer player){
        if(player.containerMenu.getSlot(msg.index).container instanceof CollarState){
            CollarBaubleState state= ToNeko.getCollarBaubleState(player.containerMenu.getSlot(msg.index).getItem());
            if(state!=null){
                player.openMenu(state);
            }
        }
    }
}
