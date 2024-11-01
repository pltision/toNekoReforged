package yee.pltision.tonekoreforged.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.datagen.TnDamageType;

@SuppressWarnings("NullableProblems")
public class PunjiEndRod extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED= BlockStateProperties.WATERLOGGED;

    private static final VoxelShape BASE_SHAPE_UP = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 11.0D, 11.0D);

    public PunjiEndRod(Properties p_154025_) {
        super(p_154025_);
    }

    //复制自末地烛
    @Override
    public void animateTick(@NotNull BlockState p_221870_, @NotNull Level p_221871_, BlockPos p_221872_, RandomSource p_221873_) {
        Direction direction = Direction.UP;
        double d0 = (double)p_221872_.getX() + 0.55D - (double)(p_221873_.nextFloat() * 0.1F);
        double d1 = (double)p_221872_.getY() + 0.55D - (double)(p_221873_.nextFloat() * 0.1F);
        double d2 = (double)p_221872_.getZ() + 0.55D - (double)(p_221873_.nextFloat() * 0.1F);
        float d3 = 0.4F - (p_221873_.nextFloat() + p_221873_.nextFloat()) * 0.4F;
        if (p_221873_.nextInt(5) == 0) {
            p_221871_.addParticle(ParticleTypes.END_ROD, d0 + (double)direction.getStepX() * d3, d1 + (double)direction.getStepY() * d3, d2 + (double)direction.getStepZ() * d3, p_221873_.nextGaussian() * 0.005D, p_221873_.nextGaussian() * 0.005D, p_221873_.nextGaussian() * 0.005D);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        super.createBlockStateDefinition(p_49915_);
        p_49915_.add(WATERLOGGED);
    }
    public BlockState updateShape(BlockState state, Direction direction, BlockState changed, LevelAccessor level, BlockPos pos, BlockPos changedPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return state;
    }


    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter p_154118_, @NotNull BlockPos p_154119_, @NotNull CollisionContext p_154120_) {

        return BASE_SHAPE_UP;
    }

    public void fallOn(Level p_154047_, BlockState p_154048_, BlockPos p_154049_, Entity p_154050_, float p_154051_) {
        p_154050_.causeFallDamage(p_154051_ + 2.0F, 2.0F, ToNeko.damageSource(p_154047_, TnDamageType.FALL_ON_END_ROD));
    }
    public FluidState getFluidState(BlockState p_154235_) {
        return p_154235_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_154235_);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }
}
