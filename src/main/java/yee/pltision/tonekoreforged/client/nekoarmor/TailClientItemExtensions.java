package yee.pltision.tonekoreforged.client.nekoarmor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

public class TailClientItemExtensions implements IClientItemExtensions {
    @Override
    public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
        return IClientItemExtensions.super.getHumanoidArmorModel(livingEntity, itemStack, equipmentSlot, original);
    }
}
