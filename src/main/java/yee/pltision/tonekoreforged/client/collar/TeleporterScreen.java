package yee.pltision.tonekoreforged.client.collar;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.collar.bauble.TeleporterMenu;

@OnlyIn(Dist.CLIENT)
public class TeleporterScreen extends AbstractContainerScreen<TeleporterMenu> {
    public static final ResourceLocation INVENTORY_LOCATION = ToNeko.location("textures/gui/ender_blot_menu.png");
    public TeleporterScreen(TeleporterMenu p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
        this.imageHeight=160;
        this.inventoryLabelY = this.imageHeight - 86;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float p_97788_, int p_97789_, int p_97790_) {
//        RenderSystem.setShaderTexture(0, TheEndPortalRenderer.END_SKY_LOCATION);
//        RenderSystem.setShader(GameRenderer::getRendertypeEndGatewayShader);
        Matrix4f matrix4f = graphics.pose().last().pose();
//        consumer.begin(RenderType.endGateway().mode(),RenderType.endGateway().format());
        graphics.blit(INVENTORY_LOCATION,leftPos, topPos, 0, this.imageHeight, this.imageWidth, 256-this.imageHeight);

        float maxWidth=142-34;
        float width=getMenu().containerData.get(1)==0?0:getMenu().containerData.get(0)*maxWidth/getMenu().containerData.get(1);

        RenderSystem.runAsFancy(() -> {
            BufferBuilder consumer= (BufferBuilder)graphics.bufferSource().getBuffer(RenderType.endGateway());

            //这玩意要求有正反顺序
            consumer.vertex(matrix4f,getGuiLeft()+34,  getGuiTop()+23,0    )/*.uv(0, 0)*/.endVertex();
            consumer.vertex(matrix4f,getGuiLeft()+34,  getGuiTop()+34,0    )/*.uv(0, 1)*/.endVertex();
            consumer.vertex(matrix4f,getGuiLeft()+34+width, getGuiTop()+34,0    )/*.uv(1, 1)*/.endVertex();
            consumer.vertex(matrix4f,getGuiLeft()+34+width, getGuiTop()+23,0    )/*.uv(1, 0)*/.endVertex();
        });
        graphics.flush();

//        BufferUploader.drawWithShader(consumer.end());

        graphics.blit(INVENTORY_LOCATION,leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}
