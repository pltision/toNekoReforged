package yee.pltision.tonekoreforged.collar;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.client.collar.BasicCollarState;

public class BasicCollarItem extends Item implements CollarItem{
    public BasicCollarItem(Properties p_41383_) {
        super(p_41383_);
    }

    public CollarState asState(ItemStack item){
        return new BasicCollarState(item);
    }

    @Override
    public AbstractContainerMenu getMenu(int id, @NotNull Inventory inventory, @NotNull Player player, CollarState state, ItemStack item) {
        return new BasicCollarMenu(id,inventory,state,item);
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return CollarItem.super.initCapabilities(stack,nbt);
    }


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level p_41432_, @NotNull Player p_41433_, @NotNull InteractionHand p_41434_) {
        return CollarItem.super.use(p_41432_, p_41433_, p_41434_);
    }
}
