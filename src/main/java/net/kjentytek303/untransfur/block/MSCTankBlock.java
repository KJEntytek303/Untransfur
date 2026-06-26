package net.kjentytek303.untransfur.block;
/*
import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.block_entity.MSCTankBlockEntity;
import net.kjentytek303.untransfur.init.InitBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;


public class MSCTankBlock extends BaseEntityBlock {

	public MSCTankBlock(Properties pProperties) { super(pProperties); }


	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new MSCTankBlockEntity( pPos, pState );
	}

	@Override
	public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
		if (pState.getBlock() != pNewState.getBlock()) {
			BlockEntity be = pLevel.getBlockEntity(pPos);
			if( be instanceof MSCTankBlockEntity be1) {
				be1.drops();
			}
		}
		super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
	}

	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if( !pLevel.isClientSide() ) {
			BlockEntity be = pLevel.getBlockEntity(pPos);
			if ( be instanceof MSCTankBlockEntity be1 ) {
				NetworkHooks.openScreen(((ServerPlayer) pPlayer), be1, pPos);
			} else {
				Untransfur.LOGGER.error("Missing MSC Tank container provider", new IllegalStateException("Missing MSC TankContainer provider"));
			}
		}

		return InteractionResult.sidedSuccess(pLevel.isClientSide());
	}


	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
		if( pLevel.isClientSide()){
			return null;
		}
		return createTickerHelper(pBlockEntityType, InitBlockEntities.MSC_TANK_BLOCK_ENTITY.get(),
			((pLevel1, pPos, pState1, pBlockEntity) ->
				pBlockEntity.tick(pLevel1, pPos, pState1))
		);
	}

}
*/