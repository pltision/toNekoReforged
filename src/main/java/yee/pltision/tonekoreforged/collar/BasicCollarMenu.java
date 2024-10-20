package yee.pltision.tonekoreforged.collar;

import net.minecraft.server.level.ServerPlayer;
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
import yee.pltision.tonekoreforged.collar.bauble.BaublesAccessor;
import yee.pltision.tonekoreforged.collar.bauble.BorderBaubleSlotAccessor;
import yee.pltision.tonekoreforged.collar.bauble.CollarBaubleSlot;

import java.util.Set;
import java.util.function.Function;

public class BasicCollarMenu extends AbstractContainerMenu implements CollarMenu {
    public static final int SLOT_SIZE=5;

//    CollarStateHandler handler;
    CollarState state;
    Container container;
    //如果非空则为物品创建
//    @Nullable ItemStack item;
    @Nullable Function<Player,Boolean> validChecker;
    Runnable broadcastChanges=()->{};
    int itemSlot=-1;
    ServerPlayer player;

    protected BasicCollarMenu(@Nullable MenuType<?> p_38851_, int p_38852_) {
        super(p_38851_, p_38852_);
    }
    public BasicCollarMenu(int p_38852_, Inventory inventory) {
        super(ToNeko.BASIC_COLLAR_MENU.get(), p_38852_);
        this.container=new SimpleContainer(SLOT_SIZE);
        initSlots(inventory);
    }
    public BasicCollarMenu(int p_38852_, Inventory inventory, @NotNull CollarState state, @Nullable Function<Player,Boolean> validChecker) {
        super(ToNeko.BASIC_COLLAR_MENU.get(), p_38852_);
        this.state=state;
//        this.item=item;
        this.validChecker=validChecker;
        container=state;
        initSlots(inventory);
    }
    public BasicCollarMenu(int p_38852_, Inventory inventory, @NotNull CollarState state, ItemStack item) {
        this(p_38852_, inventory, state,
                p-> !item.isEmpty() && item.getCapability(CollarCapabilityProvider.COLLAR_HANDLER_ITEM).isPresent() //我看Useful-Backpacks是这么做的
        );
        broadcastChanges=()-> CollarStateHandler.addTagToItem(state, item);
    }
    public void initSlots(Inventory inventory){
        checkContainerSize(container,SLOT_SIZE);
        BaublesAccessor baublesAccessor=BaublesAccessor.of(container);
        for(int i=0;i<container.getContainerSize();i++){
            addSlot(new CollarBaubleSlot(container,i,26+i*27,22,baublesAccessor ,SlotAccessor.INSTANCE));
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
        return validChecker!=null&&validChecker.apply(p_38874_);
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        broadcastChanges.run();
    }

    @Override
    public BaublesAccessor createBaubleAccessor() {
        return BaublesAccessor.of(container);
    }

    public static class SlotAccessor implements BorderBaubleSlotAccessor {
        public static final SlotAccessor INSTANCE=new SlotAccessor();

        public static final Set<Integer> FONT=Set.of(0);
        public static final Set<Integer> LEFT=Set.of(1);
        public static final Set<Integer> BACK=Set.of(2,3);
        public static final Set<Integer> RIGHT=Set.of(4);

        public Set<Integer> fontSideSlots(){
            return FONT;
        }
        public Set<Integer> leftSideSlots(){
            return LEFT;
        }
        public Set<Integer> backSideSlots(){
            return BACK;
        }
        public Set<Integer> rightSideSlots(){
            return RIGHT;
        }
    }
}
