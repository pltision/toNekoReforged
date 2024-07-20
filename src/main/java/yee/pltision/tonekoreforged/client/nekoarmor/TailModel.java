package yee.pltision.tonekoreforged.client.nekoarmor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.NotNull;

public class TailModel extends Model {
    public TailModel() {
        super(RenderType::entityCutoutNoCull);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack p_103111_, @NotNull VertexConsumer p_103112_, int p_103113_, int p_103114_, float p_103115_, float p_103116_, float p_103117_, float p_103118_) {

    }
}
