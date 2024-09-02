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
            if (player == null) return;
            ServerLevel level = player.serverLevel();
            level.getServer().execute(() -> handelPacket(msg,player));
            DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> handelPacket(msg,player));
        });
        ctx.get().setPacketHandled(true);
    }
    public static void handelPacket(SSetCollarSlotPacket msg, ServerPlayer player){

        player.resetLastActionTime();
//        System.out.println(player.containerMenu.containerId+" "+ msg.containerId);
        if (player.containerMenu.containerId == msg.containerId)
        {
            if (player.isSpectator()) {
                player.containerMenu.sendAllDataToRemote();
            } else if (!player.containerMenu.stillValid(player)) {
                ToNeko.LOGGER.debug("Player {} interacted with invalid menu {}", player, player.containerMenu);
            } else {
                int i = msg.slotNum;

                CollarSlotHandler collar = ToNeko.getLocalPlayerCollar(player);
                if (collar != null) {
                    ItemStack carried=player.containerMenu.getCarried();
                    if(i==-1){
                        if(collar.mayReplace(player,carried)){
//                            ItemStack collarItem=collar.getCollarItem();
                            player.containerMenu.setCarried(collar.getCollarItem());
                            collar.setCollarSlotAndSend(player,carried);
                        }

                }
                /*if (!player.containerMenu.isValidSlotIndex(i)) {
                    ToNeko.LOGGER.debug("Player {} clicked invalid slot index: {}, available slots: {}", player.getName(), i, player.containerMenu.slots.size());
                } else {
                    boolean flag = msg.getStateId() != player.containerMenu.getStateId();
                    player.containerMenu.suppressRemoteUpdates();
                    player.containerMenu.clicked(i, msg.getButtonNum(), msg.getClickType(), player);

                    for(Int2ObjectMap.Entry<ItemStack> entry : Int2ObjectMaps.fastIterable(msg.getChangedSlots())) {
                        player.containerMenu.setRemoteSlotNoCopy(entry.getIntKey(), entry.getValue());
                    }

                    player.containerMenu.setRemoteCarried(msg.getCarriedItem());
                    player.containerMenu.resumeRemoteUpdates();
                    if (flag) {
                        player.containerMenu.broadcastFullState();
                    } else {
                        player.containerMenu.broadcastChanges();
                    }*/

                }
            }
        }
    }
}
