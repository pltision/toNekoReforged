package yee.pltision.tonekoreforged.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ContainerScreenEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.collar.CollarSlotHandler;
import yee.pltision.tonekoreforged.collar.CollarStateHandler;
import yee.pltision.tonekoreforged.network.NekoNetworks;
import yee.pltision.tonekoreforged.network.SSetCollarSlotCreativePacket;
import yee.pltision.tonekoreforged.network.SSetCollarSlotPacket;

import static yee.pltision.tonekoreforged.ToNeko.MODID;

@Mod.EventBusSubscriber(modid = MODID, /*bus = Mod.EventBusSubscriber.Bus.MOD, */value = Dist.CLIENT)
public class CollarSlotScreenListeners{
    public static final ResourceLocation COLLAR_SLOT= ToNeko.location("textures/gui/collar_slot.png");

    public static boolean isOpeningCollarSlot;
    public static boolean replacedCollarSlot;

    @SubscribeEvent
    public static void clickCollarSlot(ScreenEvent.MouseButtonPressed.Pre event){
        Vector2i pos=getHeadSlotPos(event.getScreen());
        if (pos == null) return;
        AbstractContainerScreen<?> screen;
        if(event.getScreen() instanceof AbstractContainerScreen<?> containerScreen)
            screen=containerScreen;
        else return;

        if(isOpeningCollarSlot&&isInCollarBord((int)event.getMouseX(),(int)event.getMouseY(),pos.x,pos.y))
        {
            if(isInCollarSlot((int)event.getMouseX(),(int)event.getMouseY(),collarSlotLeft(pos.x)-1,collarSlotTop(pos.y)-1)) {
                LivingEntity player=screen.getMinecraft().player;
                CollarSlotHandler collar = ToNeko.getLocalPlayerCollar(screen.getMinecraft().player);
                if (collar != null) {
                    ItemStack carried=screen.getMenu().getCarried();
                    if (screen instanceof CreativeModeInventoryScreen && event.getButton() == GLFW.GLFW_MOUSE_BUTTON_MIDDLE && carried.isEmpty()) {
                        if(carried.isEmpty()){
                            ItemStack collarItem=collar.getCollarItem();
                            if(!collarItem.isEmpty()){
                                screen.getMenu().setCarried(collarItem.copy());
                            }
                        }
                    }
                    else if(collar.mayReplace(player,carried)){
                        screen.getMenu().setCarried(collar.getCollarItem());
                        collar.setCollarSlot(carried);
                        if (screen instanceof CreativeModeInventoryScreen)
                            NekoNetworks.INSTANCE.sendToServer(new SSetCollarSlotCreativePacket(carried));
                        else{
                            NekoNetworks.INSTANCE.sendToServer(new SSetCollarSlotPacket(screen.getMenu().containerId, -1));
                        }
                        event.setCanceled(true);
                        screen.setDragging(false);
                        replacedCollarSlot=true;
                    }
                }
            }
        }

    }

    @SubscribeEvent
    public static void cancelRelease(ScreenEvent.MouseButtonReleased.Pre event){
        if(replacedCollarSlot&&(event.getScreen()instanceof InventoryScreen||(event.getScreen()instanceof CreativeModeInventoryScreen screen&&screen.isInventoryOpen())))
            event.setCanceled(true);
        replacedCollarSlot =false;
    }

    @SubscribeEvent
    public static void renderCollarSlot(ContainerScreenEvent.Render.Foreground event){
//        debugOutScreenPos(event);
        failed:
        {
            CollarSlotHandler collar = ToNeko.getLocalPlayerCollar(event.getContainerScreen().getMinecraft().player);
            if(collar==null|| collar.disableSlotUi()) break failed;

            Vector2i pos=getHeadSlotPos(event.getContainerScreen());
            if (pos == null) break failed;
            if(event.getContainerScreen()instanceof CreativeModeInventoryScreen screen&& !screen.isInventoryOpen())
                break failed;

            int x= event.getMouseX(), y= event.getMouseY();
            if(isInHeadSlot(x,y,pos.x-1,pos.y-1))
                isOpeningCollarSlot=true;
            else if(!isInCollarBord(x,y,pos.x-1,pos.y-1))
                isOpeningCollarSlot=false;

            if(isOpeningCollarSlot) renderCollarSlot(event,pos.x,pos.y,collar);

            return;
        }
        isOpeningCollarSlot = false;

    }
    public static Vector2i getHeadSlotPos(Screen screen){
        if (screen instanceof InventoryScreen inventoryScreen)
            return getHeadSlotPos(inventoryScreen);
        else if (screen instanceof CreativeModeInventoryScreen inventoryScreen)
            return getHeadSlotPos(inventoryScreen);
        else
            return null;
    }

    public static void renderCollarSlot(ContainerScreenEvent.Render event, int headX, int headY, CollarStateHandler collar){
        GuiGraphics graphics=event.getGuiGraphics();

        graphics.pose().pushPose();
        graphics.pose().translate(-event.getContainerScreen().getGuiLeft(), -event.getContainerScreen().getGuiTop(), 337.121334F);

        graphics.blit(COLLAR_SLOT, collarBordLeft(headX), collarBordTop(headY), 0, 0,4+18+2+18+4, 4+18+4);

        int collarSlotX=collarSlotLeft(headX),collarSlotY=collarSlotTop(headY);

        /*renderSlot(event.getContainerScreen(),graphics,new Slot(null,0,collarSlotX,collarSlotY){
            @Override
            public @NotNull ItemStack getItem() {
                return collar.getCollarSlot();
            }
        });*/
        ItemStack item=collar.getCollarItem();
        if(item.isEmpty()){
            graphics.blit(COLLAR_SLOT, collarSlotX-2, collarSlotY, 4+18+2+18+4, 0,4+18+2+18+4+16, 16);
        }
        else {
            graphics.pose().pushPose();
            graphics.pose().translate(0, 0, -150);

            graphics.renderItem(item, collarSlotX, collarSlotY);

            graphics.pose().popPose();

        }
        if(isInCollarSlot(event.getMouseX(),event.getMouseY(), collarSlotX-1, collarSlotY-1))
        {
            graphics.pose().pushPose();
            graphics.pose().translate(0, 0, 1337.121334F);

            AbstractContainerScreen.renderSlotHighlight(graphics, collarSlotX, collarSlotY, 0);
            if(!item.isEmpty())
                graphics.renderTooltip(Minecraft.getInstance().font, item,event.getMouseX(),event.getMouseY());

            graphics.pose().popPose();
        }
        
        graphics.pose().popPose();

    }

    /*public static final Method RENDER_SLOT_METHOD=getRenderSlotMethod();

    public static Method getRenderSlotMethod(){
        Method renderSlotMethod=null;
        var screenClass= AbstractContainerScreen.class;
        try {
            renderSlotMethod = screenClass.getDeclaredMethod("renderSlot", GuiGraphics.class, Slot.class);
            renderSlotMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            ToNeko.LOGGER.error("Cannot get renderSlot method in AbstractContainerScreen! You can try use Curios mod.\n{}\nMethods in class: {}", e,screenClass.getMethods());
        }
        return renderSlotMethod;
    }

    public static void renderSlot(AbstractContainerScreen<?> screen,GuiGraphics guiGraphics,Slot slot){
        try {
            RENDER_SLOT_METHOD.invoke(screen,guiGraphics,slot);
        } catch (IllegalAccessException | InvocationTargetException e) {
            ToNeko.LOGGER.error("Failed when using renderSlot method in AbstractContainerScreen!");
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println(RENDER_SLOT_METHOD);
    }*/

    public static int collarSlotLeft(int x){return x-18-2;}
    public static int collarSlotTop(int y){return y;}
    public static int collarBordLeft(int x){return collarSlotLeft(x)-4-1;}
    public static int collarBordTop(int y){return collarSlotTop(y)-4-1;}
    public static int collarBordRight(int x){return x+18+4;}
    public static int collarBordBottom(int y){return y+18+4;}

    public static boolean isInCollarBord(int mouseX,int mouseY,int headX,int headY){
        return mouseX>=collarBordLeft(headX) &&
                mouseX<collarBordRight(headX) &&
                mouseY>=collarBordTop(headY) &&
                mouseY<collarBordBottom(headY);
    }
    public static boolean isInHeadSlot(int mouseX,int mouseY,int headX,int headY){
        return mouseX>=headX &&
                mouseX<headX+18 &&
                mouseY>=headY &&
                mouseY<headY+18;
    }
    public static boolean isInCollarSlot(int mouseX,int mouseY,int collarX,int collarY){
        return mouseX>=collarX &&
                mouseX<collarX+18 &&
                mouseY>=collarY &&
                mouseY<collarY+18;
    }

    public static Vector2i getHeadSlotPos(InventoryScreen screen){
        Slot slot= screen.getMenu().getSlot(InventoryMenu.ARMOR_SLOT_START);
        return new Vector2i(screen.getGuiLeft()+slot.x,screen.getGuiTop()+slot.y);
    }

    /**
     *  See {@link CreativeModeInventoryScreen#selectTab(CreativeModeTab)}, line 519, 530
     */
    public static Vector2i getHeadSlotPos(CreativeModeInventoryScreen screen){
        return new Vector2i(screen.getGuiLeft()+54,screen.getGuiTop()+6);
    }

}
