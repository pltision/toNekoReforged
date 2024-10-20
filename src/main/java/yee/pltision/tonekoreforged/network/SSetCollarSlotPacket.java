package yee.pltision.tonekoreforged.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.ToNekoCapabilityHelper;
import yee.pltision.tonekoreforged.collar.CollarSlotHandler;

import java.util.function.Supplier;

public class SSetCollarSlotPacket {
    public int containerId;
    public int stateId;
    public int slotNum;
    public int buttonNum;

    public SSetCollarSlotPacket(int containerId,int slotNum){
        this.containerId=containerId;
        this.slotNum=slotNum;
    }

    public SSetCollarSlotPacket(FriendlyByteBuf buf){
        containerId=buf.readVarInt();
        slotNum= buf.readVarInt();
    }

    public static void encode(SSetCollarSlotPacket packet, FriendlyByteBuf buf){
        buf.writeVarInt(packet.containerId);
        buf.writeVarInt(packet.slotNum);
    }

    // In Packet class
    public static void handle(SSetCollarSlotPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null|| ToNekoCapabilityHelper.getCollar(player).disableSlotUi()) return;
            ServerLevel level = player.serverLevel();
            level.getServer().execute(() -> handelPacket(msg,player));
//            DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> handelPacket(msg,player));
        });
        ctx.get().setPacketHandled(true);
    }
    public static void handelPacket(SSetCollarSlotPacket msg, ServerPlayer player){
//        try{

        player.resetLastActionTime();
        if (player.containerMenu.containerId == msg.containerId)
        {
            if (player.isSpectator()) {
                player.containerMenu.sendAllDataToRemote();
            } else if (!player.containerMenu.stillValid(player)) {
                ToNeko.LOGGER.debug("Player {} interacted with invalid menu {}", player, player.containerMenu);
            } else {
                int i = msg.slotNum;

                CollarSlotHandler collar = ToNekoCapabilityHelper.getLocalPlayerCollar(player);
                if (collar != null) {
                    ItemStack carried=player.containerMenu.getCarried();
                    if(i==-1){
                        if(collar.mayReplace(player,carried)){
                            player.containerMenu.setCarried(collar.getCollarItem());
                            collar.setCollarSlot(carried);
                            NekoNetworks.INSTANCE.send(
                                    PacketDistributor.TRACKING_ENTITY.with(()->player),
                                    new CCollarStateChangePacket(player.getId(), carried)
                            );
                        }

                }

                }
            }
        }

//        }
//        catch (Exception e){
//            ToNeko.LOGGER.error("[ToNeko Debug] {}", e.toString());
//        }
    }
}
