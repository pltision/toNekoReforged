package yee.pltision.tonekoreforged.collar;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import yee.pltision.tonekoreforged.ToNeko;

public interface MenuProviderItem {
    
    default InteractionResultHolder<ItemStack> menuProviderItem$use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack=player.getItemInHand(hand);
        if (level.isClientSide) {
            return InteractionResultHolder.success(itemStack);
        } else {
            MenuProvider handler= ToNeko.getCapability(itemStack,CollarCapabilityProvider.MENU_PROVIDER_ITEM);
            if (handler!=null) {
                player.openMenu(handler);
//                player.awardStat(Stats.OPEN_SHULKER_BOX);
                PiglinAi.angerNearbyPiglins(player,true);
            }
            return InteractionResultHolder.consume(itemStack);
        }
    }
}
