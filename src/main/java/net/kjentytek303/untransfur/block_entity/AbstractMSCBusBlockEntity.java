package net.kjentytek303.untransfur.block_entity;

import net.kjentytek303.untransfur.client.screen.MSCBusMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

import static net.kjentytek303.untransfur.block.MSCInputBusBlock.FACING;


public abstract class AbstractMSCBusBlockEntity extends BaseContainerBlockEntity implements MenuProvider, WorldlyContainer {

	public static final int AMOUNT_OF_SLOTS = 4;
	public NonNullList<ItemStack> items = NonNullList.withSize(AMOUNT_OF_SLOTS, ItemStack.EMPTY);
	public static final int[] SLOTS = IntStream.range(0, AMOUNT_OF_SLOTS).toArray();

	protected AbstractMSCBusBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
		super(pType, pPos, pBlockState);
	}

	@Override
	protected @NotNull AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pInventory) {
		return new MSCBusMenu(pContainerId, pInventory, this);
	}

	@Override
	public int getContainerSize() {
		return AMOUNT_OF_SLOTS;
	}
	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
		return super.getCapability(cap);
	}
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if( cap == ForgeCapabilities.ITEM_HANDLER && ( side == null || side == this.getBlockState().getValue(FACING) ) ) {
			return super.getCapability(cap, side);
		}
		return LazyOptional.empty().cast();
	}

	@Override
	public boolean isEmpty() {
		for(ItemStack itemstack : this.items) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public @NotNull ItemStack getItem(int pSlot) {
		return this.items.get( pSlot );
	}

	@Override
	public @NotNull ItemStack removeItem(int pSlot, int pAmount) {
		return ContainerHelper.removeItem(items, pSlot, pAmount);
	}

	@Override
	public @NotNull ItemStack removeItemNoUpdate(int pSlot) {
		return ContainerHelper.takeItem(items, pSlot);
	}

	@Override
	public void setItem(int pSlot, @NotNull ItemStack pStack) {
		ItemStack existingItem = this.items.get(pSlot);
		boolean not_empty_and_same_item = !pStack.isEmpty() && ItemStack.isSameItemSameTags(pStack, existingItem);
		this.items.set(pSlot, pStack);
		if (pStack.getCount() > this.getMaxStackSize()) {
			pStack.setCount(this.getMaxStackSize());
		}

		if( pSlot >= 0 && pSlot < AMOUNT_OF_SLOTS && !not_empty_and_same_item) {
			this.setChanged();
		}
	}

	@Override
	public boolean stillValid(@NotNull Player pPlayer) {
		if( this.getLevel() == null || this.getLevel().getBlockEntity(this.getBlockPos()) != this )
			return false;
		return Container.stillValidBlockEntity(this, pPlayer);
	}
	@Override
	public void clearContent() {
		this.items.clear();
	}

	public void load(@NotNull CompoundTag tag) {
		super.load( tag );
		ContainerHelper.loadAllItems(tag, items);
	}

	public void saveAdditional(@NotNull CompoundTag tag) {
		super.saveAdditional(tag);
		ContainerHelper.saveAllItems( tag, items);
	}

	@Override
	public int @NotNull [] getSlotsForFace(@NotNull Direction pSide) {
		return SLOTS;
	}

	public abstract void tick(Level level, BlockPos pos, BlockState state);

}
