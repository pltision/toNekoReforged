package yee.pltision.tonekoreforged.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ContainerScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import static yee.pltision.tonekoreforged.ToNeko.MODID;

import org.joml.Vector2i;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.collar.CollarHandler;

@Mod.EventBusSubscriber(modid = MODID, /*bus = Mod.EventBusSubscriber.Bus.MOD, */value = Dist.CLIENT)
public class CollarSlotScreenListeners {
    public static final ResourceLocation COLLAR_SLOT= ToNeko.location("textures/gui/collar_slot.png");

    public static boolean isOpeningCollarSlot;

    /*@SubscribeEvent
    public static void renderCollarSlot(ContainerScreenEvent.Render.Background event){
        debugOutScreenPos(event);
    }*/

    public static void debugOutScreenPos(ContainerScreenEvent.Render event){
        System.out.println(event);
        System.out.println(event.getContainerScreen().getGuiLeft()+" "+event.getContainerScreen().getGuiTop());
    }

    @SubscribeEvent
    public static void renderCollarSlot(ContainerScreenEvent.Render.Background event){
//        debugOutScreenPos(event);
        failed:
        {
            Vector2i pos;
            if (event.getContainerScreen() instanceof InventoryScreen screen)
                pos = getHeadSlotPos(screen);
            else if (event.getContainerScreen() instanceof CreativeModeInventoryScreen screen)
                pos = getHeadSlotPos(screen);
            else
                break failed;

            if (pos == null) break failed;

            CollarHandler collar = ToNeko.getCollarHandler(event.getContainerScreen().getMinecraft().player);
            if(collar==null) break failed;

            int x= event.getMouseX(), y= event.getMouseY();
            if(isInHeadSlot(x,y,pos.x,pos.y))
                isOpeningCollarSlot=true;
            else if(!isInCollarBord(x,y,pos.x,pos.y))
                isOpeningCollarSlot=false;

            if(isOpeningCollarSlot) renderCollarSlot(event,pos.x,pos.y,collar);

            return;
        }
        isOpeningCollarSlot = false;

    }
    public static void renderCollarSlot(ContainerScreenEvent.Render event, int headX, int headY, CollarHandler collar){
        GuiGraphics graphics=event.getGuiGraphics();

        graphics.pose().pushPose();
        graphics.pose().translate(/*-event.getContainerScreen().getGuiLeft()*/0, /*-event.getContainerScreen().getGuiTop()*/0, 137.121334F);

        graphics.blit(COLLAR_SLOT, collarBordLeft(headX), collarBordTop(headY), 0, 0,4+18+2+18+4, 4+18+4);

        int collarSlotX=collarSlotLeft(headX),collarSlotY=collarSlotTop(headY);
        if(collar.getCollarSlot().isEmpty()){
            graphics.blit(COLLAR_SLOT, collarSlotX-1, collarSlotY, 4+18+2+18+4, 0,4+18+2+18+4+16, 16);
        }
        else {
            graphics.renderItem(collar.getCollarSlot(), collarSlotX, collarSlotY);
        }

        graphics.pose().popPose();

    }


    public static int collarSlotLeft(int x){return x-18-2-1;}
    public static int collarSlotTop(int y){return y;}
    public static int collarBordLeft(int x){return collarSlotLeft(x)-4;}
    public static int collarBordTop(int y){return collarSlotTop(y)-4-1;}
    public static int collarBordRight(int x){return x+4;}
    public static int collarBordBottom(int y){return y+18+4;}

    public static boolean isInCollarBord(int mouseX,int mouseY,int headX,int headY){
        return mouseX>=collarBordLeft(headX) &&
                mouseX<=collarBordRight(headX) &&
                mouseY>=collarBordTop(headY) &&
                mouseY<=collarBordBottom(headY);
    }
    public static boolean isInHeadSlot(int mouseX,int mouseY,int headX,int headY){
        return mouseX>=headX &&
                mouseX<=headX+18 &&
                mouseY>=headY &&
                mouseY<=headY+18;
    }

    public static Vector2i getHeadSlotPos(InventoryScreen screen){
        Slot slot= screen.getMenu().getSlot(InventoryMenu.ARMOR_SLOT_START);
        return new Vector2i(screen.getGuiLeft()+slot.x,screen.getGuiTop()+slot.y);
    }

    /**
     *  See {@link CreativeModeInventoryScreen#selectTab(CreativeModeTab)}, line 519, 530
     */
    public static @Nullable Vector2i getHeadSlotPos(CreativeModeInventoryScreen screen){
        return screen.isInventoryOpen()? new Vector2i(screen.getGuiLeft()+54,screen.getGuiTop()+6):null;
    }
}
