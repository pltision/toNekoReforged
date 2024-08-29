package yee.pltision.tonekoreforged.collar;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yee.pltision.tonekoreforged.ToNeko;

@Mod.EventBusSubscriber
public class CollarStateEventListener{

    @SubscribeEvent
    public static void livingTick(LivingEvent.LivingTickEvent event){
        CollarState state= ToNeko.getCollarState(event.getEntity());
        if(state!=null)
            state.entityTick(event.getEntity());
    }
}
