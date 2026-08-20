package net.kjentytek303.untransfur.block;

import net.kjentytek303.untransfur.config.ServerCfg;
import net.kjentytek303.untransfur.init.InitMobEffects;
import net.kjentytek303.untransfur.util.ProcessUntransfur;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.process.TransfurEvents;
import net.ltxprogrammer.changed.util.LevelUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.MinecraftForge;

import static net.kjentytek303.untransfur.util.ProcessUntransfur.ProcUntfRetVal.UNTRANSFUR;


public class DroppedUntransfurSyringe extends Block implements SimpleWaterloggedBlock {

	public DroppedUntransfurSyringe(Properties pProperties) {
		super(pProperties);
	}

	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final VoxelShape SHAPE = Block.box( 3.0, 0.0, 3.0, 13.0, 2.0, 13.0);
	public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;

	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	public void neighborChanged( BlockState state, Level level, BlockPos pos, Block source, BlockPos source_pos, boolean moved_by_piston ) {
		super.neighborChanged( state, level, pos, source, source_pos, moved_by_piston);
		if( state.canSurvive(level, pos)) {
			return;
		}
		//TODO: Check if it will drop
		dropResources(state, level, pos);
		level.removeBlock(pos, false);
	}

	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if( level.isClientSide || !(entity instanceof LivingEntity living_entity)) {
			return;
		}

		if(! LevelUtil.isTouchingBlockCollision(level, pos, state, living_entity )) {
			return;
		}

		if( !( living_entity instanceof Player player )) {
			living_entity.addEffect(new MobEffectInstance(InitMobEffects.UNSAFE_UNTRANSFUR.get(), 10, 0));
			level.removeBlock(pos, false);
			return;
		}

		if( player.isCreative() || player.isSpectator() ) {
			return;
		}

		switch(ServerCfg.UNTRANSFUR_HANDLE_MODE.get() ) {
			case SIMPLE -> {
				ProcessUntransfur.incrementPlayerUntransfurProgress(player, 0.15);
			}
			case ORGANICS_ONLY -> {
				if (ProcessTransfur.isPlayerNotLatex(player) ) {
					if(ProcessUntransfur.incrementPlayerUntransfurProgress(player, 0.15) == UNTRANSFUR) {
						var event = new TransfurEvents.UntransfurPlayerByBlockEvent(state, pos, player, ProcessTransfur.getPlayerTransfurVariant(player), null );
						if(!MinecraftForge.EVENT_BUS.post(event)) {
							TransfurEvents.finalizeUntransfurPlayerEvent(event);
						}
					}

				} else {
					player.addEffect(new MobEffectInstance(InitMobEffects.UNSAFE_UNTRANSFUR.get(), 10, 0));
				}
			}

			case COMPLEX -> {
				if (ProcessTransfur.isPlayerNotLatex(player) ) {
					player.addEffect(new MobEffectInstance(InitMobEffects.UNSAFE_UNTRANSFUR.get(), 10, 0));
				} else {
					//TODO: Flinston Solution
				}
			}
		}
		level.removeBlock(pos, false);
	}

	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos ) {
		return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
	}

	public VoxelShape getOcclusionShape( BlockState state, BlockGetter level, BlockPos pos ) {
		return Shapes.empty();
	}

	public PushReaction getPistonPushReaction( BlockState state ) {
		return PushReaction.DESTROY;
	}

	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder ) {
		builder.add(ROTATION, WATERLOGGED);
	}

	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(ROTATION, Mth.floor((context.getRotation() * 16.0 / 360.0) + 0.5) & 15).setValue(WATERLOGGED, false);
	}

	public BlockState rotate(BlockState state, Rotation rotation) {
		return (BlockState)state.setValue(ROTATION, rotation.rotate((Integer)state.getValue(ROTATION), 16));
	}

	public BlockState mirror(BlockState state, Mirror mirror) {
		return (BlockState)state.setValue(ROTATION, mirror.mirror((Integer)state.getValue(ROTATION), 16));
	}

	public FluidState getFluidState(BlockState state) {
		return (Boolean)state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	public BlockState updateShape(BlockState state, Direction direction, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos) {
		if ((Boolean)state.getValue(WATERLOGGED)) {
			level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return super.updateShape(state, direction, otherState, level, pos, otherPos);
	}
}
