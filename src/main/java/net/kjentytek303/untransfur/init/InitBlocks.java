package net.kjentytek303.untransfur.init;

import net.kjentytek303.untransfur.block.MSCControllerBlock;
//import net.kjentytek303.untransfur.block.MSCTankBlock;
import net.kjentytek303.untransfur.block.MSCInputBusBlock;
import net.kjentytek303.untransfur.block.MSCInputHatchBlock;
import net.kjentytek303.untransfur.block.MSCOutputBusBlock;
import net.kjentytek303.untransfur.block.MSCOutputHatchBlock;
import net.kjentytek303.untransfur.block.MSCRedstoneLogicAdapterBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static net.kjentytek303.untransfur.Untransfur.MODID;
import static net.kjentytek303.untransfur.init.InitItems.ITEM_REGISTRY;


public class InitBlocks {
	public static final DeferredRegister<Block> BLOCK_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

	public static final RegistryObject<Block> MSC_CONTROLLER = registerWithItem(
		"msc_controller",
		() -> new MSCControllerBlock (
			BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
		),
		new Item.Properties()
	);
	public static final RegistryObject<Block> MSC_METAL_BLOCK = registerWithItem(
		"msc_metal_block",
		() -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)),
		new Item.Properties()
	);

	public static final RegistryObject<Block> MSC_INPUT_BUS = registerWithItem(
		"msc_input_bus",
		() -> new MSCInputBusBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)),
		new Item.Properties()
	);

	public static final RegistryObject<Block> MSC_OUTPUT_BUS = registerWithItem(
		"msc_output_bus",
		() -> new MSCOutputBusBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)),
		new Item.Properties()
	);
	public static final RegistryObject<Block> MSC_INPUT_HATCH = registerWithItem(
		"msc_input_hatch",
		() -> new MSCInputHatchBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)),
		new Item.Properties()
	);

	public static final RegistryObject<Block> MSC_OUTPUT_HATCH = registerWithItem(
		"msc_output_hatch",
		() -> new MSCOutputHatchBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)),
		new Item.Properties()
	);

	public static final RegistryObject<Block> MSC_REDSTONE_LOGIC_ADAPTER = registerWithItem(
		"msc_redstone_logic_adapter",
		() -> new MSCRedstoneLogicAdapterBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)),
		new Item.Properties()
	);

	public static final RegistryObject<Block> MSC_ADVANCED_LOGIC_ADAPTER = registerWithItem(
		"msc_advanced_logic_adapter",
		() -> new MSCRedstoneLogicAdapterBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)),
		new Item.Properties()
	);


/*
	public static final RegistryObject<Block> MSC_TANK = registerWithItem(
		"msc_tank",
		() -> new MSCTankBlock(
			BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
		),
		new Item.Properties()
	);
*/

	private static <T extends Block> RegistryObject<T> registerWithItem(String name, Supplier<T> supplier, Item.Properties properties) {
		var ret = BLOCK_REGISTRY.register(name, supplier);
		ITEM_REGISTRY.register(name, () -> new BlockItem( ret.get(), properties));
		return ret;
	}

}
