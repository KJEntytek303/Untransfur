package net.kjentytek303.untransfur.client.screen;

import net.kjentytek303.untransfur.block_entity.MSCInputBusBlockEntity;
import net.kjentytek303.untransfur.init.InitBlocks;
import net.kjentytek303.untransfur.init.InitMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;


public class MSCInputBusMenu extends AbstractContainerMenu {
	private final MSCInputBusBlockEntity bentity;
	private final Level level;

	public MSCInputBusMenu(int container_id, Inventory inv, FriendlyByteBuf extra) {
		this(container_id, inv, inv.player.level().getBlockEntity(extra.readBlockPos()));
	}

	public MSCInputBusMenu( int container_id, Inventory inv, BlockEntity bentity) {
		super(InitMenus.MSC_INPUT_BUS_MENU.get(), container_id);
		this.bentity = (MSCInputBusBlockEntity) bentity;
		this.level = inv.player.level();
		checkContainerSize(inv, 4);
		addPlayerInventory(inv);
		addPlayerHotbar(inv);

		this.bentity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent( item_handler -> {
			this.addSlot(new SlotItemHandler( item_handler, 0, 71, 8));
			this.addSlot(new SlotItemHandler( item_handler, 1, 89, 8));
			this.addSlot(new SlotItemHandler( item_handler, 2, 71, 26));
			this.addSlot(new SlotItemHandler( item_handler, 3, 89, 26));
		});
	}

	// CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
	// must assign a slot number to each of the slots used by the GUI.
	// For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
	// Each time we add a Slot to the container, it automatically increases the slotIndex, which means
	//  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
	//  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
	//  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
	private static final int HOTBAR_SLOT_COUNT = 9;
	private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
	private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
	private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
	private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
	private static final int VANILLA_FIRST_SLOT_INDEX = 0;
	private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

	// THIS YOU HAVE TO DEFINE!
	private static final int TE_INVENTORY_SLOT_COUNT = 4;  // must be the number of slots you have!
	@Override
	public ItemStack quickMoveStack(Player playerIn, int pIndex) {
		Slot sourceSlot = slots.get(pIndex);
		if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
		ItemStack sourceStack = sourceSlot.getItem();
		ItemStack copyOfSourceStack = sourceStack.copy();

		// Check if the slot clicked is one of the vanilla container slots
		if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
			// This is a vanilla container slot so merge the stack into the tile inventory
			if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
				+ TE_INVENTORY_SLOT_COUNT, false)) {
				return ItemStack.EMPTY;  // EMPTY_ITEM
			}
		} else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
			// This is a TE slot so merge the stack into the players inventory
			if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
				return ItemStack.EMPTY;
			}
		} else {
			System.out.println("Invalid slotIndex:" + pIndex);
			return ItemStack.EMPTY;
		}
		// If stack size == 0 (the entire stack was moved) set slot contents to null
		if (sourceStack.getCount() == 0) {
			sourceSlot.set(ItemStack.EMPTY);
		} else {
			sourceSlot.setChanged();
		}
		sourceSlot.onTake(playerIn, sourceStack);
		return copyOfSourceStack;
	}


	@Override
	public boolean stillValid(Player pPlayer) {
		return stillValid(ContainerLevelAccess.create(level, bentity.getBlockPos()), pPlayer, InitBlocks.MSC_INPUT_BUS.get());
	}

	private void addPlayerInventory( Inventory inv ) {
		for( int y=0; y<3; y++) {
			for(int x=0; x<9; x++) {
				this.addSlot(new Slot(inv, x + y*9 + 9, 8 + x*18, 63 + y*18));
			}
		}
	}

	private void addPlayerHotbar( Inventory inv ) {
		for( int x=0; x<9; x++) {
			this.addSlot(new Slot(inv, x, 8 + x*18, 121));
		}
	}
}
