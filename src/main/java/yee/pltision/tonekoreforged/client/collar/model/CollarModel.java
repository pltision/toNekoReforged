package yee.pltision.tonekoreforged.client.collar.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class CollarModel extends Model {
    public CollarModel(Function<ResourceLocation, RenderType> p_103110_) {
        super(p_103110_);
    }

    public static MeshDefinition createMesh(CubeDeformation cubeDeformation) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("toneko:collar", CubeListBuilder.create()
                .addBox(-3.0F, -1.0F, -2.0F, 3.0F, 0.0F, 2.0F, cubeDeformation), PartPose.ZERO);
        return meshdefinition;
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer consumer,
                               int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

    }
}
