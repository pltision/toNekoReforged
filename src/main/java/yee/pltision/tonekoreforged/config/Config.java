package yee.pltision.tonekoreforged.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import yee.pltision.tonekoreforged.ToNeko;

import java.util.List;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = ToNeko.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue REMOVE_SET_WHEN_REMOVED_ALL_OWNER = BUILDER
            .comment("If true, when command removed a neko's all owner, the neko will be not a neko.")
            .comment("如果为true，当使用命令移除了一只猫娘的所有主人后，它就不是猫娘了。")
            .define("removeSetWhenRemovedAllOwner", true);

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> CONFIG_LANG=ConfigLang.langInti();

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean removeStateWhenRemovedAllOwner;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        removeStateWhenRemovedAllOwner = REMOVE_SET_WHEN_REMOVED_ALL_OWNER.get();

        ConfigLang.load(CONFIG_LANG.get());
    }
}
