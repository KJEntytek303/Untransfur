package net.kjentytek303.untransfur.block_entity;

import net.kjentytek303.untransfur.init.InitBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static net.kjentytek303.untransfur.block.MSCInputBusBlock.FACING;


public class MSCInputBusBlockEntity extends AbstractMSCBusBlockEntity implements WorldlyContainer {

	public MSCInputBusBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(InitBlockEntities.MSC_INPUT_BUS_BLOCK_ENTITY.get(), pPos, pBlockState);
	}

	@Override
	protected @NotNull Component getDefaultName() {
		return Component.translatable("block.untransfur.msc_input_bus");
	}

	@Override
	public boolean canPlaceItemThroughFace(int pIndex, @NotNull ItemStack pItemStack, @Nullable Direction pDirection) {
		if( pIndex >= AMOUNT_OF_SLOTS ) {
			return false;
		}

		return pDirection == this.getBlockState().getValue(FACING);
	}

	@Override
	public boolean canTakeItemThroughFace(int pIndex, @NotNull ItemStack pStack, @NotNull Direction pDirection) {
		return false;
	}

	@Override
	public void tick(Level level, BlockPos pos, BlockState state) {

	}
}
