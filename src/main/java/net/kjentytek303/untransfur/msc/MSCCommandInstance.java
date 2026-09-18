package net.kjentytek303.untransfur.msc;

import net.kjentytek303.untransfur.Untransfur;
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

		if(!command.matches("^untransfur\\.msc\\.program\\.([a-z][a-z0-9_]{2,}):([a-z][a-z0-9_]*)$")) {
			Untransfur.LOGGER.warn("Couldn't create MSCComandInstance: Malformed program id: {}", command);
			return new MSCCommandInstance(InitMSCCommands.EMPTY.get(), ItemStack.EMPTY);
		}

		String[] str_arr = command.substring(23).split(":");

		ResourceLocation command_id = ResourceLocation.fromNamespaceAndPath(str_arr[0], str_arr[1]);
		if (InitMSCCommands.REGISTRY.get().getValue(command_id) == null) {
			Untransfur.LOGGER.warn("Couldn't create MSCCommandInstance: No such entry in the registry: {}", command);
			return new MSCCommandInstance(InitMSCCommands.EMPTY.get(), ItemStack.EMPTY);
		}
		return new MSCCommandInstance( InitMSCCommands.REGISTRY.get().getValue(command_id), argument);
	}

	public static MSCCommandInstance fromCompound(CompoundTag tag) {
		String str = "";
		ItemStack stack = ItemStack.EMPTY;
		if (tag.contains("argument")) {
			stack.deserializeNBT(tag.getCompound("argument"));
		}
		if (tag.contains("program")) broken:{
			str = tag.getString("program");
		}
		if(str.matches("^([a-z][a-z0-9_]{2,}):([a-z][a-z0-9_]*)$" ) ) {
			String[] str_arr = str.split(":");
			return new MSCCommandInstance(ResourceLocation.fromNamespaceAndPath(str_arr[0], str_arr[1]));
		}
		Untransfur.LOGGER.warn("Couldn't create MSCCommandInstace: {}, regex mismatch", str);
		return new MSCCommandInstance(InitMSCCommands.EMPTY.get(), ItemStack.EMPTY);
	}

	public MSCCommandInstance(ResourceLocation name) {
		this(name, ItemStack.EMPTY);
	}

	public MSCCommandInstance(ResourceLocation name, ItemStack stack) {
		if (InitMSCCommands.REGISTRY.get().getValue(name) == null) {
			Untransfur.LOGGER.warn("Couldn't create MSCCommandInstance from id {}:{}", name.getNamespace(), name.getPath());
			this.command = InitMSCCommands.EMPTY.get();
		} else {
			this.command = InitMSCCommands.REGISTRY.get().getValue(name);
		}
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
