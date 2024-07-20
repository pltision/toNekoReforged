package yee.pltision.tonekoreforged.collar;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.client.collar.CollarRenderHelper;
import yee.pltision.tonekoreforged.collar.bauble.CollarBaubleState;

import java.util.ArrayList;
import java.util.List;

public class CollarItem extends Item {
    public CollarItem(Properties p_41383_) {
        super(p_41383_);
    }
    @Nullable
    <E extends Entity> CollarRenderHelper<E,?> createRenderHelper(Entity entity, ItemStack collar){
        return null;
    }
    CollarState asState(){
        return new CollarState() {
            ArrayList<CollarBaubleState> baubles;
            @Override
            public List<CollarBaubleState> baubles() {
                return List.of();
            }

            @Override
            public ItemStack asItem() {
                return null;
            }

            @Override
            public @Nullable <E> CollarRenderHelper<E, ?> getCollarRenderHelper() {
                return null;
            }

            @Override
            public Component getDisplayName() {
                return null;
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int p_39954_, @NotNull Inventory p_39955_, @NotNull Player p_39956_) {
                return null;
            }
        };
    }

}
