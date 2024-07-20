package yee.pltision.tonekoreforged.collar.bauble;

import net.minecraft.client.model.Model;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.client.collar.CollarBaubleRenderer;
import yee.pltision.tonekoreforged.collar.CollarState;

public interface CollarBaubleState {
    default boolean isTrueModel(Model model){return true;}

    ItemStack asItem();

    @OnlyIn(Dist.CLIENT)
    @Nullable
    <T> CollarBaubleRenderer<T,?> getRenderer(T entity, CollarState collar);
}
