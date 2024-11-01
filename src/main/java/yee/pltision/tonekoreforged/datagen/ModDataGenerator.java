package yee.pltision.tonekoreforged.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import yee.pltision.tonekoreforged.ToNeko;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

import static yee.pltision.tonekoreforged.datagen.TnDamageType.FALLING_END_ROD;

@Mod.EventBusSubscriber(modid = ToNeko.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGenerator {
    @SubscribeEvent
    public static void generateData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output=generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookup=event.getLookupProvider();

        RegistrySetBuilder builder=new RegistrySetBuilder();

        builder.add(Registries.DAMAGE_TYPE,TnDamageType::bootstrap);

        generator.addProvider(event.includeServer(),bindRegistries((packOutput, providerCompletableFuture) ->new DatapackBuiltinEntriesProvider(packOutput,providerCompletableFuture,builder,Set.of(ToNeko.MODID)) , lookup));

        generator.addProvider(event.includeServer(),new DamageTypeTagsProvider(output,lookup,ToNeko.MODID,null){
            @Override
            protected void addTags(HolderLookup.@NotNull Provider p_270108_) {
                this.tag(DamageTypeTags.DAMAGES_HELMET).add( FALLING_END_ROD);
                this.tag(DamageTypeTags.BYPASSES_SHIELD).addTag(DamageTypeTags.BYPASSES_ARMOR).add( FALLING_END_ROD);
                this.tag(DamageTypeTags.IS_FALL).add(FALLING_END_ROD);
            }
        });

    }

    static <T extends DataProvider> DataProvider.Factory<T> bindRegistries(BiFunction<PackOutput, CompletableFuture<HolderLookup.Provider>, T> p_256618_, CompletableFuture<HolderLookup.Provider> p_256515_) {
        return (p_255476_) -> p_256618_.apply(p_255476_, p_256515_);
    }
}
