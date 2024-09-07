package yee.pltision.tonekoreforged;

import com.mojang.logging.LogUtils;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
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
import yee.pltision.tonekoreforged.collar.curios.CuriosInterface;
import yee.pltision.tonekoreforged.config.Config;
import yee.pltision.tonekoreforged.enchentment.RobShearEnchantment;
import yee.pltision.tonekoreforged.item.NekoArmorMaterial;
import yee.pltision.tonekoreforged.network.NekoNetworks;
import yee.pltision.tonekoreforged.recipe.DyingTranslateRecipe;

import java.lang.reflect.Field;
import java.util.function.Consumer;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ToNeko.MODID)
public class ToNeko
{
    static boolean installedCurios;

    public static boolean useCuriosApi(){
        return installedCurios;
    }

    // Define mod id in a common place for everything to reference
    public static final String MODID = "toneko";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Item> ITEMS=DeferredRegister.create(Registries.ITEM,MODID);
    public static final DeferredRegister<Block> BLOCKS=DeferredRegister.create(Registries.BLOCK,MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER=DeferredRegister.create(Registries.RECIPE_SERIALIZER,MODID);
    public static final DeferredRegister<MenuType<?>> MENUS=DeferredRegister.create(Registries.MENU,MODID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS=DeferredRegister.create(Registries.SOUND_EVENT,MODID);
    public static final DeferredRegister<Enchantment> ENCHANTMENTS=DeferredRegister.create(Registries.ENCHANTMENT,MODID);

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

    public static final EnchantmentCategory SHEARS=EnchantmentCategory.create("shears",item -> item instanceof ShearsItem);

    public static class PublicDispenserBlock extends DispenserBlock{
        public PublicDispenserBlock(Properties p_52664_) {
            super(p_52664_);
        }

        @Override
        public @NotNull DispenseItemBehavior getDispenseMethod(@NotNull ItemStack p_52667_) {
            return super.getDispenseMethod(p_52667_);
        }
    }
    public static final RegistryObject<PublicDispenserBlock> PUBLIC_DISPENSER_BLOCK =BLOCKS.register("public_dispenser",()->new PublicDispenserBlock(BlockBehaviour.Properties.of()));

    //剥取
    public static final RegistryObject<Enchantment> ROB_SHEAR =ENCHANTMENTS.register("rob_shear",()->new RobShearEnchantment(Enchantment.Rarity.VERY_RARE,SHEARS, new EquipmentSlot[]{EquipmentSlot.MAINHAND}));


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
        BLOCKS.register(modEventBus);
        ENCHANTMENTS.register(modEventBus);

        NekoNetworks.register();
        modEventBus.addListener(ToNeko::afterComplete);
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
    public static CollarStateHandlerItem getItemCollarHandel(ItemStack item){
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
    public static @NotNull CollarSlotHandler getCollar(LivingEntity player){
        return player.getCapability(CollarCapabilityProvider.COLLAR_HANDLER,null).orElse(CollarCapabilityProvider.FALLBACK_CAPABILITY);
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

    public static void afterComplete(FMLLoadCompleteEvent event){
        if(Config.dispenserCanUseRobShear){
            DispenseItemBehavior behavior = PUBLIC_DISPENSER_BLOCK.get().getDispenseMethod(new ItemStack(Items.SHEARS));
            DispenserBlock.registerBehavior(Items.SHEARS.asItem(), (blockSource, item) -> {
                if (item.getEnchantmentLevel(ToNeko.ROB_SHEAR.get()) > 0 && RobShearEnchantment.tryShearLivingEntity(blockSource.getLevel(),
                        blockSource.getBlockState().hasProperty(DispenserBlock.FACING) ? blockSource.getPos().relative(blockSource.getBlockState().getValue(DispenserBlock.FACING)) : blockSource.getPos()
                        , item))
                    return item;
                return behavior.dispense(blockSource, item);
            });
        }

        try {
            Class<?> curiosCapability=Class.forName("top.theillusivec4.curios.api.CuriosCapability");
            Field field=curiosCapability.getField("ITEM");
            CuriosInterface.curiosItemCapability= (Capability<?>) field.get(null);

            installedCurios =true;
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException | ClassCastException ignored) {
        }

    }

}
