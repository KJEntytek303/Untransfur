package net.kjentytek303.untransfur.block_entity;

import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.client.menu.MSCRedstoneLogicAdapterMenu;
import net.kjentytek303.untransfur.init.InitBlockEntities;
import net.kjentytek303.untransfur.init.InitItems;
import net.kjentytek303.untransfur.init.InitMSCCommands;
import net.kjentytek303.untransfur.msc.IMSCAugment;
import net.kjentytek303.untransfur.msc.MSCCommandInstance;
import net.kjentytek303.untransfur.msc.MSCScheduledCommand;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

public class MSCRedstoneLogicAdapterBlockEntity extends BaseContainerBlockEntity implements MenuProvider, WorldlyContainer, IMSCAugment {

	public static final int AMOUNT_OF_SLOTS = 1;
	public NonNullList<ItemStack> items = NonNullList.withSize(AMOUNT_OF_SLOTS, ItemStack.EMPTY);
	public static final int[] SLOTS = IntStream.range(0, AMOUNT_OF_SLOTS).toArray();
	public MSCControllerBlockEntity controller = null;

	public MSCRedstoneLogicAdapterBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(InitBlockEntities.MSC_REDSTONE_LOGIC_ADAPTER_BE.get(), pPos, pBlockState);
	}
	@Override
	public Component getDefaultName() {
		return Component.translatable("block.untransfur.msc_redstone_logic_adapter");
	}
	@Override
	public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
		return new MSCRedstoneLogicAdapterMenu(pContainerId, pInventory, this);
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
		return pIndex < AMOUNT_OF_SLOTS && pItemStack.is(InitItems.MSC_PROGRAM_ROM.get());
	}

	@Override
	public boolean canTakeItemThroughFace(int pIndex, @NotNull ItemStack pStack, @NotNull Direction pDirection) {
		return pIndex < AMOUNT_OF_SLOTS;
	}

	@Override
	public int @NotNull [] getSlotsForFace(@NotNull Direction pSide) {
		return SLOTS;
	}

	public void tick(Level level, BlockPos pos, BlockState state) {}

	@Override
	public void addController(MSCControllerBlockEntity ctrl) {
		this.controller = ctrl;
	}

	public void msc_tick(MSCControllerBlockEntity msc ) { }

	public int getSignal() {
		//if no controller or controller not running - null
		if( this.controller == null || this.controller.current_command == null ) {
			return 0;
		}
		ItemStack rom = this.items.get(0);
		MSCScheduledCommand rom_program = InitMSCCommands.EMPTY.get();

		if( rom.equals(ItemStack.EMPTY)  ) { // no ROM? check if running at all.
			return 15;
		}

		if( rom.getTag() != null && rom.getTag().contains("program")) { //get MSCCommand from ROM //TODO maybe move this to a function?? ~KJEntytek303
			rom_program = InitMSCCommands.findByStr(rom.getTag().getString("program"));
		}

		if( rom_program == this.controller.current_command.command) { //if programs match, 15
			return 15;
		}
		//else null
		return 0;
	}


	public void addProgram() {
		ItemStack rom = this.items.get(0);
		if( !rom.is(InitItems.MSC_PROGRAM_ROM.get()) || rom.getTag() == null || !rom.getTag().contains("program") ) {
			return;
		}
		if( this.controller == null ) {
			Untransfur.LOGGER.debug("Null controller at {}", this.getBlockPos());
			return;
		}
		controller.inputProgram( InitMSCCommands.findByStr(rom.getTag().getString("program")).asInstance( ItemStack.EMPTY));
	}
}