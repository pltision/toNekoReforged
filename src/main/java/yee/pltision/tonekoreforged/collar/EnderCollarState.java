package yee.pltision.tonekoreforged.collar;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;

public class EnderCollarState extends BasicCollarState implements TeleportCollarState, ContainerData {
    public EnderCollarState(){}
    public int enderPears;
    public int maxEndPears=64;


    public EnderCollarState(ItemStack item){
        readItem(item);
    }

    @Override
    public boolean canConsume() {
        return enderPears>0;
    }

    @Override
    public void consumeWhitTeleport() {
        enderPears--;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag= super.serializeNBT();
        tag.putInt("enderPowers",enderPears);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        enderPears=nbt.getInt("enderPowers");
    }

    @Override
    public int get(int p_39284_) {
        return p_39284_==0?enderPears:maxEndPears;
    }

    @Override
    public void set(int p_39285_, int p_39286_) {
        switch (p_39285_){
            case 0->enderPears=p_39286_;
            case 1->maxEndPears=p_39286_;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
