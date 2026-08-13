package net.kjentytek303.untransfur.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import static net.kjentytek303.untransfur.block.MSCInputBusBlock.FACING;
import static net.kjentytek303.untransfur.init.InitBlockEntities.MSC_OUTPUT_BUS_BLOCK_ENTITY;
import static net.minecraft.world.level.block.entity.HopperBlockEntity.getContainerAt;


public class MSCOutputBusBlockEntity extends AbstractMSCBusBlockEntity implements WorldlyContainer {

	private int eject_cooldown = 0;

	public ContainerData data = new ContainerData() {
		@Override
		public int get(int pIndex) {
			return switch (pIndex) {
				case 0 -> MSCOutputBusBlockEntity.this.eject_cooldown;
				default -> 0;
			};
		}

		@Override
		public void set(int pIndex, int pValue) {
			switch( pIndex ) {
				case 0 -> MSCOutputBusBlockEntity.this.eject_cooldown = pValue;
			}
		}
		@Override
		public int getCount() {
			return 1;
		}
	};

	public MSCOutputBusBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(MSC_OUTPUT_BUS_BLOCK_ENTITY.get(), pPos, pBlockState);
	}

	@Override
	public void tick(Level level, BlockPos pos, BlockState state) {
		if( level.isClientSide ){
			return;
		}

		if( eject_cooldown > 0 ) {
			eject_cooldown--;
			setChanged();
			return;
		}

		tryEjectItems();
		data.set(0, 20);
		setChanged();
	}

	public boolean tryEjectItems() {
		if( getLevel() != null && getLevel().isClientSide() ) {
			return false;
		}

		Direction direction = getBlockState().getValue(FACING);
		Container attached_container = getContainerAt(getLevel(), getBlockPos().relative(direction));

		if( attached_container == null ) {
			return false;
		}

		ItemStack stack;
		for( int my_slot=0; my_slot<items.size(); my_slot++ ) {
			//assign and check
			if( (stack = items.get(my_slot)).isEmpty() ) {
				continue;
			}

			for( int i=0; i<attached_container.getContainerSize(); i++ ) {
				if( attached_container instanceof WorldlyContainer world_container &&
					world_container.canPlaceItemThroughFace(i, stack.copyWithCount(1), direction.getOpposite()) ) {

					this.data.set(0, 40);
					if (canMergeItems(this.items.get(my_slot).copyWithCount(1), world_container.getItem(i))) {
						this.items.get(my_slot).shrink(1);
						world_container.getItem(i).grow(1);

					}
					else {
						world_container.setItem(i, this.removeItem(my_slot, 1));
					}
					this.setChanged();
					return true;
				} else if( attached_container.canPlaceItem(i, stack.copyWithCount(1)) ) {
					this.data.set(0, 40);
					if (canMergeItems(this.items.get(my_slot).copyWithCount(1), attached_container.getItem(i))) {
						this.items.get(my_slot).shrink(1);
						attached_container.getItem(i).grow(1);
					}
					else {
						attached_container.setItem(i, this.removeItem(my_slot, 1));
					}
					this.setChanged();
					return true;
				}

			}
		}

		return false;
	}

	public static boolean canMergeItems(ItemStack stack1, ItemStack stack2) {
		return stack1.getCount() <= stack1.getMaxStackSize() && ItemStack.isSameItemSameTags(stack1, stack2);
	}

	@Override
	public boolean canPlaceItemThroughFace(int pIndex, ItemStack pItemStack, @Nullable Direction pDirection) {
		return false;
	}

	@Override
	public boolean canTakeItemThroughFace(int pIndex, ItemStack pStack, Direction pDirection) {
		if( pIndex >= AMOUNT_OF_SLOTS ) {
			return false;
		}

		return pDirection == this.getBlockState().getValue(FACING);
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("block.untransfur.msc_output_bus");
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		eject_cooldown = tag.getInt("eject_cooldown");
	}

	@Override
	public void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putInt("eject_cooldown", eject_cooldown);
	}


}
