package yee.pltision.tonekoreforged.curios;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.client.ICurioRenderer;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.client.nekoarmor.EarsModel;
import yee.pltision.tonekoreforged.client.nekoarmor.TailModel;
import yee.pltision.tonekoreforged.collar.CollarState;
import yee.pltision.tonekoreforged.config.Config;

import java.util.function.Supplier;

public class CuriosInterface {

    public static ICapabilityProvider tryCreateCuriosHandel(LivingEntity entity){

        return new CuriosCollarCapabilityProvider(new Supplier<ICurioStacksHandler>() {
            ICurioStacksHandler handler;

            @Override
            public ICurioStacksHandler get() {
                if(handler==null){
                    ICuriosItemHandler curiosItemHandler=ToNeko.getCapability(entity,CuriosCapability.INVENTORY);
                    if(curiosItemHandler!=null){
                        handler= curiosItemHandler.getCurios().get(Config.curiosSlotType);
                    }
                }
                return handler;
            }
        });
    }

    public static LazyOptional<?> createCollarItemOptional(ItemStack stack, Supplier<CollarState> state){
        if(state.get()==null)return LazyOptional.of(()->state);
        else return LazyOptional.of(()->new ICurio() {
            @Override
            public ItemStack getStack() {
                return stack;
            }

            @Override
            public boolean canUnequip(SlotContext slotContext) {
                if(slotContext.entity()instanceof ServerPlayer serverPlayer) state.get().canTake(serverPlayer,slotContext.entity());
                return state.get().canTake(null,slotContext.entity());
            }
        });
    }

    public static void registryRenderers(){
        CuriosRendererRegistry.register(ToNeko.EARS.get(),()->new ICurioRenderer() {
            @Override
            public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if(renderLayerParent.getModel() instanceof HumanoidModel<?> model) {
                    EarsModel.getEarsModel(model).renderToBuffer(matrixStack,renderTypeBuffer.getBuffer(RenderType.armorCutoutNoCull(ToNeko.location("textures/models/armor/ears_layer_1.png"))),light, OverlayTexture.NO_OVERLAY,1,1,1,1);
                }
            }
        });
        CuriosRendererRegistry.register(ToNeko.DYED_EARS.get(),()->new ICurioRenderer() {
            @Override
            public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if(renderLayerParent.getModel() instanceof HumanoidModel<?> model) {
                    float r=1,g=1,b=1;
                    if (stack.getItem() instanceof net.minecraft.world.item.DyeableLeatherItem dyeableLeatherItem) {
                        int i = (dyeableLeatherItem).getColor(stack);
                        r = (float)(i >> 16 & 255) / 255.0F;
                        g = (float)(i >> 8 & 255) / 255.0F;
                        b = (float)(i & 255) / 255.0F;
                    }
                    EarsModel.getEarsModel(model).renderToBuffer(matrixStack,renderTypeBuffer.getBuffer(RenderType.armorCutoutNoCull(ToNeko.location("textures/models/armor/dyed_ears_layer_1.png"))),light, OverlayTexture.NO_OVERLAY,r,g,b,1);
                    EarsModel.getEarsModel(model).renderToBuffer(matrixStack,renderTypeBuffer.getBuffer(RenderType.armorCutoutNoCull(ToNeko.location("textures/models/armor/dyed_ears_layer_1_overlay.png"))),light, OverlayTexture.NO_OVERLAY,1,1,1,1);
                }
            }
        });
        CuriosRendererRegistry.register(ToNeko.TAIL.get(),()->new ICurioRenderer() {
            @Override
            public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if(renderLayerParent.getModel() instanceof HumanoidModel<?> model) {
                    TailModel.getTailModel(slotContext.entity(),model).renderToBuffer(matrixStack,renderTypeBuffer.getBuffer(RenderType.armorCutoutNoCull(ToNeko.location("textures/models/armor/tail_layer_2.png"))),light, OverlayTexture.NO_OVERLAY,1,1,1,1);
                }
            }
        });
        CuriosRendererRegistry.register(ToNeko.DYED_TAIL.get(),()->new ICurioRenderer() {
            @Override
            public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if(renderLayerParent.getModel() instanceof HumanoidModel<?> model) {
                    float r=1,g=1,b=1;
                    if (stack.getItem() instanceof net.minecraft.world.item.DyeableLeatherItem dyeableLeatherItem) {
                        int i = (dyeableLeatherItem).getColor(stack);
                        r = (float)(i >> 16 & 255) / 255.0F;
                        g = (float)(i >> 8 & 255) / 255.0F;
                        b = (float)(i & 255) / 255.0F;
                    }
                    TailModel.getTailModel(slotContext.entity(),model).renderToBuffer(matrixStack,renderTypeBuffer.getBuffer(RenderType.armorCutoutNoCull(ToNeko.location("textures/models/armor/dyed_tail_layer_2.png"))),light, OverlayTexture.NO_OVERLAY,r,g,b,1);
                    TailModel.getTailModel(slotContext.entity(),model).renderToBuffer(matrixStack,renderTypeBuffer.getBuffer(RenderType.armorCutoutNoCull(ToNeko.location("textures/models/armor/dyed_tail_layer_2_overlay.png"))),light, OverlayTexture.NO_OVERLAY,1,1,1,1);
                }
            }
        });
    }
}
