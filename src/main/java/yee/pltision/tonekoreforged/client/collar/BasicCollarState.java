package yee.pltision.tonekoreforged.client.collar;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.collar.BasicCollarMenu;
import yee.pltision.tonekoreforged.collar.CollarState;
import yee.pltision.tonekoreforged.collar.bauble.CollarBaubleState;

import java.util.Arrays;
import java.util.List;

public class BasicCollarState implements CollarState {
    final List<CollarBaubleState> baubles= createBaubleList();
    final List<CollarBaubleState> lastBaubles= createLastBaubleRecord();
    @Override
    public List<CollarBaubleState> baubles() {
        return baubles;
    }

/*    @Override
    public ItemStack asItem() {
        return new ItemStack(ToNeko.COLLAR.get());
    }*/

    @Override
    public <E> CollarRenderHelper<E, ?> getCollarRenderHelper() {
        return CollarRenderer.INSTANT.cast();
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.empty();
    }

    public List<CollarBaubleState> createBaubleList(){
        return Arrays.asList(null,null,null,null,null);
    }

    //要比baubles初始化晚
    public List<CollarBaubleState> createLastBaubleRecord(){
        return Arrays.asList(new CollarBaubleState[baubles().size()]);
    }

    @Override
    public void entityTick(LivingEntity entity) {
        for(int i=0,to=/*Math.min(*/baubles.size()/*,lastBaubles.size())*/;i<to;i++){
            CollarBaubleState bauble=baubles.get(i);
            if(bauble!=null&&bauble==lastBaubles.get(i))
                bauble.entityInit(entity,this,i);
        }

        CollarState.super.entityTick(entity);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int p_39954_, @NotNull Inventory p_39955_, @NotNull Player p_39956_) {
        return new BasicCollarMenu(p_39954_,p_39955_,this,null);
    }
    BasicCollarState(){}
    public BasicCollarState(ItemStack item){
        readItem(item);
    }
}