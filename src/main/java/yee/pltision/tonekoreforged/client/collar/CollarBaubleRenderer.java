package yee.pltision.tonekoreforged.client.collar;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.collar.CollarState;

@OnlyIn(Dist.CLIENT)
public interface CollarBaubleRenderer<E,M extends Model> {
    default @Nullable M tryCastModel(Model model){return (M)model;}

    default void render(@NotNull PoseStack stack, @NotNull MultiBufferSource source, int idkInt, @NotNull E entity, float neverMind1, float neverMind2, float neverMind3, float neverMind4, float neverMind5, float neverMind6, CollarState collarState, CollarRenderHelper<E,M> renderHelper,int slot,M model){
        render(stack,source,idkInt,entity,collarState,renderHelper,slot, model);
    }

    default void render(PoseStack stack, MultiBufferSource source, int idkInt, E entity,CollarState collarState, CollarRenderHelper<E,M> renderHelper,int slot,M model){}

}
