package net.kjentytek303.untransfur.block;

import com.mojang.datafixers.util.Either;
import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.ltxprogrammer.changed.block.StasisChamber;
import net.ltxprogrammer.changed.block.ThreeXThreeSection;
import net.ltxprogrammer.changed.block.entity.StasisChamberBlockEntity;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class MSCControllerBlock extends StasisChamber {

	public static final VoxelShape SHAPE = Block.box( 0,0,0, 16,16,16);
	protected final RegistryObject<SoundEvent> open;
	protected final RegistryObject<SoundEvent> close;

	public MSCControllerBlock(Properties properties) {
		super(ChangedSounds.STASIS_CHAMBER_DOOR_OPEN, ChangedSounds.STASIS_CHAMBER_DOOR_CLOSE);
		this.registerDefaultState( this.stateDefinition.any().setValue(SECTION, ThreeXThreeSection.CENTER ).setValue(ACTIVE, true).setValue(FACING, Direction.NORTH));
		open = ChangedSounds.STASIS_CHAMBER_DOOR_OPEN;
		close = ChangedSounds.STASIS_CHAMBER_DOOR_CLOSE;
	}

	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new MSCControllerBlockEntity(pos, state);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos, Either<Boolean, Direction> allCheckOrDir) {
		return true;
	}

	@Override
	public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public @NotNull VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
		return SHAPE;
	}

	@Override
	public @NotNull VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
		return SHAPE;
	}

	@Override
	public @NotNull VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

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

}
