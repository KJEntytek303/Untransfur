package net.kjentytek303.untransfur.msc;

import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;


public class MSCCommandInstance implements Function<MSCControllerBlockEntity, Boolean>, Predicate<MSCControllerBlockEntity> {

	public MSCCommandInstance( CompoundTag tag ) {
		String str = "";
		ItemStack stack = ItemStack.EMPTY;
		if(tag.contains("argument")) {
			stack.deserializeNBT(tag.getCompound("argument"));
		}
		if(tag.contains("program") ) {
			str = tag.getString("program");
		}
		this.func = MSCScheduledCommand.getFunction(str);
		this.func_str = str;
		this.predicate = MSCScheduledCommand.getPredicate(str);
		this.argument = stack;
	}

	public MSCCommandInstance( String name ) {
		this(name, ItemStack.EMPTY);
	}

	public MSCCommandInstance( String name, ItemStack stack) {
		this.func = MSCScheduledCommand.getFunction(name);
		this.func_str = name;
		this.predicate = MSCScheduledCommand.getPredicate(name);
		this.argument = stack;
	}

	public BiFunction<MSCControllerBlockEntity, ItemStack, Boolean> func;
	public String func_str;
	public Predicate<MSCControllerBlockEntity> predicate;
	public ItemStack argument;

	@Override
	public Boolean apply(MSCControllerBlockEntity msc) {
		return func.apply(msc, argument);
	}
	@Override
	public boolean test(MSCControllerBlockEntity mscControllerBlockEntity) {
		return predicate.test(mscControllerBlockEntity);
	}

	public CompoundTag getCompound() {
		CompoundTag tag = new CompoundTag();
		tag.putString("program", this.func_str);
		tag.put("argument", argument.serializeNBT());
		return tag;
	}
}
