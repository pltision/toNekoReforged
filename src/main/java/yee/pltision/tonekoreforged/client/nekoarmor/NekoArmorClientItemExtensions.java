package yee.pltision.tonekoreforged.client.nekoarmor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

public interface NekoArmorClientItemExtensions {
    IClientItemExtensions TAIL=new IClientItemExtensions() {
        @Override
        public @NotNull Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
            return new TailModel(livingEntity,original);
        }
    };
    IClientItemExtensions EARS=new IClientItemExtensions() {
        @Override
        public @NotNull Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
            ArmorModelInstances.EARS.copyPropertiesTo(original);
            return ArmorModelInstances.EARS;
        }
    };
}
