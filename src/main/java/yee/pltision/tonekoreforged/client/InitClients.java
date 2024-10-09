package yee.pltision.tonekoreforged.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.client.collar.BasicCollarScreen;
import yee.pltision.tonekoreforged.client.collar.CollarLayout;
import yee.pltision.tonekoreforged.client.collar.CollarRendererInstances;
import yee.pltision.tonekoreforged.client.collar.TeleporterScreen;
import yee.pltision.tonekoreforged.client.nekoarmor.ArmorModelInstances;

import static yee.pltision.tonekoreforged.ToNeko.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class InitClients {

    @SubscribeEvent
    public static void registry(EntityRenderersEvent.RegisterLayerDefinitions event){
        ArmorModelInstances.registry(event);
        CollarRendererInstances.registry(event);
    }


    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event){
        for(String skin:event.getSkins()){
            if(event.getSkin(skin) instanceof  PlayerRenderer playerRenderer) {
                playerRenderer.addLayer(new CollarLayout<>(playerRenderer));
            }
        }
        ArmorModelInstances.initInstance(event.getContext());
        CollarRendererInstances.initInstance(event.getContext());
    }

    @SubscribeEvent
    public static void initItemColors(RegisterColorHandlersEvent.Item event){
        event.register((p_92708_, p_92709_) -> p_92709_ > 0 ? -1 : ((DyeableLeatherItem)p_92708_.getItem()).getColor(p_92708_), ToNeko.DYED_EARS.get(),ToNeko.DYED_TAIL.get());
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        MenuScreens.register(ToNeko.BASIC_COLLAR_MENU.get(), BasicCollarScreen::new);
        MenuScreens.register(ToNeko.ENDER_BLOT_MENU.get(), TeleporterScreen::new);
    }
}

