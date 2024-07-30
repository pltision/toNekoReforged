package yee.pltision.tonekoreforged.client.collar;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.collar.CollarState;

import static yee.pltision.tonekoreforged.RendererUtils.*;
import static yee.pltision.tonekoreforged.Vector3fUtils.*;


public class BellRenderer<E extends Entity,M extends HumanoidModel<?>> implements CollarBaubleRenderer<E,M> {
    public static final ResourceLocation RING = ToNeko.location("textures/entity/ring.png");
    public static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(RING);

    public static final BellRenderer<Entity,?> INSTANT=new BellRenderer<>();

    public static final Matrix3f MATRIX_ROT_Y=new Matrix3f(1,0,0, 0,-1,0, 0,0,-1);

    public static Vector3f LIGHT=new Vector3f(0,-1,0);

    public static Vector3f VERTEX=new Vector3f(1,1,1);

    @Override
    public void render(@NotNull PoseStack stack, @NotNull MultiBufferSource source, int idkInt, @NotNull E entity, float walking, float speed, float neverMind3, float neverMind4, float neverMind5, float neverMind6, CollarState collarState, CollarRenderHelper<E,M> renderHelper, int slot, M model) {
//        System.out.println(walking+"\t"+ speed);

        stack.pushPose();

        PoseStack.Pose pose = stack.last();

        VertexConsumer vertexconsumer = source.getBuffer(RENDER_TYPE);

        Vector3f top=new Vector3f();
        Vector3f bottom=new Vector3f();
        Vector3f move=new Vector3f();
        Matrix3f transform=new Matrix3f();
        Matrix3f rotate=new Matrix3f();

        renderHelper.getTiePos(entity,collarState,model,slot,top,bottom,transform, rotate);

        rotate.rotateX((-Mth.sin(model.head.xRot)+Mth.sin(model.body.xRot)+0.25f)*Mth.PI/4);
        rotate.rotateX(Mth.abs(-Mth.sin(walking/2)*Mth.PI/4)*speed);//摆动

        Vector3f afterMove=new Vector3f(0, .5f,-Mth.sin(model.head.xRot)*0.5f-Math.max(Mth.sin(model.head.xRot),0));

        move.set(bottom);
//        bottom.add(top.mul(-1));
//        move.add(bottom.mul(.5f));

        cube(pose,vertexconsumer,idkInt,
                VERTEX,transform,afterMove,rotate,move,
                new float[][]{
                        {3/16f,3/16f},
                        {0/16f,3/16f},
                        {9/16f,3/16f},
                        {6/16f,3/16f},
                        {3/16f,0/16f},
                        {6/16f,0/16f}
                },3/16f);

        stack.popPose();
    }


    public static void cube(PoseStack.Pose pose,VertexConsumer vertexconsumer,int idkInt,
                            Vector3f vertex,Matrix3f cubeRotate, Vector3f afterMove, Matrix3f afterTransform, Vector3f move,
                            float[][] uvStarts, float uvSize
    ){
        Vector3f compute3f=new Vector3f();
        Vector3f computeVertex=new Vector3f(vertex);

        face(pose.pose(),pose.normal(),vertexconsumer,idkInt,
                computeVertex,ROT_Z,cubeRotate,compute3f,
                afterMove,afterTransform,move, new float[]{uvStarts[0][0], uvStarts[0][1], uvStarts[0][0] + uvSize, uvStarts[0][1] + uvSize});
        computeVertex.rotateY(Mth.PI/2);

        face(pose.pose(),pose.normal(),vertexconsumer,idkInt,
                computeVertex,ROT_X,cubeRotate,compute3f,
                afterMove,afterTransform,move, new float[]{uvStarts[1][0], uvStarts[1][1], uvStarts[1][0] + uvSize, uvStarts[1][1] + uvSize});
        computeVertex.rotateY(Mth.PI/2);

        face(pose.pose(),pose.normal(),vertexconsumer,idkInt,
                computeVertex,ROT_NZ,cubeRotate,compute3f,
                afterMove,afterTransform,move, new float[]{uvStarts[2][0], uvStarts[2][1], uvStarts[2][0] + uvSize, uvStarts[2][1] + uvSize});
        computeVertex.rotateY(Mth.PI/2);

        face(pose.pose(),pose.normal(),vertexconsumer,idkInt,
                computeVertex,ROT_NX,cubeRotate,compute3f,
                afterMove,afterTransform,move, new float[]{uvStarts[3][0], uvStarts[3][1], uvStarts[3][0] + uvSize, uvStarts[3][1] + uvSize});
        computeVertex.rotateY(Mth.PI/2);

        computeVertex.rotateX(Mth.PI/2);
        face(pose.pose(),pose.normal(),vertexconsumer,idkInt,
                computeVertex,ROT_Y,cubeRotate,compute3f,
                afterMove,afterTransform,move, new float[]{uvStarts[4][0], uvStarts[4][1], uvStarts[4][0] + uvSize, uvStarts[4][1] + uvSize});

        computeVertex.rotateX(Mth.PI);
        face(pose.pose(),pose.normal(),vertexconsumer,idkInt,
                computeVertex,ROT_NY,cubeRotate,compute3f,
                afterMove,afterTransform,move, new float[]{uvStarts[5][0], uvStarts[5][1], uvStarts[5][0] + uvSize, uvStarts[5][1] + uvSize});
    }

    public static void face(Matrix4f idk4f, Matrix3f idk3f, VertexConsumer vertexconsumer, int idkInt,
                            Vector3f vertex, Matrix3f vertexRot, Matrix3f cubeRotate, Vector3f compute3f,
                            Vector3f afterMove, Matrix3f afterTransform, Vector3f move,
                            float[] uvs
    ){
        for(int i=0;i<4;i++) {
            compute3f.set(vertex);
            compute3f.add(afterMove);
            compute3f.mul(afterTransform);
            compute3f.mul(cubeRotate);
            compute3f.add(move);
            vertex(idk4f,idk3f,vertexconsumer,idkInt,
                    compute3f.x,compute3f.y,compute3f.z,
                    uvs[UV_INDEXES[i][0]],uvs[UV_INDEXES[i][1]],
                    LIGHT.x,LIGHT.y,LIGHT.z);
            vertex.mul(vertexRot);
        }
    }
    public static void vertex(Matrix4f idk4f, Matrix3f idk3f, VertexConsumer consumer, int idkInt,
                       float x, float y, float z,
                       float uvx, float uvy,
                       float rx, float ry, float rz) {
        consumer.vertex(idk4f, x, y, z).color(255, 255, 255, 255).uv(uvx, uvy).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(idkInt).normal(idk3f, rx, ry, rz).endVertex();
    }

}
