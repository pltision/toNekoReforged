package yee.pltision.tonekoreforged.collar;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.collar.bauble.BorderBaubleSlotAccessor;
import yee.pltision.tonekoreforged.collar.bauble.CollarBaubleSlot;

public class BasicCollarMenu extends AbstractContainerMenu {
    public static final int SLOT_SIZE=5;

//    CollarStateHandler handler;
    CollarState state;
    Container container;
    //如果非空则为物品创建
    @Nullable ItemStack item;

    protected BasicCollarMenu(@Nullable MenuType<?> p_38851_, int p_38852_) {
        super(p_38851_, p_38852_);
    }
    public BasicCollarMenu(int p_38852_, Inventory inventory) {
        super(ToNeko.BASIC_COLLAR_MENU.get(), p_38852_);
        this.container=new SimpleContainer(SLOT_SIZE);
        initSlots(inventory);
    }
    public BasicCollarMenu(int p_38852_, Inventory inventory, @NotNull CollarState state, @Nullable ItemStack item) {
        super(ToNeko.BASIC_COLLAR_MENU.get(), p_38852_);
        this.state=state;
        this.item=item;
        container=state;
        initSlots(inventory);
    }
    public void initSlots(Inventory inventory){
        checkContainerSize(container,SLOT_SIZE);
        for(int i=0;i<container.getContainerSize();i++){
            addSlot(new CollarBaubleSlot(container,i,26+i*27,22, SlotAccessor.INSTANCE));
        }

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 9 + j * 18, 66 + i * 18));
            }
        }

        for(int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 9 + i * 18, 124));
        }
    }

    //抄自漏斗
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slotId) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotId);
        if (/*slot != null && */slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (slotId < this.container.getContainerSize()) {
                if (!this.moveItemStackTo(itemstack1, this.container.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.container.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(@NotNull Player p_38874_) {
        if(item==null)
            return p_38874_.getCapability(CollarCapabilityProvider.COLLAR_HANDLER).orElse(CollarCapabilityProvider.FALLBACK_CAPABILITY).getState()==this.state;
        else
            return !item.isEmpty() && item.getCapability(CollarCapabilityProvider.COLLAR_HANDLER_ITEM).isPresent();//我看Useful-Backpacks是这么做的
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        if(item!=null&&state!=null){
            CollarStateHandler.addTagToItem(state,item);
        }
    }
    public static class SlotAccessor implements BorderBaubleSlotAccessor {
        public static final SlotAccessor INSTANCE=new SlotAccessor();

        @Override
        public boolean isFontSideSlot(int slot) {
            return slot==0;
        }

        @Override
        public boolean isLeftSideSlot(int slot) {
            return slot==1;
        }

        @Override
        public boolean isBackSideSlot(int slot) {
            return slot==2||slot==3;
        }

        @Override
        public boolean isRightSideSlot(int slot) {
            return slot==4;
        }
    }
}
