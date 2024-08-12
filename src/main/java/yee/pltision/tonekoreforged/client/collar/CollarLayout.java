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
    public void render(@NotNull PoseStack stack, @NotNull MultiBufferSource source, int idkInt, @NotNull E entity, float neverMind1, float neverMind2, float neverMind3, float neverMind4, float neverMind5, float neverMind6) {

        entity.getCapability(CollarCapabilityProvider.COLLAR_RECORD).ifPresent(cap->{
            CollarState state= cap.getState();
            CollarRenderHelper<LivingEntity,?> collarRenderHelper;
            if(state!=null&&(collarRenderHelper= state.getCollarRenderHelper())!=null&& collarRenderHelper.isTrueModel(getParentModel())) {
                ((CollarRenderHelper)collarRenderHelper).render(stack, source, idkInt, entity, neverMind1, neverMind2, neverMind3, neverMind4, neverMind5, neverMind6, getParentModel());
                int i=0;
                for(CollarBaubleState baubleState:cap.getState().baubles()){
                    CollarBaubleRenderer<LivingEntity,?> collarBaubleRenderer=baubleState.getRenderer(entity,cap.getState());
                    if(collarBaubleRenderer!=null) {
                        Model castedModel=collarBaubleRenderer.tryCastModel(getParentModel());
                        if(castedModel!=null)
                            ((CollarBaubleRenderer)collarBaubleRenderer).render(stack, source, idkInt, entity, neverMind1, neverMind2, neverMind3, neverMind4, neverMind5, neverMind6, cap.getState(), collarRenderHelper, i, castedModel);
                    }
                    i++;
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
