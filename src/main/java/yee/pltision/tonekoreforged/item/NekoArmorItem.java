package yee.pltision.tonekoreforged.item;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.type.capability.ICurio;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.collar.curios.CuriosInterface;

public class NekoArmorItem extends ArmorItem {
    public NekoArmorItem(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
        super(p_40386_, p_266831_, p_40388_);
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ICapabilityProvider() {
            final LazyOptional<?> optional=ToNeko.installedCurios()?LazyOptional.of(()-> (ICurio) () -> stack):LazyOptional.empty();

            @Override
            public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                return cap== ToNeko.curiosItemCapability?optional.cast():LazyOptional.empty();
            }

        };
    }

}
