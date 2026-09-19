package net.kjentytek303.untransfur.msc;

import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.kjentytek303.untransfur.init.InitMSCCommands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;
import java.util.function.Predicate;


@ParametersAreNonnullByDefault
public class MSCCommandInstance implements Function<MSCControllerBlockEntity, Boolean>, Predicate<MSCControllerBlockEntity> {
	public final MSCScheduledCommand command;
	public final ItemStack argument;

	public static MSCCommandInstance fromNBTString(String command, ItemStack argument ) {
		return new MSCCommandInstance( InitMSCCommands.findByNBTStr(command), argument);
	}

	//For MSCController
	public static MSCCommandInstance fromCompound(CompoundTag tag) {
		String str = "";
		ItemStack stack = ItemStack.EMPTY;
		if (tag.contains("argument")) {
			stack.deserializeNBT(tag.getCompound("argument"));
		}
		if (tag.contains("program")) broken:{
			str = tag.getString("program");
		}
		return new MSCCommandInstance(InitMSCCommands.findByStr(str), stack);
	}

	public MSCCommandInstance(ResourceLocation name, ItemStack stack) {
		this.command = InitMSCCommands.findByResLoc(name);
		this.argument = stack;
	}

	public MSCCommandInstance(MSCScheduledCommand command, ItemStack argument) {
		this.command = command;
		this.argument = argument;
	}

	@Override
	public Boolean apply(MSCControllerBlockEntity msc) {
		return this.command.apply(msc, argument);
	}

	@Override
	public boolean test(MSCControllerBlockEntity mscControllerBlockEntity) {
		return this.command.test(mscControllerBlockEntity);
	}

	public CompoundTag getCompound() {
		CompoundTag tag = new CompoundTag();
		tag.putString("program", this.command.command_id.toString());
		tag.put("argument", argument.serializeNBT());
		return tag;
	}
}
