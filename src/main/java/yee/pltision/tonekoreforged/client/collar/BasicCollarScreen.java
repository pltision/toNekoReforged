package yee.pltision.tonekoreforged.client.collar;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.collar.BasicCollarMenu;

@OnlyIn(Dist.CLIENT)
public class BasicCollarScreen extends AbstractContainerScreen<BasicCollarMenu> {
    public static final ResourceLocation INVENTORY_LOCATION = ToNeko.location("textures/gui/basic_collar_menu.png");
    public BasicCollarScreen(BasicCollarMenu p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
        this.imageWidth=178;
        this.imageHeight=148;
        this.titleLabelX = 10;
        this.titleLabelY = 5;
        this.inventoryLabelX = 10;
        this.inventoryLabelY = this.imageHeight - 94;
    }

//    Collection<AbstractButton> buttons=new LinkedList<>();

    @Override
    protected void init() {
        super.init();
        InitCollarScreenContext.acceptInit(this, this::addRenderableWidget);
    }


    @Override
    protected void renderBg(@NotNull GuiGraphics p_283065_, float p_97788_, int p_97789_, int p_97790_) {
        p_283065_.blit(INVENTORY_LOCATION,leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);

    }

    Container container=getMenu().getSlot(0).container;
    ItemStack[] containerItems=new ItemStack[container.getContainerSize()];
    @Override
    public void render(@NotNull GuiGraphics p_283479_, int p_283661_, int p_281248_, float p_281886_) {
        //有点生草的检测，懒了喵
        boolean changed=false;
        for(int i=0;i<containerItems.length;i++){
            changed|=container.getItem(i).equals(containerItems[i]);
            containerItems[i]=container.getItem(i);
        }
        if(changed)
            this.repositionElements();

        this.renderBackground(p_283479_);
        super.render(p_283479_, p_283661_, p_281248_, p_281886_);
        this.renderTooltip(p_283479_, p_283661_, p_281248_);
    }

}
