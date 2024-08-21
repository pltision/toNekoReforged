package yee.pltision.tonekoreforged.collar;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.client.collar.CollarRenderHelper;
import yee.pltision.tonekoreforged.collar.bauble.CollarBaubleState;
import yee.pltision.tonekoreforged.network.CCollarStateChangePacket;
import yee.pltision.tonekoreforged.network.NekoNetworks;

import java.util.List;

public interface CollarState extends MenuProvider {
    List<CollarBaubleState> baubles();

    ItemStack asItem();

    @Nullable
    <E> CollarRenderHelper<E,?> getCollarRenderHelper();

    default boolean canTake(LivingEntity player){
        return true;
    }


}
