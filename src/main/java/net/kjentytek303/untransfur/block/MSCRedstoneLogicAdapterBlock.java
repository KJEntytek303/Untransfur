package net.kjentytek303.untransfur.block;

import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.block_entity.MSCRedstoneLogicAdapterBlockEntity;
import net.kjentytek303.untransfur.init.InitBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;


public class MSCRedstoneLogicAdapterBlock extends AbstractMSCBlock {

	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	public MSCRedstoneLogicAdapterBlock(Properties pProperties) {
		super(pProperties);
	}

	private static void tick(Level level1, BlockPos pos1, BlockState state1, MSCRedstoneLogicAdapterBlockEntity bentity) {
		bentity.tick(level1, pos1, state1);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		return this.defaultBlockState().setValue(FACING, pContext.getNearestLookingDirection().getOpposite()).setValue(POWERED, false);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(POWERED);
	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return state.getValue(POWERED);
	}

	@Override
	public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction side) {
		return this.isSignalSource(state) && side == state.getValue(FACING).getOpposite() ? 15 : 0;
		//TODO: Make it return only when the MSC is running that program
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new MSCRedstoneLogicAdapterBlockEntity(pPos, pState);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> be_type) {
		if(level.isClientSide) {
			return null;
		}
		return createTickerHelper(be_type, InitBlockEntities.MSC_REDSTONE_LOGIC_ADAPTER_BE.get(), MSCRedstoneLogicAdapterBlock::tick);
	}

	public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
		if( pState.getBlock() == pNewState.getBlock()) {
			super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
			return;
		}

		BlockEntity blockentity = pLevel.getBlockEntity(pPos);
		if (blockentity instanceof Container container) {
			Containers.dropContents(pLevel, pPos, container);
			pLevel.updateNeighbourForOutputSignal(pPos, this);
		}
		super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
	}

	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if( level.isClientSide() ) {
			return InteractionResult.sidedSuccess(true);
		}

		BlockEntity entity = level.getBlockEntity(pos);
		if( entity instanceof MSCRedstoneLogicAdapterBlockEntity msc_re) {
			NetworkHooks.openScreen((ServerPlayer) player, msc_re, pos);
			return InteractionResult.sidedSuccess(false);
		}

		Untransfur.LOGGER.error("Assertion failed: Missing container provider in MSCRedstoneLogicAdapter");
		for( var str : Thread.currentThread().getStackTrace()) {
			Untransfur.LOGGER.error(str.toString());
		}
		return InteractionResult.FAIL;
	}

}
