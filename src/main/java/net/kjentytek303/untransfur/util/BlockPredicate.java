package net.kjentytek303.untransfur.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;


public class BlockPredicate implements Predicate<BlockState> {
	public Block required_block;
	public TagKey<Block> accepted_tag;

	public BlockPredicate(Block obj) {
		this.required_block = obj;
		accepted_tag = null;
	}

	public BlockPredicate(TagKey<Block> tag) {
		accepted_tag = tag;
		required_block = null;
	}

	@Override
	public boolean test(BlockState blockState) {
		return  ( required_block == null && accepted_tag == null ) ||
			( accepted_tag != null && blockState.is(accepted_tag) ) ||
			( required_block != null && blockState.getBlock().equals(required_block))
		;
	}
}
