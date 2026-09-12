package net.kjentytek303.untransfur.util;

import net.kjentytek303.untransfur.Untransfur;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;


public class UntfTags {
	public static class Blocks {
		public static final TagKey<Block> MSC_AUGMENT_BLOCKS = BlockTags.create(Untransfur.modResource("msc_augment"));
		public static final TagKey<Block> MSC_INPUT = BlockTags.create(Untransfur.modResource("msc_input"));
		public static final TagKey<Block> MSC_OUTPUT = BlockTags.create(Untransfur.modResource("msc_output"));
	}

	public static class Items {
		public static final TagKey<Item> MSC_COMPATIBLE_ITEMS = ItemTags.create(Untransfur.modResource("msc_input_item"));
	}
}
