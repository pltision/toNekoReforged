package yee.pltision.tonekoreforged.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
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

import static yee.pltision.tonekoreforged.datagen.TnDamageType.FALLING_END_ROD;
import static yee.pltision.tonekoreforged.datagen.TnDamageType.FALL_ON_END_ROD;

@Mod.EventBusSubscriber(modid = ToNeko.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGenerator {
    @SubscribeEvent
    public static void generateData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output=generator.getPackOutput();

        RegistrySetBuilder builder=new RegistrySetBuilder();
        builder.add(Registries.DAMAGE_TYPE,TnDamageType::bootstrap);

        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider().thenApply(provider ->
                builder.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), provider));

        generator.addProvider(event.includeServer(), (DataProvider.Factory<DatapackBuiltinEntriesProvider>) o-> new DatapackBuiltinEntriesProvider(o,lookup,builder,Set.of(ToNeko.MODID)));

        generator.addProvider(event.includeServer(),new DamageTypeTagsProvider(output,lookup,ToNeko.MODID,null){
            @Override
            protected void addTags(HolderLookup.@NotNull Provider p_270108_) {
                this.tag(DamageTypeTags.DAMAGES_HELMET).add( FALLING_END_ROD);
                this.tag(DamageTypeTags.BYPASSES_SHIELD).add( FALLING_END_ROD);
                this.tag(DamageTypeTags.IS_FALL).add(FALL_ON_END_ROD);
            }
        });

    }

}
