package yee.pltision.tonekoreforged.collar;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.client.collar.CollarRenderHelper;
import yee.pltision.tonekoreforged.collar.bauble.BaublesAccessor;
import yee.pltision.tonekoreforged.collar.bauble.CollarBaubleState;

import java.util.Collections;

public interface CollarState extends MenuProvider , INBTSerializable<CompoundTag>, Container, BaublesAccessor {
    String COLLAR_TAG_NAME="CollarItem";

    @Nullable
    default  <E> CollarRenderHelper<E,?> getCollarRenderHelper(){
        return null;
    }

    default boolean canTake(@Nullable ServerPlayer taker, LivingEntity entity){
        return true;
    }

    default void addTagToItem(ItemStack item){
        item.addTagElement(COLLAR_TAG_NAME,serializeNBT());
    }

    @Override
    default CompoundTag serializeNBT() {
        CompoundTag main=new CompoundTag();

        ListTag baubles=new ListTag();
        for(CollarBaubleState bauble:baubles()){
            CompoundTag tag=new CompoundTag();
            if(bauble!=null)
                bauble.asItem().save(tag);
            baubles.add(tag);
        }
        main.put("baubles",baubles);

        return main;
    }

    @Override
    default void deserializeNBT(CompoundTag nbt) {
        ListTag baubles= nbt.getList("baubles", CompoundTag.TAG_COMPOUND);

        int size=Math.min(baubles().size(),baubles.size());
        for(int i=0;i<size;i++){
            baubles().set(i,
                    ToNeko.getCollarBaubleState(ItemStack.of(baubles.getCompound(i)))
            );
        }

    }

    default void readItem(ItemStack item){
        CompoundTag collarTag= item.getTagElement(COLLAR_TAG_NAME);
        if(collarTag!=null)deserializeNBT(collarTag);
    }

    default void entityTick(LivingEntity entity){
        for(CollarBaubleState state:baubles()){
            if(state!=null)
                state.entityTick(entity);
        }
    }

    default boolean doDropWhenDeath(LivingEntity entity){
        return true;
    }

    // --- 容器 ---

    @Override
    default int getContainerSize() {
        return this.baubles().size();
    }

    @Override
    default boolean isEmpty() {
        for(CollarBaubleState bauble:this.baubles())
            if(bauble==null)return false;
        return true;
    }

    @Override
    default @NotNull ItemStack getItem(int p_18941_) {
        CollarBaubleState bauble=this.baubles().get(p_18941_);
        return bauble==null?ItemStack.EMPTY:bauble.asItem();
    }

    @Override
    default @NotNull ItemStack removeItem(int slot, int count) {
        CollarBaubleState bauble=this.baubles().get(slot);
        if(bauble!=null&&count>0) {
            baubles().set(slot,null);
            return bauble.asItem();
        }
        else
            return ItemStack.EMPTY;
    }

    @Override
    default @NotNull ItemStack removeItemNoUpdate(int p_18951_) {
        CollarBaubleState bauble=this.baubles().get(p_18951_);
        return bauble==null?ItemStack.EMPTY:bauble.asItem();
    }

    @Override
    default void setItem(int slot, @NotNull ItemStack item) {
        this.baubles().set(slot,ToNeko.getCollarBaubleState(item));
    }

    @Override
    default void setChanged() {

    }

    @Override
    default boolean stillValid(@NotNull Player p_18946_) {
        return true;
    }

    @Override
    default void clearContent() {
        Collections.fill(this.baubles(), null);
    }

    @Override
    default int getMaxStackSize() {
        return 1;
    }

    // ^^^ 容器 ^^^

}
