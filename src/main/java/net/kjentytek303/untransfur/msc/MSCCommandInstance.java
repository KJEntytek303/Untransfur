package net.kjentytek303.untransfur.msc;

import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.kjentytek303.untransfur.init.InitMSCCommands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;
import java.util.function.Predicate;


@ParametersAreNonnullByDefault
public class MSCCommandInstance implements Function<MSCControllerBlockEntity, Boolean>, Predicate<MSCControllerBlockEntity> {
	public final MSCScheduledCommand command;
	public final ItemStack argument;
	public int ticks_elapsed = 0;
	public boolean should_count_final = false;
	public int additional_delay = 20;

	//For MSCController
	public static MSCCommandInstance fromCompound(CompoundTag tag) {
		String str = "";
		ItemStack stack = ItemStack.EMPTY;
		if (tag.contains("argument")) {
			stack.deserializeNBT(tag.getCompound("argument"));
		}
		if (tag.contains("program")) {
			str = tag.getString("program");
		}
		MSCCommandInstance ret = InitMSCCommands.findByStr(str).asInstance(stack);
		if( tag.contains("ticks_elapsed")) {
			ret.ticks_elapsed = tag.getInt("ticks_elapsed");
		}
		if (tag.contains("additional_delay")) {
			ret.additional_delay = tag.getInt("additional_delay");
		}
		if (tag.contains("should_count_final") ) {
			ret.should_count_final = tag.getBoolean("should_count_final");
		}
		return ret;
	}

	/**
	 * Internal use only, use MSCScheduledCommand this.asInstance(ItemStack argument) to construct
	 * Inheriting is fine.
	 */
	protected MSCCommandInstance(MSCScheduledCommand command, ItemStack argument) {
		this.command = command;
		this.argument = argument;
	}

	@Override
	public Boolean apply(MSCControllerBlockEntity msc) {
		ticks_elapsed++;
		if(!should_count_final && command.apply(msc, argument)) {
			return true;
		}
		should_count_final = true;
		return additional_delay-- == 0;
	}


	@Override
	public boolean test(MSCControllerBlockEntity mscControllerBlockEntity) {
		return this.command.test(mscControllerBlockEntity);
	}

	public CompoundTag getCompound() {
		CompoundTag tag = new CompoundTag();
		tag.putString("program", this.command.command_id.toString());
		tag.put("argument", argument.serializeNBT());
		tag.putInt("ticks_elapsed", this.ticks_elapsed);
		tag.putInt("additional_delay", this.additional_delay);
		tag.putBoolean("should_count_final", this.should_count_final);
		return tag;
	}
}
