package yee.pltision.tonekoreforged.collar.bauble;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.collar.CollarState;

public class TeleporterState extends AbstractCollarBaubleState implements Container, ContainerData {

    LivingEntity entity;

    int enderPearls;
    int maxCount=64;

    ItemStack input=ItemStack.EMPTY;
    ItemStack output=ItemStack.EMPTY;

    CollarState state;

    public TeleporterState(ItemStack item) {
        super(item);
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return input.isEmpty();
    }

    @Override
    public @NotNull ItemStack getItem(int p_18941_) {
        return p_18941_==0?input:ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int count) {
        /*if(slot==1){
            enderPearls-= Math.max(NumberUtils.min(count,output.getCount(),enderPearls),0);
            return output.split(count);
        }
        else{
            return input.split(count);
        }*/
        return slot==0?input.split(count):ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        if(slot==0){
            return input;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack item) {
        if(slot==0){
            input=item;
            if(input.is(Items.ENDER_PEARL)){
                int add=Math.min(maxCount-enderPearls,input.getCount());
                input.split(add);
                enderPearls+=add;
            }
        }
    }

    @Override
    public void setChanged() {
        if(enderPearls>0&&output.isEmpty())
            output = new ItemStack(Items.ENDER_PEARL);
        output.setCount(Math.min(output.getMaxStackSize(),enderPearls));

        if(input.is(Items.ENDER_PEARL)){
            int add=Math.min(maxCount-enderPearls,input.getCount());
            input.shrink(add);
            enderPearls+=add;
        }
    }

    @Override
    public boolean stillValid(@NotNull Player p_18946_) {
        return (state==null||state.stillValid(p_18946_))&&(entity==null|| state== ToNeko.getCollar(entity).getState());
    }

    @Override
    public void clearContent() {
        input=ItemStack.EMPTY;
    }

    @Override
    public void entityInit(LivingEntity entity, CollarState state, int slot) {
        super.entityInit(entity, state, slot);
        this.state=state;
        this.entity=entity;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag main=super.serializeNBT();
        main.putInt("values",enderPearls);
        return main;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        enderPearls= nbt.getInt("values");
    }

    public ItemStack getCanTake(){
        return output;
    }

    public void take(int count){
        enderPearls-=count;
        if(enderPearls<0)enderPearls=0;
//        setChanged();
    }

    @Override
    public int get(int p_39284_) {
        return p_39284_==0?enderPearls:maxCount;
    }

    @Override
    public void set(int p_39285_, int p_39286_) {
    }

    @Override
    public int getCount() {
        return 2;
    }
}
