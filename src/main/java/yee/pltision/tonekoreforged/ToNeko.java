package yee.pltision.tonekoreforged;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import yee.pltision.tonekoreforged.client.nekoarmor.NekoArmorClientItemExtensions;
import yee.pltision.tonekoreforged.collar.*;
import yee.pltision.tonekoreforged.collar.bauble.BellItem;
import yee.pltision.tonekoreforged.collar.bauble.CollarBaubleHandel;
import yee.pltision.tonekoreforged.collar.bauble.CollarBaubleState;
import yee.pltision.tonekoreforged.config.Config;
import yee.pltision.tonekoreforged.item.NekoArmorMaterial;
import yee.pltision.tonekoreforged.network.NekoNetworks;
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
    public static final DeferredRegister<MenuType<?>> MENUS=DeferredRegister.create(Registries.MENU,MODID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS=DeferredRegister.create(Registries.SOUND_EVENT,MODID);

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

    public static final RegistryObject<Item> COLLAR=ITEMS.register("collar",()->new BasicCollarItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BELL=ITEMS.register("bell",()->new BellItem(new Item.Properties()));
    public static final RegistryObject<SoundEvent> BELL_SOUND=SOUND_EVENTS.register("item.bell.ding",()->SoundEvent.createVariableRangeEvent(ToNeko.location("item.bell.ding")));

    public static final RegistryObject<MenuType<BasicCollarMenu>> BASIC_COLLAR_MENU=MENUS.register("basic_collar",()-> new MenuType<>(BasicCollarMenu::new, FeatureFlagSet.of()));

    public ToNeko()
    {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        ITEMS.register(modEventBus);
        RECIPE_SERIALIZER.register(modEventBus);
        MENUS.register(modEventBus);
        SOUND_EVENTS.register(modEventBus);

        NekoNetworks.register();
    }

    public static ResourceLocation location(String name){
        return new ResourceLocation(MODID,name);
    }

    public static CollarBaubleHandel getCollarBaubleHandel(ItemStack item){
        return ToNeko.getCapability(item, CollarCapabilityProvider.COLLAR_BAUBLE_HANDEL_ITEM);
    }
    public static CollarBaubleState getCollarBaubleState(ItemStack item){
        CollarBaubleHandel handler= ToNeko.getCapability(item, CollarCapabilityProvider.COLLAR_BAUBLE_HANDEL_ITEM);
        return handler==null?null:handler.getBaubleState();
    }
    public static CollarStateHandlerItem getItemCollarState(ItemStack item){
        return ToNeko.getCapability(item, CollarCapabilityProvider.COLLAR_HANDLER_ITEM);
    }
    public static CollarSlotHandler getLocalPlayerCollar(@Nullable Player player){
        return  player==null? null:
                ToNeko.getCapability(player, CollarCapabilityProvider.COLLAR_HANDLER);
    }
    public static CollarState getCollarState(LivingEntity player){
        CollarSlotHandler handler= ToNeko.getCapability(player, CollarCapabilityProvider.COLLAR_HANDLER);
        return handler==null?null:handler.getState();
    }
    public static CollarSlotHandler getCollar(LivingEntity player){
        return ToNeko.getCapability(player, CollarCapabilityProvider.COLLAR_HANDLER);
    }

    public static <C> C getCapability(LivingEntity entity, Capability<C> cap){
        LazyOptional<C> capability=entity.getCapability(cap,null);
        return capability.isPresent()?
                capability.orElseThrow(()->new RuntimeException("LazyOptional is present but still throw exception!"))
                : null;
    }
    public static <C> C getCapability(ItemStack item, Capability<C> cap){
        LazyOptional<C> capability=item.getCapability(cap,null);
        return capability.isPresent()?
                capability.orElseThrow(()->new RuntimeException("LazyOptional is present but still throw exception!"))
                : null;
    }

}
