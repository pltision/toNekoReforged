package yee.pltision.tonekoreforged.client.nekoarmor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import yee.pltision.tonekoreforged.Vector3fUtils;

import static yee.pltision.tonekoreforged.RendererUtils.UV_INDEXES;

@OnlyIn(Dist.CLIENT)
public class TailModel extends Model {
    final HumanoidModel<?> parent;
    final LivingEntity entity;

    public static TailModel getTailModel(LivingEntity entity, HumanoidModel<?> parent){
        return new TailModel(entity,parent);
    }

    public TailModel(LivingEntity entity,HumanoidModel<?> parent) {
        super(parent::renderType);
        this.parent=parent;
        this.entity=entity;
    }

    static int SECTION_COUNT=8;
    static Vector3f SECTION_MOVE=new Vector3f(0,0,3f);
    static Vector3f VERTEX=new Vector3f(1,1,0);
    static Quaternionf VERTEX_ROTATE=new Quaternionf().rotateZ(Mth.PI/2);

    static Vector3f STAND_MOVE=new Vector3f(0,12,1);

    static float bValue(float speed){
        return Math.min(1,1/Math.max(1,Math.min(speed*6,5)));
    }

    static float aValue(float time,float speed){
        return time/24;
    }

    public static Vector3f getMove(LivingEntity entity){
        return STAND_MOVE;
    }

    static Quaternionf getInitRotate(LivingEntity entity,float time,float speed){
        if(entity.isVisuallySwimming()||entity.isFallFlying())
            return new Quaternionf().rotateX( -( Mth.sin(aValue(time,speed))*Mth.PI/12+Mth.PI*(2/3f)) );
        if(entity.getPose()== Pose.SLEEPING)
            return new Quaternionf().rotateX( -( Mth.PI*(2/3f)) );
        return new Quaternionf().rotateX( -( Mth.sin(aValue(time,speed))*Mth.PI/12+Mth.PI/3 )*bValue(speed));
    }

    float swingScale(LivingEntity entity){
        return entity.isVisuallySwimming()||entity.getPose()== Pose.SLEEPING ? 0.5f : 1f;
    }

    int r,g,b,a;
    @Override
    public void renderToBuffer(@NotNull PoseStack pose, @NotNull VertexConsumer consumer, int idkInt, int p_103114_, float r, float g, float b, float a) {
        this.r= (int) (r*255);
        this.g= (int) (g*255);
        this.b= (int) (b*255);
        this.a= (int) (a*255);

        Vector3f compute3f=new Vector3f();
        Quaternionf computeRotate=new Quaternionf();

        float partialTick=Minecraft.getInstance().getPartialTick();
        float time=(entity.tickCount+ partialTick);

        computeRotate.rotateY(-(float) Math.toRadians(Mth.lerp(partialTick, entity.yRotO, entity.getYRot())));
        if(entity.isVisuallySwimming())computeRotate.rotateX(-(float) Math.toRadians(Mth.lerp(partialTick, entity.xRotO, entity.getXRot())));
        Vector3f movement=new Vector3f(
                (float) (entity.getX()-entity.xOld),
                (float) (entity.getY()-entity.yOld),
                (float) (entity.getZ()-entity.zOld)
        );
        compute3f.set(0,0,1).rotate(computeRotate);
        float speed= (float) (entity.getDeltaMovement().x * compute3f.x + entity.getDeltaMovement().y * compute3f.y + entity.getDeltaMovement().z * compute3f.z);
        speed=Mth.lerp(partialTick,Math.min(movement.x * compute3f.x + movement.y * compute3f.y + movement.z * compute3f.z,speed),speed);   //好像并没有什么卵用的插值

        float swingScale= swingScale(entity);

//        System.out.println(speed+"\t"+movement+"\t"+compute3f);
//        System.out.println(entity.isSwimming()+"\t"+entity.isVisuallySwimming());

        Vector3f move=getMove(entity);

        Quaternionf itRotate=getInitRotate(entity,time,speed);
        Quaternionf nextRotate=new Quaternionf();
        Vector3f it=new Vector3f();
        Vector3f next=new Vector3f();

        Vector3f computeVertex=new Vector3f(VERTEX);
        float[] uvs=new float[4];

        Vector3f normal=new Vector3f(0,1,0);
        Vector3f normalCompute=new Vector3f();

        for(int i=0;i<SECTION_COUNT;i++){
            computeRotate.set(0,0,0,1)
                    .rotateX(Mth.cos((1-Mth.cos(i*Mth.PI/SECTION_COUNT/2))*Mth.PI*(1+Mth.cos(aValue(time,speed))/8))*Mth.PI/6*bValue(speed) *swingScale );
            nextRotate.set(itRotate).mul(computeRotate);
            next.set(it).add(compute3f.set(SECTION_MOVE).rotate(nextRotate));

            section(pose.last(),consumer,idkInt,it,next,itRotate,nextRotate,move,computeVertex,compute3f,normal,normalCompute,uvs,i);

            itRotate.set(nextRotate);
            it.set(next);
        }

        uvs[0]=0 /16f;
        uvs[1]=15/16f;
        uvs[2]=1 /16f;
        uvs[3]=16/16f;
        normalCompute.set(0,0,1);
        normalCompute.rotate(itRotate);
        for(int i=0;i<4;i++){
            vertex(pose.last(),consumer,idkInt,computeVertex,itRotate,it,move,compute3f,uvs[UV_INDEXES[i][0]],uvs[UV_INDEXES[i][1]],normalCompute);
            computeVertex.rotate(VERTEX_ROTATE);
        }

    }

    public void section(PoseStack.Pose pose, VertexConsumer consumer, int idkInt,
                               Vector3f it,Vector3f next,Quaternionf itRotate,Quaternionf nextRotate,
                               Vector3f move,Vector3f vertex,Vector3f normal,Vector3f computeNormal,Vector3f compute3f,float[] uvs,int index){
        for(int i=0;i<4;i++){
            normal.rotate(VERTEX_ROTATE);
            computeNormal.set(normal);
            computeNormal.rotate(itRotate);

            getSectionUvs(uvs,index,i);
            vertex(pose,consumer,idkInt,vertex,itRotate,it,move,compute3f,uvs[0],uvs[3],computeNormal);
            vertex(pose,consumer,idkInt,vertex,nextRotate,next,move,compute3f,uvs[2],uvs[3],computeNormal);
            vertex.rotate(VERTEX_ROTATE);
            vertex(pose,consumer,idkInt,vertex,nextRotate,next,move,compute3f,uvs[2],uvs[1],computeNormal);
            vertex(pose,consumer,idkInt,vertex,itRotate,it,move,compute3f,uvs[0],uvs[1],computeNormal);
        }
    }

    public static float SECTION_WIDTH=3/16f;
    public static float SECTION_HEIGHT=2/16f;

    public static void getSectionUvs(float[] array,int sectionIndex,int faceIndex){
        array[0]=(sectionIndex/*&1*/)*SECTION_WIDTH;
        array[1]=(faceIndex/*+ (sectionIndex >> 1) *4*/)*SECTION_HEIGHT;
        array[2]=array[0]+SECTION_WIDTH;
        array[3]=array[1]+SECTION_HEIGHT;
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, int idkInt,
                              Vector3f originVertex,Quaternionf rotate,Vector3f sectionMove,Vector3f move,Vector3f compute3f,
                              float uvx, float uvy,Vector3f normal
    ){
        compute3f.set(originVertex);
        compute3f.rotate(rotate);
        compute3f.add(sectionMove);
        compute3f.add(move);
        compute3f.mul(Vector3fUtils.ENTITY_SCALE);
        vertex(pose,consumer,idkInt,compute3f.x,compute3f.y,compute3f.z,uvx,uvy,normal);
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, int idkInt,
                              float x, float y, float z,
                              float uvx, float uvy,Vector3f normal) {
        consumer.vertex(pose.pose(), x, y, z).color(r, g, b, a).uv(uvx, uvy).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(idkInt).normal(pose.normal(), normal.x,normal.y,normal.z).endVertex();
    }
}
