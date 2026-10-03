package net.kjentytek303.untransfur.block;

import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.kjentytek303.untransfur.init.InitBlockEntities;
import net.kjentytek303.untransfur.msc.ControllerStatus;
import net.kjentytek303.untransfur.util.BlockUtilities;
import net.ltxprogrammer.changed.block.SeatableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;


public class MSCControllerBlock extends BaseEntityBlock implements SeatableBlock {
	public MSCControllerBlock ( Properties properties ) {
		super(properties);
		this.registerDefaultState( this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(STATUS, ControllerStatus.DISASSEMBLED));
	}

	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final EnumProperty<ControllerStatus> STATUS = EnumProperty.create("status", ControllerStatus.class);

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
		builder.add(STATUS);
	}

	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState()
			.setValue(FACING, context.getHorizontalDirection().getOpposite())
			.setValue(STATUS, ControllerStatus.DISASSEMBLED)
			;
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
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
			if(msc.checkMultiblock(pLevel, pPos, pPlayer) && !msc.isCrashed() && msc.current_command == null) {
				setStatus(pState, pLevel, pPos, ControllerStatus.INACTIVE);
			}
		}

		return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
	}


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
		if(pLevel.isClientSide()) {
			return null;
		}
		return createTickerHelper(pBlockEntityType, InitBlockEntities.MSC_CONTROLLER_BLOCK_ENTITY.get(), MSCControllerBlockEntity::serverTick);
	}
	@Override
	public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
		//TODO: Refactor
		if( pState.is(pNewState.getBlock()) || !(pLevel.getBlockEntity(pPos) instanceof MSCControllerBlockEntity msc) ) {
			super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
			return;
		}

		if( msc.multiblock_valid ) {
			msc.invalidateMultiblock(true);
		}

		if( msc.checkForBlowUp() ) {
			msc.blowUp();
		}
		pLevel.removeBlockEntity(pPos);

		super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
	}

	@Override
	public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
		return super.rotate(state, level, pos, direction);
	}

	@Override
	public @Nullable PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.BLOCK;
	}

	public static AABB getDetectionSize(BlockState msc_controller, BlockPos pos ) {
		Vec3 controller_center = pos.getCenter();
		Vec3 right_bottom_front = BlockUtilities.TransformHorizontalDirectionVec3(controller_center, msc_controller.getValue(FACING).getOpposite(), 1.5, 0, -0.5);
		Vec3 left_top_back = BlockUtilities.TransformHorizontalDirectionVec3(controller_center, msc_controller.getValue(FACING).getOpposite(), -1.5, 6.5, -3.5);

		return new AABB( left_top_back, right_bottom_front).inflate(0.125);
	}

	public static AABB getDetectionSizeForExit(BlockState msc_controller, BlockPos pos) {
		Vec3 controller_center = pos.getCenter();
		Vec3 right_bottom_front = BlockUtilities.TransformHorizontalDirectionVec3(controller_center, msc_controller.getValue(FACING).getOpposite(), 1.5, 0, 1.0);
		Vec3 left_top_back = BlockUtilities.TransformHorizontalDirectionVec3(controller_center, msc_controller.getValue(FACING).getOpposite(), -1.5, 6.5, -3.5);

		return new AABB( left_top_back, right_bottom_front).inflate(0.125);
	}

	public static AABB getDetectionSizeForEntrance( BlockState msc_controller, BlockPos pos ) {


		Vec3 controller_center = pos.getCenter();
		Vec3 right_bottom_front = BlockUtilities.TransformHorizontalDirectionVec3(controller_center, msc_controller.getValue(FACING).getOpposite(), 1.5, 0, -1.0);
		Vec3 left_top_back = BlockUtilities.TransformHorizontalDirectionVec3(controller_center, msc_controller.getValue(FACING).getOpposite(), -1.5, 6.5, -3.5);

		return new AABB( left_top_back, right_bottom_front).inflate(0.125);
	}

	@Nullable
	public ControllerStatus setStatus( BlockState state, Level level, BlockPos pos, ControllerStatus status ) {
		if(state.is(this)) {
			var ret = state.getValue(STATUS);
			level.setBlockAndUpdate(pos, state.setValue(STATUS, status));
			return ret;
		}
		return null;
	}
	@Override
	public Vec3 getSitOffset(BlockGetter block_getter, BlockState block_state, BlockPos block_pos) {
		BlockPos ret = BlockUtilities.TransformHorizontalDirectionInt(new BlockPos(0, 0, 0), block_state.getValue(FACING).getOpposite(), 0, 1, -2);
		return new Vec3( ret.getX(), ret.getY(), ret.getZ());
	}
}
