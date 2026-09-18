package net.kjentytek303.untransfur.init;

import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.kjentytek303.untransfur.msc.CloseDoorProgram;
import net.kjentytek303.untransfur.msc.MSCScheduledCommand;
import net.kjentytek303.untransfur.msc.OpenDoorProgram;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

//import static net.kjentytek303.untransfur.registries.UntransfurRegistries.regKey;

public class InitMSCCommands {
	public static final DeferredRegister<MSCScheduledCommand> MSC_COMMAND_REGISTRY = DeferredRegister.create(Untransfur.modResource("msc_commands"), Untransfur.MODID);
	public static final Supplier<IForgeRegistry<MSCScheduledCommand>> REGISTRY = MSC_COMMAND_REGISTRY.makeRegistry(RegistryBuilder::new);

	public static final RegistryObject<MSCScheduledCommand> EMPTY = MSC_COMMAND_REGISTRY.register("empty", () -> new MSCScheduledCommand(Untransfur.modResource("empty")) {
		@Override
		public Boolean apply(MSCControllerBlockEntity bentity, ItemStack argument) { return false; }

		@Override public boolean test(MSCControllerBlockEntity bentity) { return false; }
	});

	public static final RegistryObject<MSCScheduledCommand> OPEN_DOOR = MSC_COMMAND_REGISTRY.register("open_door", OpenDoorProgram::new);
	public static final RegistryObject<MSCScheduledCommand> CLOSE_DOOR = MSC_COMMAND_REGISTRY.register("close_door", CloseDoorProgram::new);

	//public static final RegistryObject<MSCScheduledCommand> CAPTURE_ENTITY = "";
	//public static final RegistryObject<MSCScheduledCommand> FILL_CHAMBER = "";

	//public static final RegistryObject<MSCScheduledCommand> STABILIZE_ENTITY =
	//public static final RegistryObject<MSCScheduledCommand> TRANSFUR_ENTITY =

	//public static final RegistryObject<MSCScheduledCommand> UNTRANSFUR_ENTITY =
	//public static final RegistryObject<MSCScheduledCommand> MODIFY_ENTITY =
	//public static final RegistryObject<MSCScheduledCommand> RELEASE_ENTITY =

	//public static final RegistryObject<MSCScheduledCommand> DRAIN_CHAMBER =



}
