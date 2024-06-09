package yee.pltision.tonekoreforged.client;

import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static yee.pltision.tonekoreforged.ToNeko.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RenderModify {


    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event){
        System.out.println("开始添加Layers");
        for(String skin:event.getSkins()){
            if(event.getSkin(skin) instanceof  PlayerRenderer playerRenderer) {
                System.out.println("找到了"+playerRenderer);
                playerRenderer.addLayer(new CollarLayout<>(playerRenderer));
            }
        }
    }
}
