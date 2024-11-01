package yee.pltision.tonekoreforged.collar;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.ToNekoCapabilityHelper;

public class PlayerCollarStateHandler implements CollarSlotHandler {
    CollarState collarState;
    ItemStack item;
    Player player;

    public PlayerCollarStateHandler(Player player){
        item=ItemStack.EMPTY;
        this.player=player;
    }

    @Override
    public @Nullable CollarState getState() {
        return collarState;
    }

    @Override
    public ItemStack getCollarItem() {
        return CollarStateHandler.addTagToItem(getState(),item);
    }

    @Override
    public boolean canTake(@Nullable ServerPlayer taker, LivingEntity entity) {
        return CollarSlotHandler.super.canTake(taker, entity) && (!EnchantmentHelper.hasBindingCurse(item) );
    }

    @Override
    public void setCollarSlot(ItemStack item) {
        CollarStateHandlerItem handler= ToNekoCapabilityHelper.getItemCollarHandel(item);
        CollarState oldState=collarState;
        CollarState newState;
        if(handler!=null){
            newState=handler.getState();
            this.collarState=newState;
            this.item=item;
        }
        else {
            newState=null;
            this.collarState=null;
            this.item=ItemStack.EMPTY;
        }
        if(oldState!=newState){
            if(oldState!=null)
                oldState.unEquip(player);
            if(newState!=null)
                newState.initEntity(player);
        }
    }

    @Override
    public boolean disableSlotUi() {
        return false;
    }
}
