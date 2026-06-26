package net.kjentytek303.untransfur.block_entity;

import net.kjentytek303.untransfur.util.BlockUtilities;
import net.kjentytek303.untransfur.util.List3Wrapper;
import net.kjentytek303.untransfur.util.UntfTags;
import net.ltxprogrammer.changed.block.ChangedBlock;
import net.ltxprogrammer.changed.block.entity.StasisChamberBlockEntity;
import net.ltxprogrammer.changed.init.ChangedBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static net.kjentytek303.untransfur.block.MSCControllerBlock.FACING;
import static net.kjentytek303.untransfur.init.InitBlocks.MSC_CONTROLLER;
import static net.kjentytek303.untransfur.util.BlockUtilities.TransformHorizontalDirection;
import static net.kjentytek303.untransfur.util.BlockUtilities.fillWithBlock;
import static net.kjentytek303.untransfur.util.BlockUtilities.isBlock;
import static net.minecraft.world.level.block.Blocks.AIR;
import static net.minecraft.world.level.block.Blocks.DISPENSER;
import static net.minecraft.world.level.block.Blocks.REDSTONE_BLOCK;
import static net.minecraft.world.level.block.Blocks.SMOOTH_STONE;
import static net.minecraft.world.level.block.Blocks.SMOOTH_STONE_SLAB;
import static net.minecraftforge.common.Tags.Blocks.GLASS;


public class MSCControllerBlockEntity extends StasisChamberBlockEntity {


	public MSCControllerBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	public boolean checkMultiblock(Level level, BlockPos msc_controller_pos, Player player) {
		BlockState msc_controller = level.getBlockState(msc_controller_pos);
		if( !msc_controller.is(MSC_CONTROLLER.get()) ) {
			return false;
		}
		Direction msc_direction = msc_controller.getValue(FACING);
		BlockPos multiblock_root = BlockUtilities.TransformHorizontalDirection(msc_controller_pos, msc_direction, -2, 0, -5);
		BlockPos.MutableBlockPos iterator = new BlockPos.MutableBlockPos( multiblock_root.getX(), multiblock_root.getY(), multiblock_root.getZ());

		//Check Block-by-block
		for( int x=0; x<5; x++) {
			for ( int y=0; y<10; y++) {
				for (int z=0; z<6; z++) {
					var t = MSC_MULTIBLOCK_DEFINITION.get(x, y, z);
					if ( ! ( t.test(level.getBlockState(iterator)))
					) {
						player.sendSystemMessage(Component.literal("Error: Invalid block " + level.getBlockState(iterator) + " at " + iterator.toString()));

						return false;
					}
					iterator = TransformHorizontalDirection(iterator, msc_direction, 0, 0, 1);
				}
				iterator = TransformHorizontalDirection(iterator, msc_direction,0, 1, -6);
			}
			iterator = TransformHorizontalDirection(iterator, msc_direction, 1, -10, 0);
		}
		player.sendSystemMessage(Component.literal("Multiblock formed successfully"));
		return true;
	}
	public static final List3Wrapper<Predicate<BlockState>> MSC_MULTIBLOCK_DEFINITION;

	static {
		MSC_MULTIBLOCK_DEFINITION = new List3Wrapper<>(5, 10, 6);
		for(int i=0; i<5*10*6; i++) {
			MSC_MULTIBLOCK_DEFINITION.list.add(isBlock(ChangedBlocks.WALL_GRAY.get()));
		}

		//Bottom layer
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 0, 0, 1, 4, 0, 5, BlockUtilities.isOfTag(UntfTags.Blocks.MSC_AUGMENT_BLOCKS) );
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 1, 0, 2, 3, 0, 4, BlockUtilities.isBlock(SMOOTH_STONE_SLAB) );
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 1, 0, 0, 3, 0, 0, BlockUtilities.isBlock(SMOOTH_STONE));
		MSC_MULTIBLOCK_DEFINITION.set( 2, 0, 3, BlockUtilities.isBlock(DISPENSER));
		MSC_MULTIBLOCK_DEFINITION.set( 2, 0, 5, BlockUtilities.isBlock(MSC_CONTROLLER.get()));

		//Backplate
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 1, 1, 0, 3, 2, 0, BlockUtilities.isBlock(ChangedBlocks.OXYGENATED_WATER_CANISTER.get()));
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 1, 3, 0, 3, 4, 0, BlockUtilities.isBlock(SMOOTH_STONE));
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 0, 5, 0, 4, 9, 0, BlockUtilities.any);
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 0, 6, 0, 4, 9, 0, BlockUtilities.any);

		//Front Panel
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 1, 1, 5, 3, 5, 5, BlockUtilities.isOfTag(GLASS));
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 1, 6, 5, 3, 6, 5, BlockUtilities.isOfTag(UntfTags.Blocks.MSC_AUGMENT_BLOCKS));

		//Corner pillars
		  //back
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 0, 0, 0, 0, 4, 0, BlockUtilities.isBlock(ChangedBlocks.WALL_VENT.get()));
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 4, 0, 0, 4, 4, 0, BlockUtilities.isBlock(ChangedBlocks.WALL_VENT.get()));
		  //back-up
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 0, 4, 1, 0, 8, 1, BlockUtilities.isBlock(ChangedBlocks.WALL_VENT.get()));
		fillWithBlock(MSC_MULTIBLOCK_DEFINITION, 4, 4, 1, 4, 8, 1, BlockUtilities.isBlock(ChangedBlocks.WALL_VENT.get()));
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
	}
}
