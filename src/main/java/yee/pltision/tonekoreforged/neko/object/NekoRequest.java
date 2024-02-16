package yee.pltision.tonekoreforged.neko.object;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber
public class NekoRequest {
    /**
     * key为owner，value为neko
     */
    public static BiMap<UUID,UUID> NEKO_QUESTS = HashBiMap.create();

    @SubscribeEvent
    public static void serverStarting(ServerStartedEvent event){
        NEKO_QUESTS.clear();
    }
}
