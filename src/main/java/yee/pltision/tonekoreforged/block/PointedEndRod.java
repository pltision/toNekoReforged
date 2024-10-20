package yee.pltision.tonekoreforged.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("NullableProblems")
public class PointedEndRod extends Block implements Fallable, SimpleWaterloggedBlock {
//    public static final EnumProperty<PointedEndRodPiece> PIECE = EnumProperty.create("piece", PointedEndRodPiece.class);

    public static final DirectionProperty TIP_DIRECTION = BlockStateProperties.VERTICAL_DIRECTION;
    public static final BooleanProperty WATERLOGGED= BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty IS_TIP= BooleanProperty.create("is_tip");
    public static final BooleanProperty IS_BASE= BooleanProperty.create("is_base");

    private static final VoxelShape BASE_SHAPE = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    private static final VoxelShape BASE_SHAPE_UP = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 11.0D, 11.0D);
    private static final VoxelShape BASE_SHAPE_DOWN = Block.box(5.0D, 5.0D, 5.0D, 11.0D, 16.0D, 11.0D);
    private static final VoxelShape TIP_SHAPE_UP = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 11.0D, 9.0D);
    private static final VoxelShape TIP_SHAPE_DOWN = Block.box(7.0D, 5.0d, 7.0D, 9.0D, 16.0D, 9.0D);
    private static final VoxelShape MID_SHAPE = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D);

    public PointedEndRod(Properties p_154025_) {
        super(p_154025_);
        registerDefaultState(defaultBlockState().setValue(IS_BASE,true).setValue(IS_TIP,true));
    }

    //复制自末地烛
    @Override
    public void animateTick(BlockState p_221870_, @NotNull Level p_221871_, BlockPos p_221872_, RandomSource p_221873_) {
        Direction direction = p_221870_.getValue(TIP_DIRECTION);
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
        p_49915_.add(TIP_DIRECTION,WATERLOGGED,IS_BASE,IS_TIP);
    }

    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter p_154118_, @NotNull BlockPos p_154119_, @NotNull CollisionContext p_154120_) {
        VoxelShape voxelshape;

        if(state.getValue(TIP_DIRECTION)==Direction.UP){
            if(state.getValue(IS_BASE)) {
                if(state.getValue(IS_TIP))
                    voxelshape=BASE_SHAPE_UP;
                else
                    voxelshape=BASE_SHAPE;
            }
            else {
                if(state.getValue(IS_TIP))
                    voxelshape=TIP_SHAPE_UP;
                else
                    voxelshape=MID_SHAPE;
            }
        }
        else{
            if(state.getValue(IS_BASE)) {
                if(state.getValue(IS_TIP))
                    voxelshape=BASE_SHAPE_DOWN;
                else
                    voxelshape=BASE_SHAPE;
            }
            else {
                if(state.getValue(IS_TIP))
                    voxelshape=TIP_SHAPE_DOWN;
                else
                    voxelshape=MID_SHAPE;
            }
        }

        Vec3 vec3 = state.getOffset(p_154118_, p_154119_);
        return voxelshape.move(vec3.x, 0.0D, vec3.z);
    }

    public void fallOn(Level p_154047_, BlockState p_154048_, BlockPos p_154049_, Entity p_154050_, float p_154051_) {
        if (p_154048_.getValue(TIP_DIRECTION) == Direction.UP && p_154048_.getValue(IS_TIP)) {
            p_154050_.causeFallDamage(p_154051_ + 2.0F, 2.0F, p_154047_.damageSources().stalagmite());
        } else {
            super.fallOn(p_154047_, p_154048_, p_154049_, p_154050_, p_154051_);
        }

    }
    public @NotNull DamageSource getFallDamageSource(Entity p_254432_) {
        return p_254432_.damageSources().fallingStalactite(p_254432_);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction direction = context.getNearestLookingVerticalDirection();
        Direction tipDirection = getCanSurviveDirection(level, pos, direction.getAxis()== Direction.Axis.Y?direction:Direction.DOWN);
        if (tipDirection == null) {
            return null;
        } else {
            return this.defaultBlockState()
                    .setValue(TIP_DIRECTION, tipDirection.getOpposite())
                    .setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER)
                    .setValue(IS_BASE,!level.getBlockState(pos.relative(direction)).is(this))
                    .setValue(IS_TIP,!level.getBlockState(pos.relative(direction.getOpposite())).is(this));
        }
    }

    public Direction getCanSurviveDirection(LevelReader levelReader, BlockPos pos, Direction firstPlaceTo){
        BlockState checkState;
        BlockPos checkPos;

        checkPos=pos.relative(firstPlaceTo);
        checkState=levelReader.getBlockState(checkPos);
        if(checkState.is(this) || checkState.isFaceSturdy(levelReader, checkPos, firstPlaceTo.getOpposite()))
            return firstPlaceTo;

        checkPos=pos.relative(firstPlaceTo.getOpposite());
        checkState=levelReader.getBlockState(checkPos);
        if(checkState.is(this) || checkState.isFaceSturdy(levelReader, checkPos, firstPlaceTo))
            return firstPlaceTo.getOpposite();

        return null;
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState changed, LevelAccessor level, BlockPos pos, BlockPos changedPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        if (direction == Direction.UP || direction == Direction.DOWN){
            if(!canSurvive(state,level,pos)){
                level.scheduleTick(pos, this, state.getValue(TIP_DIRECTION)==Direction.DOWN?2:1);
            }
            if(direction==state.getValue(TIP_DIRECTION)){
                return state.setValue(IS_TIP,!changed.is(this));
            }
            /*else{
                if (state.getValue(IS_BASE)&& !changed.is(this))
                    return state.setValue(IS_BASE,true);
            }*/
        }
        return state;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos pos) {
        Direction direction=state.getValue(TIP_DIRECTION);
        BlockPos checkPos=pos.relative(direction.getOpposite());
        BlockState checkState=levelReader.getBlockState(checkPos);
        return checkState.is(this) || checkState.isFaceSturdy(levelReader, checkPos, direction);
    }
}
