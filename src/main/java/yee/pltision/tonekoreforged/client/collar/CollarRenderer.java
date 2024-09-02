package yee.pltision.tonekoreforged.client.collar;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.*;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.Vector3fUtils;
import yee.pltision.tonekoreforged.collar.CollarState;

import java.lang.Math;

@OnlyIn(Dist.CLIENT)
public class CollarRenderer<E extends LivingEntity,M extends HumanoidModel<E>> implements CollarRenderHelper<E,M> {
    public static final ResourceLocation COLLAR = ToNeko.location("textures/entity/collar.png");
    public static final CollarRenderer<LivingEntity,HumanoidModel<LivingEntity>> INSTANT = new CollarRenderer<>();
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(COLLAR);

    public static final CollarRenderHelper<LivingEntity,HumanoidModel<LivingEntity>> COLLAR_RENDERER=new CollarRenderer<>();

    public static final Vector2f SIZE = new Vector2f(3, 2);

    public static Matrix2f RIGHT_FONT = new Matrix2f(1, 0, 0, 1);
    public static Matrix2f RIGHT_BACK = new Matrix2f(1, 0, 0, -1);
    public static Matrix2f LEFT_BACK = new Matrix2f(-1, 0, 0, -1);
    public static Matrix2f LEFT_FONT = new Matrix2f(-1, 0, 0, 1);

    @Override
    public boolean isTrueModel(Model model) {
        return model instanceof HumanoidModel<?>;
    }


    public static void getTransform(HumanoidModel<?> model,Matrix3f transform){
        transform.set(Vector3fUtils.ENTITY_SCALE);
    }

    public static void getRotate(HumanoidModel<?> model,Quaternionf rotate){
        rotate.rotateY(model.head.yRot);

        rotate.rotateX(model.body.xRot);
        rotate.rotateY(model.body.yRot);
        rotate.rotateZ(model.body.zRot);

        rotate.rotateX((model.head.xRot+model.body.xRot)/3);
    }

    public static Vector3f getBodyMove(HumanoidModel<?> model){
        return new Vector3f(model.body.x,model.body.y,model.body.z).mul(Vector3fUtils.ENTITY_SCALE);
    }
    public static Vector3f getMove(HumanoidModel<?> model){
        return new Vector3f(0,
                Math.abs((model.head.xRot+model.body.xRot)/3.14159265358979f*2*0.2f),
                (float) Math.sin(model.head.xRot+model.body.xRot)*-1.5f);
    }
    public static void transformVector(Vector3f vector,Vector3f move,Matrix3f transform,Vector3f afterMove){
        vector.add(move);
        vector.mul(transform);
        vector.mul(Vector3fUtils.ENTITY_SCALE);
        vector.add(afterMove);
    }

    @Override
    public void render(@NotNull PoseStack stack, @NotNull MultiBufferSource source, int idkInt, @NotNull E entity, float neverMind1, float neverMind2, float neverMind3, float neverMind4, float neverMind5, float neverMind6, M model) {
        stack.pushPose();

        PoseStack.Pose posestack$pose = stack.last();

        VertexConsumer vertexconsumer = source.getBuffer(RENDER_TYPE);

        Vector3f move=getMove(model);
        Vector3f afterMove=getBodyMove(model);

        Matrix3f d3transform=new Matrix3f();
//        getTransform(model,d3transform);

        Quaternionf rotate=new Quaternionf();
        getRotate(model,rotate);
        d3transform.rotate(rotate);

        Vector2f compute2f=new Vector2f();
        Vector3f compute3f=new Vector3f();

        Vector3f normal=new Vector3f();
//        Quaternionf normalRotate=new Quaternionf();

        //后
        face(compute2f,compute3f,posestack$pose,vertexconsumer,idkInt,
                new float[]{0/16f,0/16f,6/16f,1/16f},
                SIZE,LEFT_FONT,RIGHT_FONT,
                move,afterMove,d3transform,normal.set(0,-1,0).rotate(rotate));

        //右
        face(compute2f,compute3f,posestack$pose,vertexconsumer,idkInt,
                new float[]{0/16f,2/16f,4/16f,3/16f},
                SIZE,RIGHT_FONT,RIGHT_BACK,
                move,afterMove,d3transform,normal.set(0,-1,0).rotate(rotate));

        //前
        face(compute2f,compute3f,posestack$pose,vertexconsumer,idkInt,
                new float[]{0/16f,1/16f,6/16f,2/16f},
                SIZE,RIGHT_BACK,LEFT_BACK,
                move,afterMove,d3transform,normal.set(0,-1,0).rotate(rotate));

        //左
        face(compute2f,compute3f,posestack$pose,vertexconsumer,idkInt,
                new float[]{0/16f,3/16f,4/16f,4/16f},
                SIZE,LEFT_BACK,LEFT_FONT,
                move,afterMove,d3transform,normal.set(0,-1,0).rotate(rotate));

        stack.popPose();
    }

    @Override
    public void getTiePos(E entity, CollarState state, M model, int slot, Vector3f top, Vector3f button, Matrix3f transform, Quaternionf positionRotate) {
//        getTransform(model,transform);
        Quaternionf quaternionf=new Quaternionf();
        getRotate(model,quaternionf);
        transform.rotate(quaternionf);

        Vector2f compute2f=new Vector2f();
        Vector3f afterMove=getBodyMove(model);
        Vector3f move=getMove(model);
        switch (slot) {
            case 0 -> {
                compute2f.set(SIZE).mul(LEFT_BACK);
                compute2f.add(1.5f, 0);
                positionRotate.rotateY(Mth.PI);
            }
            case 2 -> {
                if(state.baubles().get(3)==null){
                    compute2f.set(SIZE).mul(RIGHT_FONT);
                    compute2f.add(-2f, 0);
                }
                else {
                    compute2f.set(SIZE).mul(RIGHT_FONT);
                    compute2f.add(-1.5f, 0);
                }
                //positionRotate.rotateY(0);
            }
            case 3 -> {
                if(state.baubles().get(2)==null){
                    compute2f.set(SIZE).mul(LEFT_FONT);
                    compute2f.add(2f, 0);
                }
                else {
                    compute2f.set(SIZE).mul(LEFT_FONT);
                    compute2f.add(1.5f, 0);
                }
                //positionRotate.rotateY(0);
            }
            case 1 -> {
                compute2f.set(SIZE).y=0;
                compute2f.mul(RIGHT_FONT);
                positionRotate.rotateY(Mth.HALF_PI);
            }
            case 4 -> {
                compute2f.set(SIZE).y=0;
                compute2f.mul(LEFT_FONT);
                positionRotate.rotateY(-Mth.HALF_PI);
            }
        }
        top.set(compute2f.x(),-1,compute2f.y());
        button.set(compute2f.x(),0,compute2f.y());
        transformVector(top,move,transform,afterMove);
        transformVector(button,move,transform,afterMove);
    }
    public static void face(Vector2f compute2f, Vector3f compute3f, PoseStack.Pose pose, VertexConsumer vertexconsumer, int idkInt,
                            float[] uv,
                            Vector2f pos, Matrix2f posTransform0, Matrix2f posTransform1,
                            Vector3f move, Vector3f afterMove, Matrix3f d3Transform, Vector3f normal) {
        compute2f.set(pos).mul(posTransform0);
        compute3f.set(compute2f.x, -1, compute2f.y);
        transformVector(compute3f,move,d3Transform,afterMove);
        vertex(pose.pose(), pose.normal(), vertexconsumer, idkInt,
                compute3f.x,compute3f.y,compute3f.z, uv[0], uv[1],
                normal.x, normal.y, normal.z);

        compute3f.set(compute2f.x, 0, compute2f.y);
        transformVector(compute3f,move,d3Transform,afterMove);
        vertex(pose.pose(), pose.normal(), vertexconsumer, idkInt,
                compute3f.x,compute3f.y,compute3f.z, uv[0], uv[3],
                normal.x, normal.y, normal.z);

        compute2f.set(pos).mul(posTransform1);
        compute3f.set(compute2f.x, 0, compute2f.y);
        transformVector(compute3f,move,d3Transform,afterMove);
        vertex(pose.pose(), pose.normal(), vertexconsumer, idkInt,
                compute3f.x,compute3f.y,compute3f.z, uv[2], uv[3],
                normal.x, normal.y, normal.z);

        compute3f.set(compute2f.x, -1, compute2f.y);
        transformVector(compute3f,move,d3Transform,afterMove);
        vertex(pose.pose(), pose.normal(), vertexconsumer, idkInt,
                compute3f.x,compute3f.y,compute3f.z, uv[2], uv[1],
                normal.x, normal.y, normal.z);

//        vertex(idk4f,idk3f);
    }

    public static void vertex(Matrix4f idk4f, Matrix3f idk3f, VertexConsumer consumer, int idkInt,
                              float x, float y, float z,
                              float uvx, float uvy,
                              float rx, float ry, float rz) {
        consumer.vertex(idk4f, x, y, z).color(255, 255, 255, 255).uv(uvx, uvy).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(idkInt).normal(idk3f, rx, ry, rz).endVertex();
    }
}
