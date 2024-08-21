package yee.pltision.tonekoreforged.client.collar;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.collar.CollarState;
import yee.pltision.tonekoreforged.collar.bauble.CollarBaubleState;

import java.util.List;

public class BasicCollarState implements CollarState {
    final List<CollarBaubleState> baubles=List.of(new CollarBaubleState() {
        @Override
        public ItemStack asItem() {
            return ItemStack.EMPTY;
        }

        @Override
        public <T> CollarBaubleRenderer<T,?> getRenderer(T entity, CollarState collar) {
            return (CollarBaubleRenderer<T,?>) BellRenderer.INSTANT;
        }
    });
    @Override
    public List<CollarBaubleState> baubles() {
        return baubles;
    }

    @Override
    public ItemStack asItem() {
        return new ItemStack(ToNeko.COLLAR.get());
    }

    @Override
    public <E> CollarRenderHelper<E, ?> getCollarRenderHelper() {
        return (CollarRenderHelper<E, ?>) CollarRenderer.INSTANT;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.empty();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int p_39954_, @NotNull Inventory p_39955_, @NotNull Player p_39956_) {
        return null;
    }

}