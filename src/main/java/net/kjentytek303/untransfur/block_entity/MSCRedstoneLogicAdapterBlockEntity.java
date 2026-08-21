package net.kjentytek303.untransfur.block_entity;

import net.kjentytek303.untransfur.init.InitItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

public class MSCRedstoneLogicAdapterBlockEntity extends BaseContainerBlockEntity implements MenuProvider, WorldlyContainer {

	public static final int AMOUNT_OF_SLOTS = 1;
	public NonNullList<ItemStack> items = NonNullList.withSize(AMOUNT_OF_SLOTS, ItemStack.EMPTY);
	public static final int[] SLOTS = IntStream.range(0, AMOUNT_OF_SLOTS).toArray();

	protected MSCRedstoneLogicAdapterBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
		super(pType, pPos, pBlockState);
	}
	@Override
	protected Component getDefaultName() {
		return Component.translatable("block.untransfur.msc_redstone_logic_adapter");
	}
	@Override
	protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
		return null;
	}

	@Override
	public int getContainerSize() {
		return AMOUNT_OF_SLOTS;
	}

	@Override
	public boolean isEmpty() { return items.get(0).isEmpty(); }

	@Override
	public ItemStack getItem(int pSlot) { return items.get(0); }

	@Override
	public ItemStack removeItem(int pSlot, int pAmount) {
		return ContainerHelper.removeItem(items, pSlot, pAmount);
	}

	@Override
	public @NotNull ItemStack removeItemNoUpdate(int pSlot) {
		return ContainerHelper.takeItem(items, pSlot);
	}

	@Override
	public boolean canPlaceItem(int slotId, ItemStack stack) {
		return slotId == 0 && stack.is(InitItems.MSC_PROGRAM_ROM.get());
	}

	@Override
	public void setItem(int pSlot, ItemStack pStack) {
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
	/**
	 * Don't rename this method to canInteractWith due to conflicts with Container
	 */
	@Override
	public boolean stillValid(Player pPlayer) {
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
	public boolean canPlaceItemThroughFace(int pIndex, @NotNull ItemStack pItemStack, @Nullable Direction pDirection) {
		return pIndex < AMOUNT_OF_SLOTS;
	}

	@Override
	public boolean canTakeItemThroughFace(int pIndex, @NotNull ItemStack pStack, @NotNull Direction pDirection) {
		return pIndex < AMOUNT_OF_SLOTS;
	}

	@Override
	public int @NotNull [] getSlotsForFace(@NotNull Direction pSide) {
		return SLOTS;
	}
}
