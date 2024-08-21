package yee.pltision.tonekoreforged.collar;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CollarMenu extends AbstractContainerMenu {
    CollarState state;
    protected CollarMenu(@Nullable MenuType<?> p_38851_, int p_38852_, CollarState state) {
        super(p_38851_, p_38852_);
        this.state=state;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player p_38941_, int p_38942_) {
        return null;
    }

    @Override
    public boolean stillValid(Player p_38874_) {
        return p_38874_.getCapability(CollarCapabilityProvider.COLLAR_HANDLER).orElse(CollarCapabilityProvider.FALLBACK_CAPABILITY).getState()==this.state;
    }
}
