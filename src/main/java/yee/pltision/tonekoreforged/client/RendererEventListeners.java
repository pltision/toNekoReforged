package yee.pltision.tonekoreforged.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.client.collar.CollarLayout;
import yee.pltision.tonekoreforged.client.nekoarmor.ArmorModelInstances;
import yee.pltision.tonekoreforged.client.nekoarmor.EarsModel;

import static yee.pltision.tonekoreforged.ToNeko.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RendererEventListeners {

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
}
