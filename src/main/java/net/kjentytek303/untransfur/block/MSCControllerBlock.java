package net.kjentytek303.untransfur.block;

import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.kjentytek303.untransfur.config.ServerCfg;
import net.kjentytek303.untransfur.util.BlockUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;


public class MSCControllerBlock extends BaseEntityBlock {
	public MSCControllerBlock ( Properties properties ) {
		super(properties);
		this.registerDefaultState( this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OPEN, false).setValue(ACTIVE, false));
	}
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, OPEN, ACTIVE);
	}

	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return (BlockState)state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}
	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if (pLevel.isClientSide() ) {
			return InteractionResult.SUCCESS;
		}
		BlockEntity be = pLevel.getBlockEntity(pPos);
		if( be instanceof MSCControllerBlockEntity msc ) {
			msc.checkMultiblock(pLevel, pPos, pPlayer);
		}

		return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
	}

	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

	@Override
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}
	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new MSCControllerBlockEntity( pPos, pState );
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
		return super.getTicker(pLevel, pState, pBlockEntityType);
	}
	@Override
	public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
		if( !pState.is(pNewState.getBlock()) &&
		     ServerCfg.MSC_CHECK_FAILURE_ON_DESTROY.get() &&
		     pLevel.getBlockEntity(pPos) instanceof MSCControllerBlockEntity msc &&
		     msc.checkForBlowUp()
		) { msc.blowUp(); }

		super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
	}

	@Override
	public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
		return super.rotate(state, level, pos, direction);
	}
	/**
	 * Called when a tile entity on a side of this block changes is created or is destroyed.
	 *
	 * @param state
	 * @param level    The level
	 * @param pos      Block position in level
	 * @param neighbor Block position of neighbor
	 */
	@Override
	public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
		super.onNeighborChange(state, level, pos, neighbor);
	}
	/**
	 * Returns the reaction of the block when pushed or pulled by a piston. This method should be not called directly, instead via {@link BlockState#getPistonPushReaction()}.
	 * <ul>
	 *     <li>NORMAL: is pushable and pullable by sticky pistons</li>
	 *     <li>DESTROY: is being destroyed on pushing and pulling</li>
	 *     <li>BLOCK: is not being able to be moved</li>
	 *     <li>IGNORE: only usable by entities</li>
	 *     <li>PUSH_ONLY: can only be pushed, blocks on trying to be pulled</li>
	 *     <li>{@code null}: use the PistonPushReaction from the BlockBehaviour.Properties passed into the Block Constructor</li>
	 * </ul>
	 *
	 * @param state The state of this block
	 * @return the PushReaction of this state or {@code null} if the one passed into the block properties should be used
	 */
	@Override
	public @Nullable PushReaction getPistonPushReaction(BlockState state) {
		return super.getPistonPushReaction(state);
	}

	public AABB getDetectionSize(BlockState msc_controller, BlockPos pos ) {
		BlockPos left_bottom_back = BlockUtilities.TransformHorizontalDirection(pos, msc_controller.getValue(FACING), -1, 0, -3);
		BlockPos right_top_front = BlockUtilities.TransformHorizontalDirection(pos, msc_controller.getValue(FACING), 1, 7, -1);
		return new AABB( left_bottom_back, right_top_front);
	}

	public boolean markAsActive(BlockState state, Level level, BlockPos pos) {
		if( state.getValue(ACTIVE) ) {
			return false;
		}
		if( level.getBlockState(pos).getBlock() != this ) {
			return false;
		}

		level.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
		return true;
	}

	public boolean markAsInActive(BlockState state, Level level, BlockPos pos) {
		if( !state.getValue(ACTIVE) ) {
			return false;
		}
		if( level.getBlockState(pos).getBlock() != this ) {
			return false;
		}

		level.setBlockAndUpdate(pos, state.setValue(ACTIVE, false));
		return true;
	}
}
