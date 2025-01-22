package yee.pltision.tonekoreforged.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yee.pltision.tonekoreforged.ToNekoCapabilityHelper;
import yee.pltision.tonekoreforged.collar.CollarSlotHandler;

@Mod.EventBusSubscriber
public class ServerNetworkEvent {

    @SubscribeEvent
    public static void trackingEntity(PlayerEvent.StartTracking event){
//        System.out.println("yee2");
        if(event.getTarget() instanceof LivingEntity entity){
            ToNekoCapabilityHelper.getCollar(entity).tracking(entity);
        }
    }
    @SubscribeEvent
    public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event){
        if(event.getEntity() instanceof ServerPlayer player){
            ToNekoCapabilityHelper.getCollar(player).sendToClient(player,player);
        }
    }

    @SubscribeEvent
    public static void respawn(PlayerEvent.PlayerRespawnEvent event){
        if(event.getEntity() instanceof ServerPlayer player){
            ToNekoCapabilityHelper.getCollar(player).sendToClient(player,player);
        }
    }

    @SubscribeEvent
    public static void changedDim(PlayerEvent.PlayerChangedDimensionEvent event){
        if(event.getEntity() instanceof ServerPlayer player){
            ToNekoCapabilityHelper.getCollar(player).sendToClient(player,player);
        }
    }

}
