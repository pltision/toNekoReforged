package yee.pltision.tonekoreforged.collar;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ItemSteerable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.client.collar.BasicCollarState;
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

    CollarState asState(ItemStack item){
        return new BasicCollarState();
    }



}
