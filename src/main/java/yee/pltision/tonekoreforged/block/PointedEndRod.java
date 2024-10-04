package yee.pltision.tonekoreforged.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class PointedEndRod extends PointedDripstoneBlock {

    private static final VoxelShape TIP_MERGE_SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 16.0D, 11.0D);
    private static final VoxelShape TIP_SHAPE_UP = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 11.0D, 11.0D);
    private static final VoxelShape TIP_SHAPE_DOWN = Block.box(5.0D, 5.0D, 5.0D, 11.0D, 16.0D, 11.0D);
    private static final VoxelShape FRUSTUM_SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);

    public PointedEndRod(Properties p_154025_) {
        super(p_154025_);
    }

    @Override
    public void animateTick(BlockState p_221870_, @NotNull Level p_221871_, BlockPos p_221872_, RandomSource p_221873_) {
        Direction direction = p_221870_.getValue(PointedDripstoneBlock.TIP_DIRECTION);
        double d0 = (double)p_221872_.getX() + 0.55D - (double)(p_221873_.nextFloat() * 0.1F);
        double d1 = (double)p_221872_.getY() + 0.55D - (double)(p_221873_.nextFloat() * 0.1F);
        double d2 = (double)p_221872_.getZ() + 0.55D - (double)(p_221873_.nextFloat() * 0.1F);
        float d3 = 0.4F - (p_221873_.nextFloat() + p_221873_.nextFloat()) * 0.4F;
        if (p_221873_.nextInt(5) == 0) {
            p_221871_.addParticle(ParticleTypes.END_ROD, d0 + (double)direction.getStepX() * d3, d1 + (double)direction.getStepY() * d3, d2 + (double)direction.getStepZ() * d3, p_221873_.nextGaussian() * 0.005D, p_221873_.nextGaussian() * 0.005D, p_221873_.nextGaussian() * 0.005D);
        }
    }

    public @NotNull VoxelShape getShape(BlockState p_154117_,@NotNull BlockGetter p_154118_,@NotNull BlockPos p_154119_,@NotNull CollisionContext p_154120_) {
        DripstoneThickness dripstonethickness = p_154117_.getValue(THICKNESS);
        VoxelShape voxelshape;
        if (dripstonethickness == DripstoneThickness.TIP_MERGE) {
            voxelshape = TIP_MERGE_SHAPE;
        } else if (dripstonethickness == DripstoneThickness.TIP) {
            if (p_154117_.getValue(TIP_DIRECTION) == Direction.DOWN) {
                voxelshape = TIP_SHAPE_DOWN;
            } else {
                voxelshape = TIP_SHAPE_UP;
            }
        } else{
            voxelshape = FRUSTUM_SHAPE;
        }

        Vec3 vec3 = p_154117_.getOffset(p_154118_, p_154119_);
        return voxelshape.move(vec3.x, 0.0D, vec3.z);
    }


}
