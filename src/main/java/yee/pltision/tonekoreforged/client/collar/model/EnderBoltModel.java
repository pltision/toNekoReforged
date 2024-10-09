package yee.pltision.tonekoreforged.client.collar.model;
// Made with Blockbench 4.11.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.NotNull;

public class EnderBoltModel extends Model {
	public final ModelPart main;
	public final ModelPart root;

	public EnderBoltModel(ModelPart root) {
        super(RenderType::entityCutout);
        this.main = root.getChild("main");
		this.main.getChild("bone");
		this.main.getChild("bone2");
        this.root = root;
    }

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.5F, -1.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(-0.5F))
				.texOffs(0, 0).addBox(-5.0F, -2.5F, -1.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(-0.5F))
				.texOffs(0, 10).addBox(-4.0F, -1.5F, 0.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.25F)), PartPose.offset(2.0F,0.375f,0.25f));

        main.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 10).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(-0.125F))
                .texOffs(0, 10).addBox(-2.0F, -1.0F, 1.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(-0.125F))
                .texOffs(0, 10).addBox(-2.0F, 0.0F, 1.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(-0.125F))
                .texOffs(0, 10).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(-0.125F)), PartPose.offset(-2.0F, -1.0F, -0.5F));

        main.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(0, 6).addBox(-1.0F, -2.5F, -1.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.6F))
                .texOffs(0, 6).addBox(-1.0F, -2.5F, -2.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.6F))
                .texOffs(0, 6).addBox(-1.0F, -1.5F, -1.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.6F))
                .texOffs(0, 6).addBox(-1.0F, -1.5F, -2.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.6F)), PartPose.offset(-2.0F, 0.0F, 1.5F));

        return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}