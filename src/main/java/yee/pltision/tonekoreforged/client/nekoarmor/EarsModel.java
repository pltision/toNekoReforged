package yee.pltision.tonekoreforged.client.nekoarmor;

// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static net.minecraft.util.Mth.PI;
import static net.minecraft.util.Mth.sin;

public class EarsModel extends EntityModel<Entity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
//    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "ears_- converted"), "main");

//    private final ModelPart ears;
    private final ModelPart head;
    private final ModelPart ears;
    private final ModelPart colorFur;
    private final ModelPart colorInside;

    public EarsModel(ModelPart root) {
        this.head = root.getChild("head");
        this.ears = head.getChild("ears");
        this.colorFur = head.getChild("color_fur");
        this.colorInside = head.getChild("color_inside");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition ears = head.addOrReplaceChild("ears", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        ears.addOrReplaceChild("right", CubeListBuilder.create().texOffs(8, 4).mirror().addBox(-1.5F, -1.5F, -1.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(8, 0).addBox(-1.5F, -4.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).mirror().addBox(-1.5F, -3.0F, 0.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(4, 4).addBox(-2.5F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 4).mirror().addBox(0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(3.0F, -7.0F, 0.0F));

        ears.addOrReplaceChild("left", CubeListBuilder.create().texOffs(8, 4).addBox(-0.5F, -1.5F, -1.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(8, 0).addBox(-1.5F, -4.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-0.5F, -3.0F, 0.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 4).addBox(1.5F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 4).addBox(-1.5F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -7.0F, 0.0F));

        head.addOrReplaceChild("color_fur", CubeListBuilder.create().texOffs(8, 12).mirror().addBox(1.5F, -8.5F, -1.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(8, 12).addBox(-3.5F, -8.5F, -1.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        head.addOrReplaceChild("color_inside", CubeListBuilder.create().texOffs(0, 8).mirror().addBox(1.5F, -10.0F, 0.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 8).addBox(-3.5F, -10.0F, 0.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));



        return LayerDefinition.create(meshdefinition, 16, 16);
    }


    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        ears.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
//        second.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
//        second.render(poseStack, vertexConsumer, packedLight, packedOverlay, Math.min(0.3f+0.8f*red,1), Math.min(0.5f+0.5f*green,1), Math.min(0.5f+0.5f*blue,1), alpha);

        //算法很难写的qwq 我也没法告诉你为啥这样写喵，反正是一点点调出来的...
        float min=min(red, min(green,blue));
        float max=max(red, max(green,blue));
        float lightness=(max+min)/2;
        float saturation=lightness<0.5?(max-min)/(max+min):(max-min)/(2-max-min);

        float sFactory=0.5f+sin(saturation*PI)/2;
        float add=max(0,0.5f-lightness+(red-min(blue,green))/2)/2f;

        colorInside.render(poseStack, vertexConsumer, packedLight, packedOverlay,min(1,max) , min(1,green*sFactory+add), min(1,blue*sFactory+add), alpha);
        colorFur.render(poseStack, vertexConsumer, packedLight, packedOverlay,min(1,max)*0.5f+0.5f , min(1,green*sFactory+add)*0.5f+0.5f, min(1,blue*sFactory+add)*0.5f+0.5f, alpha);
    }

    @Override
    public void setupAnim(Entity p_102618_, float p_102619_, float p_102620_, float p_102621_, float p_102622_, float p_102623_) {

    }

    public void copyPropertiesTo(HumanoidModel<?> p_102625_) {
//        head.copyFrom(p_102625_.head);
        ears.copyFrom(p_102625_.head);
        colorFur.copyFrom(p_102625_.head);
        colorInside.copyFrom(p_102625_.head);
    }
}
