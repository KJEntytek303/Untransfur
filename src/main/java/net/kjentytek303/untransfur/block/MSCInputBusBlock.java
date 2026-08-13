package net.kjentytek303.untransfur.block;

import net.kjentytek303.untransfur.block_entity.MSCInputBusBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import static net.kjentytek303.untransfur.init.InitBlockEntities.MSC_INPUT_BUS_BLOCK_ENTITY;


public class MSCInputBusBlock extends AbstractMSCBusBlock{

	public MSCInputBusBlock(Properties pProperties) {
		super(pProperties);
	}
	private static void tick(Level level1, BlockPos pos1, BlockState state1, MSCInputBusBlockEntity bentity) {
		bentity.tick(level1, pos1, state1);
	}
	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new MSCInputBusBlockEntity(pPos, pState);
	}

	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> be_type) {
		if( level.isClientSide() ) {
			return null;
		}

		return createTickerHelper(be_type, MSC_INPUT_BUS_BLOCK_ENTITY.get(), MSCInputBusBlock::tick
		);
	}
}
