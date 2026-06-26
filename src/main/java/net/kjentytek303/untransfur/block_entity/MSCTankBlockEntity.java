package net.kjentytek303.untransfur.block_entity;
/*
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.LavaFluid;
import net.minecraft.world.level.material.WaterFluid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.kjentytek303.untransfur.init.InitBlockEntities.MSC_TANK_BLOCK_ENTITY;
*/
/*
public class MSCTankBlockEntity extends BlockEntity implements MenuProvider/*, IFluidHandler*/ /*{
	/*

	public MSCTankBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super( MSC_TANK_BLOCK_ENTITY.get(), pPos, pBlockState);
	}

	protected ItemStackHandler item_handler = new ItemStackHandler(2);
	protected boolean is_multiblock_valid = false;
	//protected FluidStack fluid_stack = new FluidStack(Fluids.EMPTY, 0 );
	protected LazyOptional<IItemHandler> lazy_item_handler = LazyOptional.empty();
	//protected LazyOptional<IFluidHandler> lazy_fluid_handler = LazyOptional.empty();
	//protected static final FluidStack water = new FluidStack(Fluids.WATER, 1);

	public static final int INPUT_SLOT = 0;
	public static final int OUTPUT_SLOT = 1;

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
		if (capability == ForgeCapabilities.ITEM_HANDLER) {
			return lazy_item_handler.cast();
		}
		/*
		if (capability == ForgeCapabilities.FLUID_HANDLER) {
			return lazy_fluid_handler.cast();
		}
		 */
	/*
		return super.getCapability( capability, side);
	}

	@Override
	public void onLoad() {
		super.onLoad();
		lazy_item_handler = LazyOptional.of( ()-> item_handler);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		lazy_item_handler.invalidate();
	}
	@Override
	public Component getDisplayName() {
		return Component.translatable("block.untransfur.msc_tank");
	}
	@Override
	public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
		return
	}

	/*
	@Override
	public int getTanks() {
		return 1;
	}
	 */


	/*
	@Override
	public @NotNull FluidStack getFluidInTank(int tank) {
		return this.fluid_stack.copy();
	}
	*/

	/*
	@Override
	public int getTankCapacity(int tank) {
		return 16 * 1000;
	}
	*/

	/*
	@Override
	public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
		return stack.isFluidEqual(water);
	}
	*/

	/*
	@Override
	public int fill(FluidStack resource, FluidAction action) {
		if ( isFluidValid(0, resource) && fluid_stack.getAmount() < getTankCapacity(0) ) { return 0; }

		int to_be_added = Math.min( getTankCapacity(0) - fluid_stack.getAmount(), 2000 );
		if ( action.execute() ) {
			fluid_stack.grow(to_be_added);
			setChanged();
		}
		return to_be_added;
	}
	*/

	/*
	@Override
	public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
		return null;
	}
	*/

	/*
	@Override
	public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
		return null;
		da
	}*/
/*
	public void drops() {
		SimpleContainer inv = new SimpleContainer(item_handler.getSlots());
		for( int i=0; i<item_handler.getSlots(); i++) {
			inv.setItem(i, item_handler.getStackInSlot(i));
		}
		Containers.dropContents(this.level, this.worldPosition, inv);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		item_handler.deserializeNBT( tag.getCompound("untf:msc_tank_inventory"));
	}

	@Override
	public void saveAdditional(CompoundTag tag){
		super.saveAdditional(tag);
		tag.put("untf:msc_tank_inventory", item_handler.serializeNBT());
	}

	public void tick(Level level, BlockPos bpos, BlockState bstate) {
		//check if multiblock is valid
	}
}

 */
