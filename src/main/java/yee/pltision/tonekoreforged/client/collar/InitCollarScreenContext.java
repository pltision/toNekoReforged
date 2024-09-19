package yee.pltision.tonekoreforged.client.collar;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.collar.CollarMenu;
import yee.pltision.tonekoreforged.collar.bauble.BaublesAccessor;
import yee.pltision.tonekoreforged.collar.bauble.CollarBaubleState;
import yee.pltision.tonekoreforged.network.NekoNetworks;
import yee.pltision.tonekoreforged.network.SOpenCollarBaublePacket;

import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public interface InitCollarScreenContext {
    AbstractContainerScreen<?> screen();
    BaublesAccessor accessor();
    Slot slot();

    <T extends GuiEventListener & Renderable & NarratableEntry>
    Consumer<T> renderableWidgetConsumer();

    static <
            T extends GuiEventListener & Renderable & NarratableEntry,
            M extends AbstractContainerMenu & CollarMenu
            >
    void acceptInit(AbstractContainerScreen<M> screen, Consumer<T> renderableWidgetConsumer) {
        BaublesAccessor accessor=screen.getMenu().createBaubleAccessor();
        for(int i=0;i<accessor.baubles().size();i++){
            int finalI = i;
            CollarBaubleState state=accessor.baubles().get(i);
            if(state!=null)
                state.initScreenButtonConsumer().accept(
                        new InitCollarScreenContext() {
                            @Override
                            public AbstractContainerScreen<?> screen() {
                                return screen;
                            }

                            @Override
                            public BaublesAccessor accessor() {
                                return accessor;
                            }

                            @Override
                            public Slot slot() {
                                return screen.getMenu().getSlot(finalI);
                            }

                            @Override
                            public Consumer<T> renderableWidgetConsumer() {
                                return renderableWidgetConsumer;
                            }
                        }
                );

        }
    }
    ResourceLocation ICONS_LOCATION = ToNeko.location("textures/gui/icons.png");
    static void addOpenMenuButton(InitCollarScreenContext c){
        c.renderableWidgetConsumer().accept(
                new Button.Builder(Component.translatable("gui.toneko.openCollarBaubleMenu"),b->
                        NekoNetworks.INSTANCE.sendToServer(new SOpenCollarBaublePacket(c.slot().index))
                )
                        .pos(c.slot().x+c.screen().getGuiLeft()+16,c.slot().y+c.screen().getGuiTop()+16-7)
                        .size(7,7)
                        .build(b->new Button(b){
                            @Override
                            protected void renderWidget(@NotNull GuiGraphics graphics, int x, int y, float p_282542_) {
                                graphics.pose().pushPose();
                                if(x>=getX()&&x<getWidth()+getX() && y>=getY()&&y<getHeight()+getY())
                                    graphics.setColor(1,1,1.25f,1);
                               graphics.blit(ICONS_LOCATION,getX(), getY(), 0, 0, getWidth(), getHeight());
                                if(x>=getX()&&x<getWidth()+getX() && y>=getY()&&y<getHeight()+getY())   //避免出颜色被锁定找不到原因
                                    graphics.setColor(1,1,1,1);
                                graphics.pose().popPose();

                            }
                        })
        );
    }
}
