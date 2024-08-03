// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class ears - Converted<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "ears_- converted"), "main");
	private final ModelPart head;
	private final ModelPart ears;
	private final ModelPart right;
	private final ModelPart left;
	private final ModelPart color_fur;
	private final ModelPart color_inside;

	public ears - Converted(ModelPart root) {
		this.head = root.getChild("head");
		this.ears = root.getChild("ears");
		this.right = root.getChild("right");
		this.left = root.getChild("left");
		this.color_fur = root.getChild("color_fur");
		this.color_inside = root.getChild("color_inside");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition ears = head.addOrReplaceChild("ears", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right = ears.addOrReplaceChild("right", CubeListBuilder.create().texOffs(8, 4).mirror().addBox(-1.5F, -1.5F, -1.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(8, 0).addBox(-1.5F, -4.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).mirror().addBox(-1.5F, -3.0F, 0.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(4, 4).addBox(-2.5F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 4).mirror().addBox(0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(3.0F, -7.0F, 0.0F));

		PartDefinition left = ears.addOrReplaceChild("left", CubeListBuilder.create().texOffs(8, 4).addBox(-0.5F, -1.5F, -1.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(8, 0).addBox(-1.5F, -4.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.5F, -3.0F, 0.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(4, 4).addBox(1.5F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 4).addBox(-1.5F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -7.0F, 0.0F));

		PartDefinition color_fur = head.addOrReplaceChild("color_fur", CubeListBuilder.create().texOffs(8, 12).mirror().addBox(1.5F, -8.5F, -1.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(8, 12).addBox(-3.5F, -8.5F, -1.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition color_inside = head.addOrReplaceChild("color_inside", CubeListBuilder.create().texOffs(0, 8).mirror().addBox(1.5F, -10.0F, 0.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 8).addBox(-3.5F, -10.0F, 0.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}