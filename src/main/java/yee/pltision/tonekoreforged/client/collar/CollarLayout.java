package yee.pltision.tonekoreforged.client.collar;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.collar.CollarCapabilityProvider;
import yee.pltision.tonekoreforged.collar.CollarState;
import yee.pltision.tonekoreforged.collar.bauble.CollarBaubleState;

@OnlyIn(Dist.CLIENT)
public class CollarLayout<E extends LivingEntity, M extends HumanoidModel<E>> extends RenderLayer<E, M> {

    public static final ResourceLocation COLLAR = ToNeko.location("textures/entity/collar.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(COLLAR);


    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull E p_114482_) {
        return COLLAR;
    }

    public CollarLayout(RenderLayerParent<E, M> p_117346_) {
        super(p_117346_);
    }

    @Override
    public void render(@NotNull PoseStack stack, @NotNull MultiBufferSource source, int idkInt, @NotNull E entity,
                       float position, float speed, float partialTick, float bob, float headRotateYFromBody, float xRot
    ) {

        entity.getCapability(CollarCapabilityProvider.COLLAR_HANDLER).ifPresent(cap->{
            CollarState state= cap.getState();
            CollarRenderHelper<LivingEntity,?> collarRenderHelper;
            if(state!=null&&(collarRenderHelper= state.getCollarRenderHelper())!=null&& collarRenderHelper.canUseModel(getParentModel())) {
                collarRenderHelper.cast().render(stack, source, idkInt, entity,state, position, speed, partialTick, bob, headRotateYFromBody, xRot, getParentModel());
                int slot=0;
                for(CollarBaubleState baubleState:cap.getState().baubles()){
                    CollarBaubleRenderer<LivingEntity,?> collarBaubleRenderer;
                    if(baubleState!=null&&(collarBaubleRenderer=baubleState.getRenderer(entity,cap.getState()))!=null) {
                        Model castedModel=collarBaubleRenderer.tryCastModel(getParentModel());
                        if(castedModel!=null) {
                            collarBaubleRenderer.cast().renderBeforePushStack(stack, source, idkInt, entity, position, speed, partialTick, bob, headRotateYFromBody, xRot, cap.getState(), collarRenderHelper.cast(), slot, castedModel);
                            stack.pushPose();
                            collarRenderHelper.cast().setStack(stack, idkInt, entity,state,slot, position, speed, partialTick, bob, headRotateYFromBody, xRot, getParentModel());
                            collarBaubleRenderer.cast().render(stack, source, idkInt, entity, position, speed, partialTick, bob, headRotateYFromBody, xRot, cap.getState(), collarRenderHelper.cast(), slot, castedModel);
                            stack.popPose();
                        }
                    }
                    slot++;
                }
            }
        });
    }

    public static String modelPartToString(ModelPart modelPart) {
        return "{\n" +
                "\tpos = [" + modelPart.x + ", " + modelPart.y + ", " + modelPart.z + "], \n" +
                "\trot = [" + modelPart.xRot + ", " + modelPart.yRot + ", " + modelPart.zRot + "], \n" +
                "\tscale = [" + modelPart.xScale + ", " + modelPart.yScale + ", " + modelPart.zScale + "], \n" +
                "\tvisible = " + modelPart.visible + ", \n" +
                "\tskipDraw = " + modelPart.skipDraw + ", \n" +
                "}";
    }

}
