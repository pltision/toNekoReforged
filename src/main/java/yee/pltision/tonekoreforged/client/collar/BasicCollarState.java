package yee.pltision.tonekoreforged.client.collar;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import yee.pltision.tonekoreforged.ToNekoCapabilityHelper;
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
        return CollarRendererInstances.BASIC_COLLAR_RENDERER.cast();
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
                bauble.initEntity(entity,this,i);
        }
        CollarState.super.entityTick(entity);
    }

    @Override
    public void initEntity(LivingEntity entity) {
        CollarState.super.initEntity(entity);
    }

    @Override
    public AbstractContainerMenu createMenuOnEntity(int id, Inventory inventory, Player player, LivingEntity entity) {
        return new BasicCollarMenu(id,inventory,this,p-> ToNekoCapabilityHelper.getCollar(entity).getState()==this);
    }

    public BasicCollarState(){}
    public BasicCollarState(ItemStack item){
        readItem(item);
    }
}