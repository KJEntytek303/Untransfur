package net.kjentytek303.untransfur.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.function.Predicate;


public class ItemPredicate {

	public ItemStack required_item;
	public TagKey<Item> accepted_tag;

	public ItemPredicate(ItemStack obj) {
		this.required_item = obj;
		accepted_tag = null;
	}

	public ItemPredicate(TagKey<Item> tag) {
		accepted_tag = tag;
		required_item = null;
	}

	public boolean test(ItemStack stack, boolean respectTags) {
		return  ( required_item == null && accepted_tag == null ) ||
			( accepted_tag != null && stack.is(accepted_tag) ) ||
			( required_item != null && stack.equals(required_item, respectTags))
			;
	}
}
