package yee.pltision.tonekoreforged.client.collar;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import yee.pltision.tonekoreforged.collar.CollarState;

@OnlyIn(Dist.CLIENT)
public interface CollarRenderHelper<E,M extends Model>
{
    boolean isTrueModel(Model model);

    void render(@NotNull PoseStack stack, @NotNull MultiBufferSource source, int idkInt, @NotNull E entity, float neverMind1, float neverMind2, float neverMind3, float neverMind4, float neverMind5, float neverMind6, M model);

    void getTiePos(E entity, CollarState state, M model, int slot, Vector3f top, Vector3f button, Matrix3f transform, Quaternionf rotate);

}
