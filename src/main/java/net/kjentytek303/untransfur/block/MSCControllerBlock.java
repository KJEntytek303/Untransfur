package net.kjentytek303.untransfur.block;

import com.mojang.datafixers.util.Either;
import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.kjentytek303.untransfur.init.InitBlockEntities;
import net.kjentytek303.untransfur.util.BlockUtilities;
import net.ltxprogrammer.changed.block.StasisChamber;
import net.ltxprogrammer.changed.block.ThreeXThreeSection;
import net.ltxprogrammer.changed.block.entity.StasisChamberBlockEntity;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.kjentytek303.untransfur.init.InitBlocks.MSC_CONTROLLER;


public class MSCControllerBlock extends BaseEntityBlock {
	public MSCControllerBlock ( Properties properties ) {
		super(properties);
		this.registerDefaultState( this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OPEN, false).setValue(ACTIVE, false));
	}
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(new Property[]{FACING, OPEN, ACTIVE});
	}

	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return (BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return (BlockState)state.setValue(FACING, rot.rotate((Direction)state.getValue(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation((Direction)state.getValue(FACING)));
	}
	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if (pLevel.isClientSide() ) {
			return InteractionResult.SUCCESS;
		}
		BlockEntity be = pLevel.getBlockEntity(pPos);
		if( be instanceof MSCControllerBlockEntity msc ) {
			msc.checkMultiblock(pLevel, pPos, pPlayer);
		}

		return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
	}
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

	@Override
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}
	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new MSCControllerBlockEntity( pPos, pState );
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
		return super.getTicker(pLevel, pState, pBlockEntityType);
	}
	@Override
	public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {

		super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
	}

	@Override
	public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
		return super.rotate(state, level, pos, direction);
	}
	/**
	 * Called when a tile entity on a side of this block changes is created or is destroyed.
	 *
	 * @param state
	 * @param level    The level
	 * @param pos      Block position in level
	 * @param neighbor Block position of neighbor
	 */
	@Override
	public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
		super.onNeighborChange(state, level, pos, neighbor);
	}
	/**
	 * Returns the reaction of the block when pushed or pulled by a piston. This method should be not called directly, instead via {@link BlockState#getPistonPushReaction()}.
	 * <ul>
	 *     <li>NORMAL: is pushable and pullable by sticky pistons</li>
	 *     <li>DESTROY: is being destroyed on pushing and pulling</li>
	 *     <li>BLOCK: is not being able to be moved</li>
	 *     <li>IGNORE: only usable by entities</li>
	 *     <li>PUSH_ONLY: can only be pushed, blocks on trying to be pulled</li>
	 *     <li>{@code null}: use the PistonPushReaction from the BlockBehaviour.Properties passed into the Block Constructor</li>
	 * </ul>
	 *
	 * @param state The state of this block
	 * @return the PushReaction of this state or {@code null} if the one passed into the block properties should be used
	 */
	@Override
	public @Nullable PushReaction getPistonPushReaction(BlockState state) {
		return super.getPistonPushReaction(state);
	}




	/*
	public AABB getDetectionSize(BlockState state, Level level, BlockPos pos) {
		return (new AABB();
	}
	 */

	/*

	public static final VoxelShape SHAPE = Block.box( 0,0,0, 16,16,16);
	protected final RegistryObject<SoundEvent> open;
	protected final RegistryObject<SoundEvent> close;

	public MSCControllerBlock(Properties properties) {
		super(ChangedSounds.STASIS_CHAMBER_DOOR_OPEN, ChangedSounds.STASIS_CHAMBER_DOOR_CLOSE);
		this.registerDefaultState( this.stateDefinition.any().setValue(SECTION, ThreeXThreeSection.CENTER ).setValue(ACTIVE, true).setValue(FACING, Direction.NORTH));
		open = ChangedSounds.STASIS_CHAMBER_DOOR_OPEN;
		close = ChangedSounds.STASIS_CHAMBER_DOOR_CLOSE;
	}


	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos, Either<Boolean, Direction> allCheckOrDir) {
		return true;
	}

/*
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockPos blockpos = context.getClickedPos();
		Level level = context.getLevel();
		Direction direction = context.getHorizontalDirection();

		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public BlockPos getBlockEntityPos(BlockState state, BlockPos pos) { return pos; }

	@Override
	public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherBlockPos) {
		return state;
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack item) { }

	public AABB getDetectionSize(BlockState state, Level level, BlockPos pos) {
		return (new AABB(state.getValue(SECTION).getRelative(pos, state.getValue(FACING), ThreeXThreeSection.CENTER))).inflate(5.0, 5.0F, 5.0);
	}

	@Override
	public boolean openDoor(BlockState state, Level level, BlockPos pos) {
		if (state.getValue(OPEN)) {
			return false;
		} else {
			boolean wantState = true;
			BlockState nBlock = level.getBlockState(pos);
			level.setBlockAndUpdate(pos, nBlock.setValue(OPEN, wantState));
			level.gameEvent(GameEvent.BLOCK_OPEN, pos, GameEvent.Context.of(state));
			level.playSound(null, pos, this.open.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
			return true;
		}
	}
	@Override
	public boolean closeDoor(BlockState state, Level level, BlockPos pos) {
		if (!state.getValue(OPEN)) {
			return false;
		} else {
			boolean wantState = false;
			BlockState nBlock = level.getBlockState(pos);
			level.setBlockAndUpdate(pos, nBlock.setValue(OPEN, wantState));
			level.gameEvent(GameEvent.BLOCK_CLOSE, pos, GameEvent.Context.of(state));
			level.playSound(null, pos, this.close.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
			return true;
		}
	}
	 */

}
