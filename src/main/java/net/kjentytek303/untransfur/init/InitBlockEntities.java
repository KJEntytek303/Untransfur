package net.kjentytek303.untransfur.init;

import net.kjentytek303.untransfur.block.MSCTankBlock;
import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.kjentytek303.untransfur.block_entity.MSCTankBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.kjentytek303.untransfur.Untransfur.MODID;
import static net.kjentytek303.untransfur.init.InitBlocks.MSC_CONTROLLER;
import static net.kjentytek303.untransfur.init.InitBlocks.MSC_TANK;


public class InitBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);

	public static final RegistryObject<BlockEntityType<MSCControllerBlockEntity>> MSC_CONTROLLER_BLOCK_ENTITY = BLOCK_ENTITY_REGISTRY.register(
		"msc_controller_block_entity",
		() -> BlockEntityType.Builder.of( MSCControllerBlockEntity::new, MSC_CONTROLLER.get()
			).build(null)
	);

	public static final RegistryObject<BlockEntityType<MSCControllerBlockEntity>> MSC_TANK_BLOCK_ENTITY = BLOCK_ENTITY_REGISTRY.register(
		"msc_tank_block_entity",
		() -> BlockEntityType.Builder.of( MSCControllerBlockEntity::new, MSC_TANK.get()
		).build(null)
	);

}
