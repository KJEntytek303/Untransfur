package net.kjentytek303.untransfur.block;

import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.block_entity.MSCInputBusBlockEntity;
import net.kjentytek303.untransfur.block_entity.MSCOutputBusBlockEntity;
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
import static net.kjentytek303.untransfur.init.InitBlockEntities.MSC_OUTPUT_BUS_BLOCK_ENTITY;


public class MSCOutputBusBlock extends AbstractMSCBusBlock {

	public MSCOutputBusBlock(Properties pProperties) {
		super(pProperties);
	}
	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new MSCOutputBusBlockEntity(pPos, pState);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> be_type) {
		if( level.isClientSide() ) {
			return null;
		}

		return createTickerHelper(be_type, MSC_OUTPUT_BUS_BLOCK_ENTITY.get(),
			(level1, pos1, state1, bentity ) -> {
				bentity.tick(level1, pos1, state1);
			}
		);
	}

}