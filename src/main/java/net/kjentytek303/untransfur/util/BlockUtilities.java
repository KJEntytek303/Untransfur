package net.kjentytek303.untransfur.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.Predicate;


public class BlockUtilities {
	/**
	 * Transforms coordinates with an offset.
	 * argument names indicate from negative to positive directions, i.e.
	 * left-right means going further left requires incrementally smaller values (-1, -2, -3 ...),
	 * while going right requires incrementally bigger values (1, 2, 3 ...)
	 * -----------------------------------
	 * In case of multiblock controllers:
	 * To make it work from the PLAYER's perspective, always call this with a mirrored direction.
	 */
	public static BlockPos.MutableBlockPos TransformHorizontalDirection(BlockPos pivot, Direction direction, int left_right, int bottom_top, int backward_forward) {
		switch (direction) {
			case WEST:
				break;
			case EAST:
				backward_forward = -backward_forward;
				left_right = -left_right;
				break;
			case NORTH:
				int tmp = -backward_forward;
				backward_forward = left_right;
				left_right = tmp;
				break;
			case SOUTH:
				int tmp1 = backward_forward;
				backward_forward = -left_right;
				left_right = tmp1;
				break;

			default:
				throw new IllegalArgumentException("Attempted to vertically transform coordinates");
		}
		return new BlockPos.MutableBlockPos(pivot.getX() + backward_forward, pivot.getY() + bottom_top, pivot.getZ() + left_right);
	}

	/**
	 * Fills the array list with predicates.
	 * Useful when performing checks against code-defined array list.
	 * beg is the starting coordinate, from left-botton-back corner.
	 * Warning: Loop iterates as long as the end positions are greater or equal.
	 */
	public static void fillWithBlock(List3Wrapper<Predicate<BlockState>> list_to_fill, int beg_x, int beg_y, int beg_z, int end_x, int end_y, int end_z, Predicate<BlockState> tester ) {
		for (int x = beg_x; x <= end_x; x++) {
			for (int y = beg_y; y <= end_y; y++) {
				for (int z = beg_z; z <= end_z; z++) {
					list_to_fill.set( x, y, z, tester);
				}
			}
		}
	}

	public static Predicate<BlockState> isOfTag(TagKey<Block> tag) {
		return block_state -> block_state.is(tag);
	}

	public static Predicate<BlockState> isBlock(Block block) {
		return block_state -> block_state.getBlock().equals(block);
	}

	public static final Predicate<BlockState> any = bs -> true;
}