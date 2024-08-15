package yee.pltision.tonekoreforged;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
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
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import yee.pltision.tonekoreforged.client.nekoarmor.NekoArmorClientItemExtensions;
import yee.pltision.tonekoreforged.collar.CollarHandler;
import yee.pltision.tonekoreforged.collar.CollarCapabilityProvider;
import yee.pltision.tonekoreforged.config.Config;
import yee.pltision.tonekoreforged.item.NekoArmorMaterial;
import yee.pltision.tonekoreforged.recipe.DyingTranslateRecipe;

import java.util.function.Consumer;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ToNeko.MODID)
public class ToNeko
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "toneko";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Item> ITEMS=DeferredRegister.create(Registries.ITEM,MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER=DeferredRegister.create(Registries.RECIPE_SERIALIZER,MODID);
    public static final RegistryObject<RecipeSerializer<DyingTranslateRecipe>> DYE_TRANSLATE_RECIPE=RECIPE_SERIALIZER.register("crafting_dying_translate",()->
            DyingTranslateRecipe.Serializer.INSTANCE
    );

    public static final RegistryObject<Item> TAIL=ITEMS.register("tail",()->new ArmorItem(NekoArmorMaterial.TAIL, ArmorItem.Type.LEGGINGS,new Item.Properties()){
        @Override
        public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
            consumer.accept(NekoArmorClientItemExtensions.TAIL);
        }
    });
    public static final RegistryObject<Item> EARS=ITEMS.register("ears",()->new ArmorItem(NekoArmorMaterial.EARS, ArmorItem.Type.HELMET,new Item.Properties()){
        @Override
        public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
            consumer.accept(NekoArmorClientItemExtensions.EARS);
        }
    });
    public static final RegistryObject<Item> DYED_TAIL=ITEMS.register("dyed_tail",()->new DyeableArmorItem(NekoArmorMaterial.DYED_TAIL, ArmorItem.Type.LEGGINGS,new Item.Properties()){
        @Override
        public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
            consumer.accept(NekoArmorClientItemExtensions.TAIL);
        }
        @Override
        public int getColor(@NotNull ItemStack p_41122_) {
            CompoundTag compoundtag = p_41122_.getTagElement("display");
            return compoundtag != null && compoundtag.contains("color", 99) ? compoundtag.getInt("color") : 0xffffff;
        }
    });
    public static final RegistryObject<Item> DYED_EARS=ITEMS.register("dyed_ears",()->new DyeableArmorItem(NekoArmorMaterial.DYED_EARS, ArmorItem.Type.HELMET,new Item.Properties()){
        @Override
        public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
            consumer.accept(NekoArmorClientItemExtensions.EARS);
        }
        @Override
        public int getColor(@NotNull ItemStack p_41122_) {
            CompoundTag compoundtag = p_41122_.getTagElement("display");
            return compoundtag != null && compoundtag.contains("color", 99) ? compoundtag.getInt("color") : 0xffffff;
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
        RECIPE_SERIALIZER.register(modEventBus);
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

    public static ResourceLocation location(String name){
        return new ResourceLocation(MODID,name);
    }

    public static CollarHandler getCollarHandler(@Nullable Player player){
        return  player==null? null:
                ToNeko.getCapability(player, CollarCapabilityProvider.COLLAR_RECORD);
    }

    public static <C> C getCapability(LivingEntity entity, Capability<C> cap){
        LazyOptional<C> capability=entity.getCapability(cap,null);
        return capability.isPresent()?
                capability.orElseThrow(()->new RuntimeException("LazyOptional is peresent but still throw exception!"))
                : null;
    }

}
