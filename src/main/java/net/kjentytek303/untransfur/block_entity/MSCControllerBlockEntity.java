package net.kjentytek303.untransfur.block_entity;

import com.google.common.collect.ImmutableList;
import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.block.MSCControllerBlock;
import net.kjentytek303.untransfur.config.ServerCfg;
import net.kjentytek303.untransfur.init.InitDamageSources;
import net.kjentytek303.untransfur.init.InitItems;
import net.kjentytek303.untransfur.msc.IMSCAugment;
import net.kjentytek303.untransfur.msc.MSCCommandInstance;
import net.kjentytek303.untransfur.msc.MSCScheduledCommand;
import net.kjentytek303.untransfur.util.BlockUtilities;
import net.kjentytek303.untransfur.util.List3Wrapper;
import net.kjentytek303.untransfur.util.UntfTags;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.block.entity.SeatableBlockEntity;
import net.ltxprogrammer.changed.entity.SeatEntity;
import net.ltxprogrammer.changed.entity.animation.StasisAnimationParameters;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedAnimationEvents;
import net.ltxprogrammer.changed.init.ChangedBlocks;
import net.ltxprogrammer.changed.item.Syringe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.WaterFluid;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import static net.kjentytek303.untransfur.block.MSCControllerBlock.FACING;
import static net.kjentytek303.untransfur.block.MSCControllerBlock.OPEN;
import static net.kjentytek303.untransfur.init.InitBlockEntities.MSC_CONTROLLER_BLOCK_ENTITY;
import static net.kjentytek303.untransfur.init.InitBlocks.MSC_CONTROLLER;
import static net.kjentytek303.untransfur.init.InitBlocks.MSC_SMOOTH_WALL;
import static net.kjentytek303.untransfur.util.BlockUtilities.TransformHorizontalDirection;
import static net.kjentytek303.untransfur.util.BlockUtilities.fillWithBlock;
import static net.kjentytek303.untransfur.util.BlockUtilities.isBlock;
import static net.kjentytek303.untransfur.util.UntfTags.Blocks.MSC_AUGMENT_BLOCKS;
import static net.ltxprogrammer.changed.init.ChangedItems.LATEX_SYRINGE;
import static net.minecraft.world.level.block.Blocks.AIR;
import static net.minecraft.world.level.block.Blocks.DISPENSER;
import static net.minecraft.world.level.block.Blocks.REDSTONE_BLOCK;
import static net.minecraft.world.level.block.Blocks.SMOOTH_STONE;
import static net.minecraft.world.level.block.Blocks.SMOOTH_STONE_SLAB;
import static net.minecraftforge.common.Tags.Blocks.GLASS;


//Credit to LTXProgrammer for the original block and code.
//TODO: Allow duplicates of commands, and allow commands to store data.
public class MSCControllerBlockEntity extends BaseContainerBlockEntity implements SeatableBlockEntity, StackedContentsCompatible {
	public SeatEntity entity_holder;

	public float fluid_level = 0.0f;
	public float fluid_level0 = 0.0f;
	public int crashed_ticks=0;

	public List<MSCCommandInstance> scheduled_commands = new ArrayList<>();
	public @Nullable MSCCommandInstance current_command = null;
	public LivingEntity cached_entity;
	public final Map<BlockPos, IMSCAugment> augments = new HashMap<>();
	public Direction msc_direction;

	public final ContainerOpenersCounter openers_counter = new ContainerOpenersCounter() {
		@Override
		protected void onOpen(Level pLevel, BlockPos pPos, BlockState pState) { }
		@Override
		protected void onClose(Level pLevel, BlockPos pPos, BlockState pState) { }
		@Override
		protected void openerCountChanged(Level pLevel, BlockPos pPos, BlockState pState, int pCount, int pOpenCount) { }
		@Override
		protected boolean isOwnContainer(Player player) {
			return false;
		}
	};
	public static final int DACCESS_FLUID_LEVEL = 0;
	public static final int DACCESS_FAILURE_CHANCE = 1;


	public NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

	public int wait_duration = 0;
	public boolean stabilized = false;
	public boolean skip_modify = false;
	public boolean one_time_menu_open = true;
	public boolean multiblock_valid = false;
	public final BlockPos multiblock_root;
	public double failure_chance = 0.0;

	protected final ContainerData data_access = new ContainerData() {
		@Override
		public int get(int pIndex) {
			return switch ( pIndex ) {
				case DACCESS_FLUID_LEVEL -> (int)(MSCControllerBlockEntity.this.fluid_level * 1000);
				case DACCESS_FAILURE_CHANCE -> (int)(MSCControllerBlockEntity.this.failure_chance * 1000);
				default -> 0;
			};
		}

		@Override
		public void set(int pIndex, int pValue) {
			switch ( pIndex ) {
				case DACCESS_FLUID_LEVEL -> MSCControllerBlockEntity.this.fluid_level = ((float)pValue) * 0.001f;
				case DACCESS_FAILURE_CHANCE -> MSCControllerBlockEntity.this.failure_chance = ((float)pValue) * 0.001f;
			}
		}

		@Override
		public int getCount() {
			return 2;
		}
	};

	public @NotNull Component getDefaultName() {
		return Component.translatable("block.untransfur.msc_controller");
	}


	protected AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory ) {
		return null;
	}

	public MSCControllerBlockEntity(BlockPos pos, BlockState state) {
		super( MSC_CONTROLLER_BLOCK_ENTITY.get(), pos, state);
		msc_direction = state.getValue(FACING).getOpposite();
		this.multiblock_root = BlockUtilities.TransformHorizontalDirection(pos, msc_direction, -2, 0, -5);
		if( this.getLevel() != null )
			multiblock_valid = checkMultiblock( this.getLevel(), pos, null);
		else { multiblock_valid = false; }
	}

	public boolean isEmpty() {
		return this.items.get(0).isEmpty();
	}

	public void startOpen(Player player) {
		if ( !this.remove && !player.isSpectator() ) {
			this.openers_counter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState() );
		}
	}

	public void stopOpen(Player player) {
		if (!this.remove && !player.isSpectator()) {
			this.openers_counter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
		}
	}

	public ItemStack getItem( int slot) {
		return this.items.get(slot);
	}

	public @NotNull ItemStack removeItem(int slot, int amount) {
		return ContainerHelper.removeItem(this.items, slot, amount);
	}

	public @NotNull ItemStack removeItemNoUpdate(int slot) {
		return ContainerHelper.takeItem(this.items, slot);
	}

	public void setItem( int slot, ItemStack stack ) {
		boolean non_empty_and_same_stack = !stack.isEmpty() && ItemStack.isSameItemSameTags(stack, this.items.get(0));
		this.items.set(slot, stack);

		if (stack.getCount() > this.getMaxStackSize()) {
			stack.setCount(this.getMaxStackSize());
		}
		if ( slot == 0 && !non_empty_and_same_stack ) {
			this.setChanged();
		}
	}

	public int getContainerSize() {
		return 1;
	}

	public boolean stillValid(Player player) {
		if (this.level.getBlockEntity(this.worldPosition ) != this ) {
			return false;
		}
		return player.distanceToSqr( this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getY() ) <= 64.0;
	}

	public boolean canPlaceItem( int slot, ItemStack stack ) {
		if (slot == 0) {
			return stack.is(UntfTags.Items.MSC_COMPATIBLE_ITEMS);
		}
		return false;
	}

	public void clearContent() {
		this.items.clear();
	}

	public void fillStackedContents( StackedContents contents ) {
		contents.accountStack(items.get(0));
	}

	protected void saveAdditional( CompoundTag tag ) {
		super.saveAdditional(tag);
		tag.putFloat("fluid_level", fluid_level);
		tag.putFloat("fluid_level0", fluid_level0);
		ContainerHelper.saveAllItems(tag, this.items);
		tag.putInt("wait_duration", wait_duration);
		tag.putBoolean("stabilized", stabilized );
		tag.putDouble("failure_chance", failure_chance);

		if(entity_holder != null) {
			tag.putInt("entity_holder_id", entity_holder.getId());
		}

		var command_tag = new ListTag();
		scheduled_commands.forEach( command ->
			command_tag.add(command.getCompound())
		);

		tag.put("commands", command_tag);
		if( current_command != null ) {
			tag.put("current_command", current_command.getCompound());
		}

		tag.putBoolean("multiblock_valid", multiblock_valid);
	}

	public void load(CompoundTag tag) {
		super.load(tag);
		fluid_level = tag.getFloat("fluid_level");
		fluid_level0 = tag.getFloat("fluid_level0");
		failure_chance = tag.getDouble("failure_chance");

		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(tag, this.items);

		wait_duration = tag.getInt("wait_duration");
		stabilized = tag.getBoolean("stabilized");

		if (tag.contains("entity_holder_id") && level != null && level.isClientSide ) {
			Entity entity = level.getEntity(tag.getInt("entity_holder_id"));
			if ( entity instanceof SeatEntity seat ) {
				entity_holder = seat;
			}
		}

		scheduled_commands.clear();
		ListTag command_tag = tag.getList("commands", 10 );
		for( int i=0; i<command_tag.size(); i++ ) {
			scheduled_commands.add( new MSCCommandInstance( command_tag.getCompound(i) ));
		}
		current_command = null;
		if (tag.contains("current_command")) {
			current_command = new MSCCommandInstance(tag.getCompound("current_command"));
		}
		multiblock_valid = tag.getBoolean("multiblock_valid");
	}
	public void markUpdated() {
		this.setChanged();
		this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
	}

	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	public CompoundTag getUpdateTag() {
		return this.saveWithoutMetadata();
	}

	public SeatEntity getEntityHolder() {
		return this.entity_holder;
	}

	public void setEntityHolder( SeatEntity entity ) {
		this.entity_holder = entity;
	}

	public boolean chamberEntity( LivingEntity entity ) {

		BlockPos entity_position = TransformHorizontalDirection(this.getBlockPos(), msc_direction, 0, 1, -2);

		if( entity_holder == null || entity_holder.isRemoved() ) {
			entity_holder = SeatEntity.createFor(entity.level(), this.getBlockState(), entity_position, false, false, false);
			this.markUpdated();
		}

		if( this.getSeatedEntity() != null || entity_holder == null ) {
			return false;
		}

		if( !level.isClientSide && getFluidType().orElse(null) instanceof WaterFluid) {
			entity.startRiding(entity_holder);
			ChangedAnimationEvents.broadcastEntityAnimation(entity, ChangedAnimationEvents.STASIS_IDLE.get(), StasisAnimationParameters.INSTANCE );
		}
		return true;
	}

	public Optional<LivingEntity> getChamberedEntity() {
		if (entity_holder == null || this.getBlockState().getValue(OPEN)) {
			return Optional.empty();
		}
		return Optional.ofNullable(
			entity_holder.getFirstPassenger() instanceof LivingEntity livingEntity ? livingEntity : null
		);
	}

	public Optional<IAbstractChangedEntity> getChamberedLatex() {
		return getChamberedEntity().map(IAbstractChangedEntity::forEither);
	}

	public @Nullable MSCCommandInstance getCurrentCommand() {
		return current_command;
	}

	public @Nullable TransfurVariant<?> findVariantFromSlots() {
		List<BlockPos> input_augments = findAugments( UntfTags.Blocks.MSC_INPUT );
		for( var augment_pos : input_augments ) {
			net.minecraft.world.level.block.entity.BlockEntity augment = level.getBlockEntity(augment_pos);
			if (!(augment instanceof Container container)) {
				continue;
			}
			for(int i=0; i<container.getContainerSize(); i++) {
				ItemStack stack = container.getItem(i);
				if (!stack.is(LATEX_SYRINGE.get())) {
					continue;
				}
				return Syringe.getVariant(stack);
			}
		}
		return null;
	}

	public List<BlockPos> findAugments(TagKey<Block> type) {
		List<BlockPos> ret = new ArrayList<>();
		BlockPos.MutableBlockPos iterator;
		for( BlockPos pos : AUGMENT_POSITIONS) {
			iterator = TransformHorizontalDirection(this.multiblock_root, level.getBlockState(this.getBlockPos()).getValue(FACING), pos);
			if(level.getBlockState(iterator).getBlock().builtInRegistryHolder().containsTag(type) ) {
				ret.add(iterator);
			}
		}
		return ret;
	}

	private ItemStack getSyringe() {
		return items.get(0);
	}

	public ImmutableList<MSCCommandInstance> getCommands() {
		return ImmutableList.copyOf(scheduled_commands);
	}

	public float getFluidYHeight() {	//TODO: might require additional renderer patches.
		return ( fluid_level * 7 );
	}

	public float getFluidLevel(float partialTick) {
		return Mth.lerp(partialTick, fluid_level0, fluid_level);
	}

	public Optional<Fluid> getFluidType() {
		return Optional.of( Fluids.WATER );
	}

	public boolean shouldChamberIdle() {
		return openers_counter.getOpenerCount() > 0;
	}

	public boolean checkForBlowUp() {
		return ServerCfg.MSC_BLOWS_UP.get() && level.getRandom().nextDouble() < failure_chance;
	}

	public static void serverTick(Level level, BlockPos pos, BlockState bstate, MSCControllerBlockEntity bentity) {

		bentity.failure_chance = Math.max(0.0, bentity.failure_chance - ServerCfg.MSC_REGENERATION_AMOUNT.get() );
		if (!bentity.multiblock_valid) {
			return;
		}

		if( ServerCfg.MSC_EXPLOSION_CHECK_RATE.get() != 0 && level.getGameTime() % ServerCfg.MSC_EXPLOSION_CHECK_RATE.get() == 0) {
			if ( bentity.checkForBlowUp() ) {
				bentity.blowUp();
				return;
			}
		}

		if( level.getGameTime() % 20 == 0) {
			if( bentity.multiblock_valid && bentity.checkMultiblock(level, pos, null)) {
				bentity.invalidateMultiblock();
				bentity.failure_chance += 0.03;
				bentity.multiblock_valid = false;
			}
			if(!bentity.multiblock_valid) {
				return;
			}
		}


		bentity.openers_counter.recheckOpeners(level, pos, bstate);
		var commands = bentity.scheduled_commands;

		if(commands.isEmpty() && !bentity.getEntitiesWithin().isEmpty()) {
			commands.add(new MSCCommandInstance ("drain"));
			commands.add(new MSCCommandInstance("release"));
			commands.add(new MSCCommandInstance ( "close_when_empty"));
		}

		while( bentity.current_command == null ) {
			bentity.current_command = commands.get(0);
			commands.remove(0);
			bentity.markUpdated();
			if (bentity.current_command == null && !commands.isEmpty()) {
				continue;
			}
			if (bentity.current_command == null) {
				return;
			}

			var cmd_predicate = MSCScheduledCommand.getPredicate(bentity.current_command.func_str);
			var tick_function = MSCScheduledCommand.getFunction(bentity.current_command.func_str);

			if (cmd_predicate == null || tick_function == null) {
				Untransfur.LOGGER.warn("Assertion failed, detected null predicate or function in MSCScheduledCommands, id {}", bentity.current_command);
				continue;
			}

			if (cmd_predicate.test(bentity)) {
				bentity.current_command = null;
				bentity.markUpdated();
				return;
			}

			if( !tick_function.apply(bentity, ItemStack.EMPTY ) ) { //Command finished
				bentity.markUpdated();
				return;
			}
			Untransfur.LOGGER.error("Assertion failed, reached unreachable section in MSCControllerBlockEntity");
			return;
		}
	}

	public boolean isOpen() {
		return this.getBlockState().getValue(MSCControllerBlock.OPEN);
	}

	public boolean isFilled() {
		return fluid_level >= 1.0f;
	}

	public boolean isDrained() {
		return fluid_level <= 0.0f;
	}

	public boolean hasEntity() {
		return getChamberedEntity().isPresent();
	}

	public <T extends Entity> List<T> getEntitiesWithin(Class<T> clazz) {
		if( !(this.getBlockState().getBlock() instanceof MSCControllerBlock msc) || getLevel() == null ) {
			return List.of();
		}

		AABB detection_box = msc.getDetectionSize(this.getBlockState(), this.getBlockPos());

		var entities = getLevel().getEntitiesOfClass(clazz, detection_box);
		var iterator = entities.iterator();
		while (iterator.hasNext()) {
			var entity = iterator.next();
			AABB entity_box = entity.getBoundingBox();

			if (! (	detection_box.contains(entity_box.minX, entity_box.minY, entity_box.minZ) &&
				detection_box.contains(entity_box.maxX, entity_box.maxY, entity_box.maxZ) )
			) { iterator.remove(); }
		}
		return entities;
	}

	public List<LivingEntity> getEntitiesWithin() {
		return getEntitiesWithin(LivingEntity.class);
	}

	public List<Player> getPlayersWithin() {
		return getEntitiesWithin(Player.class).stream()
			.filter(entity -> entity instanceof Player)
			.map(entity -> (Player)entity)
			.toList();
	}

	public boolean ensureCapturedIsStillInside() {
		var entities = getEntitiesWithin();
		if ( entities.isEmpty() ) {
			return false;
		}

		if ( cached_entity == null  ) {
			cached_entity = entities.get(0);
		}

		if(cached_entity.isDeadOrDying() || !entities.contains(cached_entity) ) {
			return false;
		}
		chamberEntity(cached_entity);
		return true;
	}

	public boolean isPlayerAllowedToConfigure(@Nullable Player controller) {
		if( controller == null ) {
			return true;
		}
		var players = getPlayersWithin();
		return players.isEmpty() || players.contains(controller);
	}

	public boolean isRedstoneAllowedToConfigure() {
		//TODO: Add a redstone circuitry crash feature if the entity is held too long.
		return true;
	}

	public void setWaitDuration(int wait_duration, @Nullable ServerPlayer controller) {
		wait_duration = Mth.clamp(wait_duration, 0, ServerCfg.MSC_MAX_STASIS_DURATION.get());

		if( wait_duration > this.wait_duration && !this.isPlayerAllowedToConfigure(controller)) {
			return;
		}
		this.wait_duration = wait_duration;
		markUpdated();
	}

	public boolean isStabilized() {
		return stabilized;
	}

	public void inputProgram( String program, @Nullable ServerPlayer controller, ItemStack arguments) {
		//TODO: crash feature.
		if(this.scheduled_commands.size() >= ServerCfg.MSC_MAX_COMMAND_SCHEDULE.get()) {
			//this.crash();
			return;
		}
		//if( MSCScheduledCommand.contains(program)) {
			this.scheduled_commands.add(new MSCCommandInstance( program, arguments ));
		//}
	}

	public void openDoor() {
		//TODO: UPDATE MULTIBLOCK STRUCTURE IN WORLD.
		this.getBlockState().setValue(OPEN, true);
	}

	public void closeDoor() {
		//TODO: UPDATE MULTIBLOCK STRUCTURE IN WORLD.
		this.getBlockState().setValue(OPEN, false);
	}

	public Optional<TransfurVariant<?>> useVariantSyringe() {
		//Check if internal inventory has a syringe.
		//If so, use it.
		//Else, check for input buses.
		//If any input bus has a valid syringe, check if the syringe can be put in the output slot.
			//Of any output buses
			//In internal controller slot
			//if not, return null.
		//Store tf variant, pop syringe stack and put away the syringe.
		return Optional.of(null);
	}

	/*
	public boolean isRedstoneAllowedToConfigure() {

	}
	*/














	public boolean checkMultiblock(Level level, BlockPos msc_controller_pos, @Nullable Player player) {
		BlockState msc_controller = level.getBlockState(msc_controller_pos);
		if( !msc_controller.is(MSC_CONTROLLER.get()) ) {
			return false;
		}
		BlockPos.MutableBlockPos iterator = new BlockPos.MutableBlockPos( multiblock_root.getX(), multiblock_root.getY(), multiblock_root.getZ());

		//Check Block-by-block
		for( int x=0; x<5; x++) {
			for ( int y=0; y<10; y++) {
				for (int z=0; z<6; z++) {
					if ( ! ( MSC_MULTIBLOCK_DEFINITION.get(x, y, z).test(level.getBlockState(iterator)))
					) {
						if (player != null ) { //TODO: Move this into component translatable
							player.sendSystemMessage(Component.literal("Error: Invalid block " + level.getBlockState(iterator) + " at " + iterator));
						}
						return false;
					}
					iterator = TransformHorizontalDirection(iterator, msc_direction, 0, 0, 1);
				}
				iterator = TransformHorizontalDirection(iterator, msc_direction,0, 1, -6);
			}
			iterator = TransformHorizontalDirection(iterator, msc_direction, 1, -10, 0);
		}
		if( player != null ) {
			player.sendSystemMessage(Component.literal("Multiblock formed successfully"));
		}
		discoverAugments();
		return true;
	}

	public void discoverAugments() {
		BlockPos.MutableBlockPos iterator;
		for(var position : AUGMENT_POSITIONS) {
			iterator = TransformHorizontalDirection(multiblock_root, msc_direction, position);
			if (level.getBlockState(iterator).is(MSC_AUGMENT_BLOCKS)) {
				if (level.getBlockEntity(iterator) != null && level.getBlockEntity(iterator) instanceof IMSCAugment msc_augment) {
					msc_augment.addController(this);
				}
			}
		}
	}

	public void invalidateMultiblock() {
		augments.forEach((pos, augment) -> augment.invalidateController() );
		scheduled_commands.clear();
		if(getEntityHolder() != null ) {
			getEntityHolder().getPassengers().forEach( entity -> {
				entity.stopRiding();
				entity.hurt(InitDamageSources.MSC_DISCONNECT.source(entity.level().registryAccess()), 30);
			});
		}
	}


	public void blowUp() {
		invalidateMultiblock();
		BlockPos explosion_root = TransformHorizontalDirection(getBlockPos(), getBlockState().getValue(FACING), 0, 1, -2);
		level.explode(null, explosion_root.getX(), explosion_root.getY(), explosion_root.getZ(), 50, true, Level.ExplosionInteraction.BLOCK);
	}

	public static final List<BlockPos> AUGMENT_POSITIONS = new ArrayList<>();
	public static final List3Wrapper<Predicate<BlockState>> MSC_MULTIBLOCK_DEFINITION;
	public static final List3Wrapper<Predicate<BlockState>> MSC_OPEN_MULTIBLOCK_DEFINITION;

	static {
		ImmutableList.Builder<BlockPos> augment_pos_builder = new ImmutableList.Builder<>();
		//LeftRight
		for(int x=0; x<6; x+=5) {
			for( int z=1; z<5; z++) {
				AUGMENT_POSITIONS.add(new BlockPos(x, 0, z));
			}
		}
		//FrontBottom
		for(int x=1; x<4; x+=2) {
			AUGMENT_POSITIONS.add(new BlockPos(x, 0, 5));
		}
		for(int x=1; x<4; x++) {
			for(int y=0; y<7; y+=6) {
				AUGMENT_POSITIONS.add(new BlockPos(x, y, (y/6) * 5));
			}
		}

		MSC_MULTIBLOCK_DEFINITION = new List3Wrapper<>(5, 10, 6);
		for(int i=0; i<5*10*6; i++) {
			MSC_MULTIBLOCK_DEFINITION.list.add(isBlock(MSC_SMOOTH_WALL.get()));
		}

		//Bottom layer
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 0, 0, 1, 4, 0, 5, BlockUtilities.isOfTag(MSC_AUGMENT_BLOCKS) );
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 1, 0, 2, 3, 0, 4, BlockUtilities.isBlock(SMOOTH_STONE_SLAB) );
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 1, 0, 0, 3, 0, 0, BlockUtilities.isBlock(SMOOTH_STONE));
		MSC_MULTIBLOCK_DEFINITION.set( 2, 0, 3, BlockUtilities.isBlock(DISPENSER));
		MSC_MULTIBLOCK_DEFINITION.set( 2, 0, 5, BlockUtilities.isBlock(MSC_CONTROLLER.get()));

		//Backplate
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 1, 1, 0, 3, 2, 0, BlockUtilities.isBlock(ChangedBlocks.OXYGENATED_WATER_CANISTER.get()));
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 1, 3, 0, 3, 3, 0, BlockUtilities.isBlock(SMOOTH_STONE));
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 1, 4, 0, 3, 4,0, BlockUtilities.isBlock(MSC_SMOOTH_WALL.get()));
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 0, 5, 0, 4, 9, 0, BlockUtilities.any);

		//Front Panel
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 1, 1, 5, 3, 5, 5, BlockUtilities.isOfTag(GLASS));
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 1, 6, 5, 3, 6, 5, BlockUtilities.isOfTag(MSC_AUGMENT_BLOCKS));

		//Corner pillars
		  //back
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 0, 0, 0, 0, 4, 0, BlockUtilities.isBlock(ChangedBlocks.WALL_VENT.get()));
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 4, 0, 0, 4, 4, 0, BlockUtilities.isBlock(ChangedBlocks.WALL_VENT.get()));
		  //back-up
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 0, 5, 1, 0, 8, 1, BlockUtilities.isBlock(ChangedBlocks.WALL_VENT.get()));
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 4, 5, 1, 4, 8, 1, BlockUtilities.isBlock(ChangedBlocks.WALL_VENT.get()));
		  //front
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 0, 0, 5, 0, 8, 5, BlockUtilities.isBlock(ChangedBlocks.WALL_VENT.get()));
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 4, 0, 5, 4, 8, 5, BlockUtilities.isBlock(ChangedBlocks.WALL_VENT.get()));

		//Middle Air
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 1, 1, 2, 3, 6, 4, BlockUtilities.isBlock(AIR));

		//topLayers
		  //air & stone_slabs
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 2, 6,2, 2, 6, 4, BlockUtilities.isBlock(SMOOTH_STONE_SLAB));
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 1, 6,3, 3, 6, 3, BlockUtilities.isBlock(SMOOTH_STONE_SLAB));
		MSC_MULTIBLOCK_DEFINITION.set(2, 6, 3, BlockUtilities.isBlock(AIR));
		  //Stone & fans
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 1, 7, 2, 3, 7, 4, BlockUtilities.isBlock(ChangedBlocks.VENT_FAN.get()));
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 2, 7,2, 2, 8, 4, BlockUtilities.isBlock(SMOOTH_STONE));
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 1, 7,3, 3, 7, 3, BlockUtilities.isBlock(SMOOTH_STONE));
		MSC_MULTIBLOCK_DEFINITION.set(2, 7, 3, BlockUtilities.isBlock(DISPENSER));
		  //Stone * redstone
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 1, 8, 2, 1, 8, 4, BlockUtilities.isBlock(REDSTONE_BLOCK) );
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 3, 8, 2, 3, 8, 4, BlockUtilities.isBlock(REDSTONE_BLOCK) );
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 1, 8, 3, 3, 8, 3, BlockUtilities.isBlock(SMOOTH_STONE) );

		//Top
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 0, 9, 1, 4, 9, 5, BlockUtilities.isBlock(ChangedBlocks.TILES_GRAY_STAIRS.get()));
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 0, 9, 2, 4, 9, 4, BlockUtilities.isBlock(ChangedBlocks.WALL_GRAY_STAIRS.get()));
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 1, 9, 1, 3, 9, 5, BlockUtilities.isBlock(ChangedBlocks.WALL_GRAY_STAIRS.get()));
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 1, 9, 2, 3, 9, 4, BlockUtilities.isBlock(ChangedBlocks.WALL_GRAY.get()));

		MSC_OPEN_MULTIBLOCK_DEFINITION = MSC_MULTIBLOCK_DEFINITION.clone();
		fillWithBlock(MSC_OPEN_MULTIBLOCK_DEFINITION, 1, 1, 5, 3, 5, 5, BlockUtilities.isBlock(AIR));
	}
}
