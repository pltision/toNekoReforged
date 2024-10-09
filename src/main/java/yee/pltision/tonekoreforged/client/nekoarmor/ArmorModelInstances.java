package yee.pltision.tonekoreforged.client.nekoarmor;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import yee.pltision.tonekoreforged.ToNeko;

@OnlyIn(Dist.CLIENT)
public class ArmorModelInstances {

    public static EarsModel EARS;

    public static void initInstance(EntityRendererProvider.Context context){
        EARS =new EarsModel(context.bakeLayer(EARS_LOCATION));
    }

    public static final ModelLayerLocation EARS_LOCATION=new ModelLayerLocation(ToNeko.location("ears"),"ears");

    public static void registry(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(EARS_LOCATION, EarsModel::createBodyLayer);

    }
}
