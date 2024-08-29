package yee.pltision.tonekoreforged.client.collar;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.collar.BasicCollarMenu;

@OnlyIn(Dist.CLIENT)
public class BasicCollarScreen extends AbstractContainerScreen<BasicCollarMenu> {
    public static final ResourceLocation INVENTORY_LOCATION = ToNeko.location("textures/gui/basic_collar_menu.png");
    public BasicCollarScreen(BasicCollarMenu p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, Component.translatable("container.toneko.basic_collar"));
        this.imageWidth=178;
        this.imageHeight=148;
        this.titleLabelX = 10;
        this.titleLabelY = 5;
        this.inventoryLabelX = 10;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics p_283065_, float p_97788_, int p_97789_, int p_97790_) {
        p_283065_.blit(INVENTORY_LOCATION,leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);

    }

}
