package yee.pltision.tonekoreforged.client.collar;

import com.mojang.blaze3d.vertex.PoseStack;
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
import yee.pltision.tonekoreforged.client.collar.model.BasicCollarModel;
import yee.pltision.tonekoreforged.collar.CollarState;

import java.lang.Math;

@OnlyIn(Dist.CLIENT)
public class CollarRenderer<E extends LivingEntity,M extends HumanoidModel<E>> implements CollarRenderHelper<E,M> {
    public static final ResourceLocation COLLAR = ToNeko.location("textures/entity/collar.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(COLLAR);
    BasicCollarModel collarModel;

    public CollarRenderer(BasicCollarModel model){
        collarModel=model;
    }

    public static final Vector2f SIZE = new Vector2f(3, 2);

    public static Matrix2f RIGHT_FONT = new Matrix2f(1, 0, 0, 1);
    public static Matrix2f RIGHT_BACK = new Matrix2f(1, 0, 0, -1);
    public static Matrix2f LEFT_BACK = new Matrix2f(-1, 0, 0, -1);
    public static Matrix2f LEFT_FONT = new Matrix2f(-1, 0, 0, 1);

    @Override
    public boolean canUseModel(Model model) {
        return model instanceof HumanoidModel<?>;
    }

    public static void getRotate(HumanoidModel<?> model,Quaternionf rotate){
        rotate.rotateY(model.head.yRot);

//        rotate.rotateX(model.body.xRot);
//        rotate.rotateY(model.body.yRot);
//        rotate.rotateZ(model.body.zRot);

        rotate.rotateX((model.head.xRot+model.body.xRot)/3);
    }

    public static Vector3f getBodyMove(HumanoidModel<?> model){
        return new Vector3f(model.body.x,model.body.y,model.body.z);
    }
    public static Vector3f getMove(HumanoidModel<?> model){
        return new Vector3f(0,
                Math.abs((model.head.xRot+model.body.xRot)/Mth.PI*2*0.2f),
                (float) Math.sin(model.head.xRot+model.body.xRot)*-1.5f);
    }
    public static void transformPos(Vector3f vector, Vector3f move, Quaternionf transform, Vector3f afterMove){
        vector.add(move);
        vector.rotate(transform);
        vector.mul(Vector3fUtils.ENTITY_SCALE);
        vector.add(afterMove);
    }

    @Override
    public void render(@NotNull PoseStack stack, @NotNull MultiBufferSource source, int light, @NotNull E entity,CollarState state, float position, float speed, float partialTick, float bob, float headRotateYFromBody, float xRot, M model) {

        Vector3f move=getMove(model);
        Vector3f afterMove=getBodyMove(model)/*.mul(Vector3fUtils.ENTITY_SCALE)*/;

        collarModel.root.x=afterMove.x;
        collarModel.root.y=afterMove.y;
        collarModel.root.z=afterMove.z;
        collarModel.root.yRot=model.head.yRot;
        collarModel.root.xRot=(model.head.xRot+model.body.xRot)/3;
        collarModel.root.zRot=model.head.zRot;
        collarModel.collar.x=move.x;
        collarModel.collar.y=move.y;
        collarModel.collar.z=move.z;

        collarModel.renderToBuffer(stack,source.getBuffer(RENDER_TYPE),light,OverlayTexture.NO_OVERLAY,1,1,1,1);

        Matrix3f d3transform=new Matrix3f();

        Quaternionf rotate=new Quaternionf();
        getRotate(model,rotate);
        d3transform.rotate(rotate);

    }

    @Override
    public void setStack(@NotNull PoseStack stack, int light, @NotNull E entity, CollarState state, int slot, float position, float speed, float partialTick, float bob, float headRotateYFromBody, float xRot, M model) {
        Vector2f compute2f=new Vector2f();
        Quaternionf baubleRotate=new Quaternionf();

        switch (slot) {
            case 0 -> {
                compute2f.set(SIZE).mul(LEFT_BACK);
                compute2f.add(1.5f, 0);
                baubleRotate.rotateY(Mth.PI);
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
                //baubleRotate.rotateY(0);
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
                //baubleRotate.rotateY(0);
            }
            case 1 -> {
                compute2f.set(SIZE).y=0;
                compute2f.mul(RIGHT_FONT);
                baubleRotate.rotateY(Mth.HALF_PI);
            }
            case 4 -> {
                compute2f.set(SIZE).y=0;
                compute2f.mul(LEFT_FONT);
                baubleRotate.rotateY(-Mth.HALF_PI);
            }
        }
        compute2f.mul(1/16f);

        Quaternionf quaternionf=new Quaternionf();
        getRotate(model,quaternionf);
        Vector3f afterMove=getBodyMove(model).mul(Vector3fUtils.ENTITY_SCALE);
        Vector3f move=getMove(model).mul(Vector3fUtils.ENTITY_SCALE);

        stack.translate(afterMove.x,afterMove.y,afterMove.z);
        stack.rotateAround(quaternionf,0,0,0);
        stack.translate(move.x,move.y,move.z);
        stack.translate(compute2f.x(),0,compute2f.y());

        stack.rotateAround(baubleRotate,0,0,0);
    }

    @Override
    public void getTiePos(E entity, CollarState state, M model, int slot, Vector3f top, Vector3f button, Quaternionf collarRotate, Quaternionf positionRotate) {
        getRotate(model,collarRotate);

        Vector2f compute2f=new Vector2f();
        Vector3f afterMove=getBodyMove(model).mul(Vector3fUtils.ENTITY_SCALE);
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
        transformPos(top,move,collarRotate,afterMove);
        transformPos(button,move,collarRotate,afterMove);
    }
}
