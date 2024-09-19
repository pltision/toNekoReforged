package yee.pltision.tonekoreforged.collar.bauble;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.collar.CollarState;

public class TeleporterMenu extends AbstractContainerMenu implements BaubleMenu {
    public static final ResourceLocation EMPTY_ENDER_PEARL = ToNeko.location("item/ender_bolt_empty_ender_pearl");
    protected TeleporterMenu(@Nullable MenuType<?> p_38851_, int p_38852_) {
        super(p_38851_, p_38852_);
    }

    ResultContainer result=new ResultContainer(){
        @Override
        public void setChanged() {
            super.setChanged();
            if(baubleState!=null){
                baubleState. setChanged();
                if(result.getItem(0).getCount()!=baubleState.getCanTake().getCount()) {
                    result.setItem(0, baubleState.getCanTake());
                }
            }
        }
    };
    Container container;
    public ContainerData containerData;
    TeleporterState baubleState;

    public TeleporterMenu(int p_38852_, Inventory inventory){
        this(p_38852_,inventory,new SimpleContainer(1),new SimpleContainerData(2));
    }

    public TeleporterMenu(int p_38852_, Inventory inventory, Container container,ContainerData data) {
        super(ToNeko.ENDER_BLOT_MENU.get(), p_38852_);

        this.addSlot(new Slot(container, 0, 6, 21) {

            public boolean mayPlace(@NotNull ItemStack p_39746_) {
                return p_39746_.is(Items.ENDER_PEARL);
            }
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, EMPTY_ENDER_PEARL);
            }
        });
        this.addSlot(new Slot(result, 1, 154, 21) {
            public boolean mayPlace(@NotNull ItemStack p_39746_) {
                return false;
            }

            @Override
            public @NotNull ItemStack remove(int p_40227_) {
                return super.remove(p_40227_).copy();
            }

            @Override
            public void onTake(Player p_150645_, ItemStack p_150646_) {
                super.onTake(p_150645_, p_150646_);
                if(baubleState!=null){
                    baubleState.take(p_150646_.getCount());
                }
            }
        });

        this.addDataSlots(data);

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 78 + i * 18));
            }
        }

        for(int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 136));
        }
        this.container=container;
        this.containerData=data;
    }

    public TeleporterMenu(int p_38852_, Inventory inventory,TeleporterState baubleState) {
        this(p_38852_,inventory,baubleState,baubleState);
        this.baubleState=baubleState;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player p_38941_, int p_38942_) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player p_38874_) {
        return baubleState!=null&&baubleState.stillValid(p_38874_);
    }

    @Override
    public @Nullable CollarState getCollarState() {
        return baubleState.state;
    }

    @Override
    public void removed(@NotNull Player p_38940_) {
        if(!p_38940_.level().isClientSide())
            this.clearContainer(p_38940_, this.container);
        super.removed(p_38940_);
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        result.setChanged();
    }
}
