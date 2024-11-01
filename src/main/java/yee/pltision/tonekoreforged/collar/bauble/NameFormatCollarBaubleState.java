package yee.pltision.tonekoreforged.collar.bauble;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.collar.CollarState;

public interface NameFormatCollarBaubleState extends CollarBaubleState {
    @Override
    ItemStack asItem();
    Component format(LivingEntity entity, Component entityName, Component displayName);

    @Override
    default void initEntity(@Nullable LivingEntity entity, CollarState state, int slot) {
        if(entity instanceof Player player)
            player.refreshDisplayName();
    }

    @Override
    default void unEquip(LivingEntity entity, CollarState state, int slot) {
        if(entity instanceof ServerPlayer player)
            player.refreshDisplayName();
    }
}
