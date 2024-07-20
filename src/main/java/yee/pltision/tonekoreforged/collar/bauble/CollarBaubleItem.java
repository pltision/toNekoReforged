package yee.pltision.tonekoreforged.collar.bauble;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.collar.CollarState;

import java.util.ArrayList;
import java.util.List;

public interface CollarBaubleItem {
    CollarBaubleState asCollarBaubleState();
}
