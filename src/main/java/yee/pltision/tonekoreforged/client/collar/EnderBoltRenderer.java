package yee.pltision.tonekoreforged.client.collar;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.client.collar.model.EnderBoltModel;
import yee.pltision.tonekoreforged.collar.CollarState;


public class EnderBoltRenderer<E extends Entity,M extends HumanoidModel<?>> implements CollarBaubleRenderer<E,M> {
    public static final ResourceLocation RING = ToNeko.location("textures/entity/ender_bolt.png");
    public static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(RING);

    public static Vector3f VERTEX=new Vector3f(0.875f,0.875f,0.875f);

    EnderBoltModel enderBoltModel;

    public EnderBoltRenderer(EnderBoltModel model){
        enderBoltModel =model;
    }

    @Override
    public void render(@NotNull PoseStack stack, @NotNull MultiBufferSource source, int light, @NotNull E entity,
                                      float position, float speed, float partialTick, float bob, float headRotateYFromBody, float xRot,
                                      CollarState collarState, CollarRenderHelper<E,M> renderHelper, int slot, M model) {
        enderBoltModel.root.xScale=enderBoltModel.root.yScale=enderBoltModel.root.zScale=0.75f;
        enderBoltModel.renderToBuffer(stack,source.getBuffer(RENDER_TYPE),light,OverlayTexture.NO_OVERLAY,1,1,1,1);
    }

    public static void vertex(Matrix4f idk4f, Matrix3f idk3f, VertexConsumer consumer, int idkInt,
                       float x, float y, float z,
                       float uvx, float uvy,
                       float rx, float ry, float rz) {
        consumer.vertex(idk4f, x, y, z).color(255, 255, 255, 255).uv(uvx, uvy).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(idkInt).normal(idk3f, rx, ry, rz).endVertex();
    }

    @Override
    public CompoundTag serializeNBT() {
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

    }
}
