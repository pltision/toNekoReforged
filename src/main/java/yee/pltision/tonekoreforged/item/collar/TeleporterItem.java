package yee.pltision.tonekoreforged.item.collar;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.client.collar.BellRenderer;
import yee.pltision.tonekoreforged.client.collar.CollarBaubleRenderer;
import yee.pltision.tonekoreforged.client.collar.InitCollarScreenContext;
import yee.pltision.tonekoreforged.collar.CollarState;
import yee.pltision.tonekoreforged.collar.bauble.*;

import java.util.function.Consumer;

public class TeleporterItem extends Item implements CollarBaubleItem{
    public TeleporterItem(Properties p_41383_) {
        super(p_41383_);
    }

    public CollarBaubleState asCollarBaubleState(ItemStack item) {
        return new TeleporterState(item);
    }
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level p_41432_, @NotNull Player p_41433_, @NotNull InteractionHand p_41434_) {
        return CollarBaubleItem.super.menuProviderItem$use(p_41432_, p_41433_, p_41434_);
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new CapabilitySerializable<CompoundTag,TeleporterState>(this, stack) {
            @Override
            public CompoundTag serializeNBT() {
                CompoundTag main=new CompoundTag();

                ifPresent(s-> main.putInt("values",s.get(0)));

                return main;
            }

            @Override
            public void deserializeNBT(CompoundTag nbt) {
                ifPresent(s-> s.set(0,nbt.getInt("values")));
            }

        }/*new CapabilityProvider(this, stack)*/;
    }
}