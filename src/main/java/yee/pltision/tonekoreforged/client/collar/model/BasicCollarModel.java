package yee.pltision.tonekoreforged.client.collar.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.NotNull;

public class BasicCollarModel extends CollarModel{
    public final ModelPart collar;
    public final ModelPart root;

    public BasicCollarModel(ModelPart root) {
        super(RenderType::entityCutout);
        this.collar = root.getChild("toneko:collar");
        this.root=root;
    }
    public static LayerDefinition createMesh() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("toneko:collar", CubeListBuilder.create()
                .texOffs(0,0)
                .addBox(-3.0F, -1.0F, -2.0F, 6.0F, 1.0F, 4.0F, new CubeDeformation(0.0f)), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer consumer,
                               int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack,consumer,packedLight,packedOverlay,red,green,blue,alpha);
    }
}
