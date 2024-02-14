package yee.pltision.tonekoreforged;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = ToNeko.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue REMOVE_SET_WHEN_REMOVED_ALL_OWNER = BUILDER
            .comment("If true, when command removed a neko's all owner, the neko will be not a neko.")
            .comment("如果为true，当使用命令移除了一只猫娘的所有主人后，它就不是猫娘了。")
            .define("removeSetWhenRemovedAllOwner", true);


    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean removeSetWhenRemovedAllOwner;
    public static Set<Item> items;

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        removeSetWhenRemovedAllOwner = REMOVE_SET_WHEN_REMOVED_ALL_OWNER.get();

    }
}
