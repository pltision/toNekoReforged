package yee.pltision.tonekoreforged.client;

import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.client.nekoarmor.ArmorModelInstances;

import static yee.pltision.tonekoreforged.ToNeko.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class InitClients {

    @SubscribeEvent
    public static void registry(EntityRenderersEvent.RegisterLayerDefinitions event){
        ArmorModelInstances.registry(event);
    }


    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event){
        /*System.out.println("开始添加Layers");
        for(String skin:event.getSkins()){
            if(event.getSkin(skin) instanceof  PlayerRenderer playerRenderer) {
                System.out.println("找到了"+playerRenderer);
                playerRenderer.addLayer(new CollarLayout<>(playerRenderer));
            }
        }*/
        ArmorModelInstances.initInstance(event.getContext());
    }

    @SubscribeEvent
    public static void initItemColors(RegisterColorHandlersEvent.Item event){
        event.register((p_92708_, p_92709_) -> p_92709_ > 0 ? -1 : ((DyeableLeatherItem)p_92708_.getItem()).getColor(p_92708_), ToNeko.DYED_EARS.get(),ToNeko.DYED_TAIL.get());
    }
}

