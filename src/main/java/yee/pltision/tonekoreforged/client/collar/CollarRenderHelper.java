package yee.pltision.tonekoreforged.client.collar;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import yee.pltision.tonekoreforged.collar.CollarState;

public interface CollarRenderHelper<E,M extends Model>
{
    boolean canUseModel(Model model);

    void render(@NotNull PoseStack stack, @NotNull MultiBufferSource source, int light, @NotNull E entity,CollarState state,
                float position, float speed, float partialTick, float bob, float headRotateYFromBody, float xRot,
                M model);

    void setStack(@NotNull PoseStack stack, int light, @NotNull E entity,CollarState state,  int slot,
               float position, float speed, float partialTick, float bob, float headRotateYFromBody, float xRot,
               M model);

    @Deprecated
    void getTiePos(E entity, CollarState state, M model, int slot, Vector3f top, Vector3f button, Quaternionf collarRotate, Quaternionf positionRotate);

    default <A,B extends Model> CollarRenderHelper<A,B> cast(){
        return (CollarRenderHelper<A, B>) this;
    }
}
