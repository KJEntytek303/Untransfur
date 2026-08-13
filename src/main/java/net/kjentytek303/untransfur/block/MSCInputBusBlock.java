package net.kjentytek303.untransfur.block;

import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.block_entity.MSCInputBusBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import static net.kjentytek303.untransfur.init.InitBlockEntities.MSC_INPUT_BUS_BLOCK_ENTITY;


public class MSCInputBusBlock extends BaseEntityBlock {

	public static final DirectionProperty FACING = DirectionalBlock.FACING;

	public MSCInputBusBlock(Properties pProperties) {
		super(pProperties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		return this.defaultBlockState().setValue(FACING, pContext.getNearestLookingDirection().getOpposite());
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(new Property[]{FACING});
	}

	@Override
	public RenderShape getRenderShape( BlockState state ) {
		return RenderShape.MODEL;
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new MSCInputBusBlockEntity(pPos, pState);
	}

	@Override
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
		if( entity instanceof MSCInputBusBlockEntity msc_in ) {
			NetworkHooks.openScreen((ServerPlayer) player, msc_in, pos);
			return InteractionResult.sidedSuccess(false);
		}
		Untransfur.LOGGER.error("Assertion failed: Missing container provider in MSCInputBusBlock", new IllegalStateException("Missing container provider"));
		//unreachable.
		return InteractionResult.sidedSuccess(false);
	}

	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> be_type) {
		if( level.isClientSide() ) {
			return null;
		}

		return createTickerHelper(be_type, MSC_INPUT_BUS_BLOCK_ENTITY.get(),
			(level1, pos1, state1, bentity ) -> {
				bentity.tick(level1, pos1, state1);
			}
		);
	}
}
