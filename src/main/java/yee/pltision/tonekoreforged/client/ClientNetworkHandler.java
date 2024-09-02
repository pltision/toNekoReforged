package yee.pltision.tonekoreforged.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.collar.CollarSlotHandler;
import yee.pltision.tonekoreforged.network.CCollarStateChangePacket;

import java.util.function.Supplier;

public class ClientNetworkHandler {
    // In ClientPacketHandlerClass
    public static void handlePacket(CCollarStateChangePacket msg, Supplier<NetworkEvent.Context> ctx) {
        // Do stuff
        Player player=Minecraft.getInstance().player;
        if(player!=null){
            if (
                    player.level().getEntity(msg.entity) instanceof LivingEntity entity
            ) {
                CollarSlotHandler collar= ToNeko.getCollar(entity);
                collar.setCollarSlot(msg.item);
            }
        }
    }

}
