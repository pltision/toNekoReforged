/*
package yee.pltision.tonekoreforged.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.*;
import yee.pltision.tonekoreforged.ToNeko;

import java.lang.Math;

import static net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip.TEXTURE_LOCATION;

@OnlyIn(Dist.CLIENT)
public class CollarLayout<E extends LivingEntity, M extends HumanoidModel<E>> extends RenderLayer<E, M> {

    public static final ResourceLocation COLLAR = new ResourceLocation(ToNeko.MODID, "textures/entity/collar.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(COLLAR);

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull E p_114482_) {
        return COLLAR;

    }

    public CollarLayout(RenderLayerParent<E, M> p_117346_) {
        super(p_117346_);
    }

    //East North Low
    */
/*public static final Vector3f ENL=new Vector3f(3,0,-2),ENH=new Vector3f(3,1,-2);
    public static final Vector3f ESL=new Vector3f(3,0,2),ESH=new Vector3f(3,1,2);
    public static final Vector3f WNL=new Vector3f(-3,0,-2),WNH=new Vector3f(-3,1,-2);
    public static final Vector3f WSL=new Vector3f(-3,0,2),WSH=new Vector3f(-3,1,2);
*//*

    public static final Vector2f SIZE = new Vector2f(3, 2);

    public static Matrix2f RIGHT_FONT = new Matrix2f(1, 0, 0, 1);
    public static Matrix2f RIGHT_BACK = new Matrix2f(1, 0, 0, -1);
    public static Matrix2f LEFT_BACK = new Matrix2f(-1, 0, 0, -1);
    public static Matrix2f LEFT_FONT = new Matrix2f(-1, 0, 0, 1);


    public static Vector3f ZERO=new Vector3f(0,0,0);
    public static Vector3f LIGHT=new Vector3f(0,-1,0);
    public static Matrix3f NORMAL=new Matrix3f(
            1/16f,0,0,
            0,1/16f,0,
            0,0,1/16f);

    @Override
    public void render(@NotNull PoseStack stack, @NotNull MultiBufferSource source, int idkInt, @NotNull E entity, float neverMind1, float neverMind2, float neverMind3, float neverMind4, float neverMind5, float neverMind6) {

        System.out.println("body="+modelPartToString(getParentModel().body));
        System.out.println("head="+modelPartToString(getParentModel().head));
//        System.out.println("swimAmount="+getParentModel().swimAmount);

        stack.pushPose();

        PoseStack.Pose posestack$pose = stack.last();
        Matrix4f idk4f = posestack$pose.pose();
        Matrix3f idk3f = posestack$pose.normal();

        VertexConsumer vertexconsumer = source.getBuffer(RENDER_TYPE);

        */
/*vertex(idk4f, idk3f, vertexconsumer, idkInt, -1, 0, -1, 0.0F, 0.0F, 0, -1, 0);
        vertex(idk4f, idk3f, vertexconsumer, idkInt, -1, 0, 1, 0.0F, 1.0F, 0, -1, 0);
        vertex(idk4f, idk3f, vertexconsumer, idkInt, 1, 0, 1, 1.0F, 1.0F, 0, -1, 0);
        vertex(idk4f, idk3f, vertexconsumer, idkInt, 1, 0, -1, 1.0F, 0.0F, 0, -1, 0);*//*



        Vector3f afterMove=new Vector3f(getParentModel().body.x,getParentModel().body.y,getParentModel().body.z);
        Vector3f move=new Vector3f(0,
                Math.abs((getParentModel().head.xRot+getParentModel().body.xRot)/3.14159265358979f*2*0.2f),
                (float) Math.sin(getParentModel().head.xRot+getParentModel().body.xRot)*-1.5f);
        afterMove.mul(NORMAL);

        Matrix3f d3transform=new Matrix3f();
        d3transform.set(NORMAL);

        d3transform.rotateY(getParentModel().head.yRot);

        d3transform.rotateX(getParentModel().body.xRot);
        d3transform.rotateY(getParentModel().body.yRot);
        d3transform.rotateZ(getParentModel().body.zRot);

        d3transform.rotateX((getParentModel().head.xRot+getParentModel().body.xRot)/3);


        Vector2f compute2f=new Vector2f();
        Vector3f compute3f=new Vector3f();

        //前
        face(compute2f,compute3f,idk4f,idk3f,vertexconsumer,idkInt,
                new float[]{0/16f,0/16f,6/16f,1/16f},
                SIZE,LEFT_FONT,RIGHT_FONT,
                move,afterMove,d3transform,LIGHT);

        //右
        face(compute2f,compute3f,idk4f,idk3f,vertexconsumer,idkInt,
                new float[]{0/16f,2/16f,4/16f,3/16f},
                SIZE,RIGHT_FONT,RIGHT_BACK,
                move,afterMove,d3transform,LIGHT);

        //后
        face(compute2f,compute3f,idk4f,idk3f,vertexconsumer,idkInt,
                new float[]{0/16f,1/16f,6/16f,2/16f},
                SIZE,RIGHT_BACK,LEFT_BACK,
                move,afterMove,d3transform,LIGHT);

        //左
        face(compute2f,compute3f,idk4f,idk3f,vertexconsumer,idkInt,
                new float[]{0/16f,3/16f,4/16f,4/16f},
                SIZE,LEFT_FONT,LEFT_BACK,
                move,afterMove,d3transform,LIGHT);

        stack.popPose();
    }

    public static String modelPartToString(ModelPart modelPart) {
        return "{\n" +
                "\tpos = {" + modelPart.x + ", " + modelPart.y + ", " + modelPart.z + "}, \n" +
                "\trot = {" + modelPart.xRot + ", " + modelPart.yRot + ", " + modelPart.zRot + "}, \n" +
                "\tscale = {" + modelPart.xScale + ", " + modelPart.yScale + ", " + modelPart.zScale + "}, \n" +
                "\tvisible = " + modelPart.visible + ", \n" +
                "\tskipDraw = " + modelPart.skipDraw + ", \n" +
                "}";
    }

    public void face(Vector2f compute2f,Vector3f compute3f,Matrix4f idk4f, Matrix3f idk3f, VertexConsumer vertexconsumer, int idkInt,
                     float[] uv,
                     Vector2f pos, Matrix2f posTransform0, Matrix2f posTransform1,
                     Vector3f move,Vector3f afterMove, Matrix3f d3Transform, Vector3f light) {
        compute2f.set(pos).mul(posTransform0);

        compute3f.set(compute2f.x, -1, compute2f.y);
        compute3f.add(move);
        compute3f.mul(d3Transform);
        compute3f.add(afterMove);
        vertex(idk4f, idk3f, vertexconsumer, idkInt,
                compute3f.x,compute3f.y,compute3f.z, uv[0], uv[1],
                light.x, light.y, light.z);

        compute3f.set(compute2f.x, 0, compute2f.y);
        compute3f.add(move);
        compute3f.mul(d3Transform);
        compute3f.add(afterMove);
        vertex(idk4f, idk3f, vertexconsumer, idkInt,
                compute3f.x,compute3f.y,compute3f.z, uv[0], uv[3],
                light.x, light.y, light.z);

        compute2f.set(pos).mul(posTransform1);

        compute3f.set(compute2f.x, 0, compute2f.y);
        compute3f.add(move);
        compute3f.mul(d3Transform);
        compute3f.add(afterMove);
        vertex(idk4f, idk3f, vertexconsumer, idkInt,
                compute3f.x,compute3f.y,compute3f.z, uv[2], uv[3],
                light.x, light.y, light.z);

        compute3f.set(compute2f.x, -1, compute2f.y);
        compute3f.add(move);
        compute3f.mul(d3Transform);
        compute3f.add(afterMove);
        vertex(idk4f, idk3f, vertexconsumer, idkInt,
                compute3f.x,compute3f.y,compute3f.z, uv[2], uv[1],
                light.x, light.y, light.z);

//        vertex(idk4f,idk3f);
    }

    public void vertex(Matrix4f idk4f, Matrix3f idk3f, VertexConsumer consumer, int idkInt,
                       float x, float y, float z,
                       float uvx, float uvy,
                       float rx, float ry, float rz) {
        consumer.vertex(idk4f, x, y, z).color(255, 255, 255, 255).uv(uvx, uvy).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(idkInt).normal(idk3f, rx, ry, rz).endVertex();
    }
}
*/
