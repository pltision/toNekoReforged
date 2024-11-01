package yee.pltision.tonekoreforged.datagen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import yee.pltision.tonekoreforged.ToNeko;

public class TnDamageType {
    public static final ResourceKey<net.minecraft.world.damagesource.DamageType> FALL_ON_END_ROD = ResourceKey.create(Registries.DAMAGE_TYPE, ToNeko.location("fall_on_end_rod"));
    public static final ResourceKey<net.minecraft.world.damagesource.DamageType> FALLING_END_ROD = ResourceKey.create(Registries.DAMAGE_TYPE, ToNeko.location("falling_end_rod"));

    static void bootstrap(BootstapContext<DamageType>context) {
        context.register(FALL_ON_END_ROD, new DamageType("fallOnEndRod", 0.0F));
        context.register(FALLING_END_ROD, new DamageType("fallingEndRod", 0.1F));

    }
}
