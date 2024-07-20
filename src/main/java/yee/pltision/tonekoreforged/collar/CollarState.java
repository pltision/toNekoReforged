package yee.pltision.tonekoreforged.collar;

import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.client.collar.CollarRenderHelper;
import yee.pltision.tonekoreforged.collar.bauble.CollarBaubleState;

import java.util.List;

public interface CollarState extends MenuProvider {
    List<CollarBaubleState> baubles();

    ItemStack asItem();

    @OnlyIn(Dist.CLIENT)
    @Nullable
    <E> CollarRenderHelper<E,?> getCollarRenderHelper();
}
