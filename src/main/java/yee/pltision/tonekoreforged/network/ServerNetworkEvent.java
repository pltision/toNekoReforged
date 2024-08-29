package yee.pltision.tonekoreforged.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.collar.CollarSlotHandler;

@Mod.EventBusSubscriber
public class ServerNetworkEvent {

    @SubscribeEvent
    public static void trackingEntity(PlayerEvent.StartTracking event){
        CollarSlotHandler handler;
//        System.out.println("yee2");
        if(
                event.getTarget() instanceof LivingEntity entity &&
                (handler= ToNeko.getCollar(entity))!=null
        ){
            ItemStack item=handler.getCollarItem();
            if( ( ! item.isEmpty() ) &&event.getEntity() instanceof ServerPlayer player) {
                handler.sendToClient(player,entity);
            }
        }
    }
    @SubscribeEvent
    public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event){
        CollarSlotHandler handler;
        if(
                event.getEntity() instanceof ServerPlayer player&&
                        (handler= ToNeko.getCollar(player))!=null
        ){
            handler.sendToClient(player,player);
        }
    }

}
