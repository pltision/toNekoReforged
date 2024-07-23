package yee.pltision.tonekoreforged;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import yee.pltision.tonekoreforged.client.nekoarmor.NekoArmorClientItemExtensions;
import yee.pltision.tonekoreforged.config.Config;
import yee.pltision.tonekoreforged.normalitem.NekoArmorMaterial;

import java.util.function.Consumer;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ToNeko.MODID)
public class ToNeko
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "to_neko";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Item> ITEMS=DeferredRegister.create(Registries.ITEM,MODID);

    public static final RegistryObject<Item> TAIL=ITEMS.register("tail",()->new ArmorItem(NekoArmorMaterial.TAIL, ArmorItem.Type.LEGGINGS,new Item.Properties()){
        @Override
        public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
            consumer.accept(NekoArmorClientItemExtensions.TAIL);
        }
    });

    public ToNeko()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        ITEMS.register(modEventBus);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }

}
