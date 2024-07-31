package yee.pltision.tonekoreforged.normalitem;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import yee.pltision.tonekoreforged.ToNeko;

public class NekoArmorMaterial implements ArmorMaterial {
    public static final NekoArmorMaterial EARS=new NekoArmorMaterial(ToNeko.location("ears").toString());
    public static final NekoArmorMaterial TAIL=new NekoArmorMaterial(ToNeko.location("tail").toString());

    String name;

    public NekoArmorMaterial(String name) {
        this.name = name;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type p_266807_) {
        return 0;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type p_267168_) {
        return 0;
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    @Override
    public @NotNull SoundEvent getEquipSound() {
        return SoundEvents.CAT_AMBIENT;
    }

    @Override
    public @NotNull Ingredient getRepairIngredient() {
        return Ingredient.EMPTY;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public float getToughness() {
        return 0;
    }

    @Override
    public float getKnockbackResistance() {
        return 0;
    }
}
