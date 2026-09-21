package net.kjentytek303.untransfur.init;

import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.kjentytek303.untransfur.msc.CaptureEntityProgram;
import net.kjentytek303.untransfur.msc.CloseDoorProgram;
import net.kjentytek303.untransfur.msc.DrainChamberProgram;
import net.kjentytek303.untransfur.msc.FillChamberProgram;
import net.kjentytek303.untransfur.msc.MSCScheduledCommand;
import net.kjentytek303.untransfur.msc.OpenDoorProgram;
import net.kjentytek303.untransfur.msc.ReleaseEntityProgram;
import net.kjentytek303.untransfur.msc.WaitProgram;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

//import static net.kjentytek303.untransfur.registries.UntransfurRegistries.regKey;

public class InitMSCCommands {
	public static final ResourceKey<Registry<MSCScheduledCommand>> MSC_COMMAND_KEY = ResourceKey.createRegistryKey(Untransfur.modResource("msc_commands"));
	public static final DeferredRegister<MSCScheduledCommand> MSC_COMMAND_REGISTRY = DeferredRegister.create(MSC_COMMAND_KEY, Untransfur.MODID);
	public static final Supplier<IForgeRegistry<MSCScheduledCommand>> REGISTRY = MSC_COMMAND_REGISTRY.makeRegistry(RegistryBuilder::new);

	public static final RegistryObject<MSCScheduledCommand> EMPTY = MSC_COMMAND_REGISTRY.register("empty", () -> new MSCScheduledCommand(Untransfur.modResource("empty")) {
		@Override
		@Contract(pure = true)
		public Boolean apply(MSCControllerBlockEntity bentity, ItemStack argument) { return false; }

		@Contract(pure = true)
		@Override public boolean test(MSCControllerBlockEntity bentity) { return false; }
	});

	public static final RegistryObject<MSCScheduledCommand> OPEN_DOOR = MSC_COMMAND_REGISTRY.register(OpenDoorProgram.ID.getPath(), OpenDoorProgram::new);
	public static final RegistryObject<MSCScheduledCommand> CLOSE_DOOR = MSC_COMMAND_REGISTRY.register(CloseDoorProgram.ID.getPath(), CloseDoorProgram::new);

	public static final RegistryObject<MSCScheduledCommand> CAPTURE_ENTITY = MSC_COMMAND_REGISTRY.register(CaptureEntityProgram.ID.getPath(), CaptureEntityProgram::new);
	public static final RegistryObject<MSCScheduledCommand> FILL_CHAMBER = MSC_COMMAND_REGISTRY.register(FillChamberProgram.ID.getPath(), FillChamberProgram::new);

	public static final RegistryObject<MSCScheduledCommand> STABILIZE_ENTITY = MSC_COMMAND_REGISTRY.register(StabilizeEntityProgram.ID.getPath(), WakeEntityProgram::new);
	public static final RegistryObject<MSCScheduledCommand> WAIT = MSC_COMMAND_REGISTRY.register(WaitProgram.ID.getPath(), WaitProgram::new);
	public static final RegistryObject<MSCScheduledCommand> TRANSFUR_ENTITY = MSC_COMMAND_REGISTRY.register(TransfurEntityProgram.ID.getPath(), TransfurEntityProgram::new);

	public static final RegistryObject<MSCScheduledCommand> UNTRANSFUR_ENTITY = MSC_COMMAND_REGISTRY.register(UntransfurEntityProgram.ID.getPath(), UntransfurEntityProgram::new);
	//public static final RegistryObject<MSCScheduledCommand> MODIFY_ENTITY =
	public static final RegistryObject<MSCScheduledCommand> WAKE_ENTITY = MSC_COMMAND_REGISTRY.register(WakeEntityProgram.ID.getPath(), WakeEntityProgram::new);
	public static final RegistryObject<MSCScheduledCommand> RELEASE_ENTITY = MSC_COMMAND_REGISTRY.register(ReleaseEntityProgram.ID.getPath(), ReleaseEntityProgram::new);

	public static final RegistryObject<MSCScheduledCommand> DRAIN_CHAMBER = MSC_COMMAND_REGISTRY.register(DrainChamberProgram.ID.getPath(), DrainChamberProgram::new);


	public static MSCScheduledCommand findByResLoc(@NotNull ResourceLocation location) {
		var ret = REGISTRY.get().getValue(location);
		if(ret == null) {
			Untransfur.LOGGER.warn("Entry {}:{} not found in MSC Command Registry", location.getNamespace(), location.getPath());
			return EMPTY.get();
		}
		return ret;
	}

	public static MSCScheduledCommand findByStr(@NotNull String str) {
		if( ResourceLocation.isValidResourceLocation(str)) {
			return findByResLoc(ResourceLocation.parse(str));
		}
		Untransfur.LOGGER.error("Attempted to query MSC Command Registry with invalid string: {}", str);
		return EMPTY.get();
	}

}