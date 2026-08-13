package net.kjentytek303.untransfur.block;

import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.block_entity.AbstractMSCBusBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;


public abstract class AbstractMSCBusBlock extends AbstractMSCBlock {
	protected AbstractMSCBusBlock(Properties pProperties) {
		super(pProperties);
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
		if( entity instanceof AbstractMSCBusBlockEntity msc_in ) {
			NetworkHooks.openScreen((ServerPlayer) player, msc_in, pos);
			return InteractionResult.sidedSuccess(false);
		}

		Untransfur.LOGGER.error("Assertion failed: Missing container provider in MSCOutputBusBlock");
		for( var str : Thread.currentThread().getStackTrace()) {
			Untransfur.LOGGER.error(str.toString());
		}
		return InteractionResult.FAIL;
	}
}
