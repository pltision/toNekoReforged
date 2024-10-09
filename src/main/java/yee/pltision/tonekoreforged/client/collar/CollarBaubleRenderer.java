package yee.pltision.tonekoreforged.client.collar;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.collar.CollarState;

@OnlyIn(Dist.CLIENT)
public interface CollarBaubleRenderer<E,M extends Model> extends INBTSerializable<CompoundTag> {
    default @Nullable M tryCastModel(Model model){return (M)model;}

    default void renderBeforePushStack(@NotNull PoseStack stack, @NotNull MultiBufferSource source, int idkInt, @NotNull E entity,
                                       float position, float speed, float partialTick, float bob, float headRotateYFromBody, float xRot,
                                       CollarState collarState, CollarRenderHelper<E,M> renderHelper, int slot, M model){
    }


    default void render(@NotNull PoseStack stack, @NotNull MultiBufferSource source, int idkInt, @NotNull E entity,
                                       float position, float speed, float partialTick, float bob, float headRotateYFromBody, float xRot,
                                       CollarState collarState, CollarRenderHelper<E,M> renderHelper, int slot, M model){
    }

    default <A,B extends Model> CollarBaubleRenderer<A,B> cast(){
        return (CollarBaubleRenderer<A, B>) this;
    }
}
