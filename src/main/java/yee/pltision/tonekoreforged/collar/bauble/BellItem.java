package yee.pltision.tonekoreforged.collar.bauble;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.client.collar.CollarBaubleRenderer;
import yee.pltision.tonekoreforged.collar.CollarState;

public class BellItem extends Item implements CollarBaubleItem {
    public BellItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public CollarBaubleState asCollarBaubleState() {
        return new CollarBaubleState() {
            @Override
            public ItemStack asItem() {
                return null;
            }

            @Override
            public @Nullable <T> CollarBaubleRenderer<T, ?> getRenderer(T entity, CollarState collar) {
                return null;
            }
        };
    }
}
