package yee.pltision.tonekoreforged.client.collar;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.client.collar.model.BasicCollarModel;
import yee.pltision.tonekoreforged.client.collar.model.EnderBoltModel;

@OnlyIn(Dist.CLIENT)
public class CollarRendererInstances {
    public static CollarRenderer<LivingEntity, HumanoidModel<LivingEntity>> BASIC_COLLAR_RENDERER;
    public static EnderBoltRenderer<LivingEntity, HumanoidModel<LivingEntity>> ENDER_BOLT_RENDERER;

    public static void initInstance(EntityRendererProvider.Context context){
        BASIC_COLLAR_RENDERER=new CollarRenderer<>(new BasicCollarModel(context.bakeLayer(BASIC_COLLAR_LOCATION)));
        ENDER_BOLT_RENDERER=new EnderBoltRenderer<>(new EnderBoltModel(context.bakeLayer(ENDER_BOLT_LOCATION)));
    }

    public static final ModelLayerLocation BASIC_COLLAR_LOCATION =new ModelLayerLocation(ToNeko.location("basic_collar"),"collar");
    public static final ModelLayerLocation ENDER_BOLT_LOCATION =new ModelLayerLocation(ToNeko.location("ender_blot"),"collar");

    public static void registry(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(BASIC_COLLAR_LOCATION, BasicCollarModel::createMesh);
        event.registerLayerDefinition(ENDER_BOLT_LOCATION, EnderBoltModel::createBodyLayer);
    }
}
