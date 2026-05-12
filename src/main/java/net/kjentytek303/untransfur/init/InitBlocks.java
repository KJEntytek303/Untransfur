package net.kjentytek303.untransfur.init;

import net.kjentytek303.untransfur.block.MSCControllerBlock;
import net.kjentytek303.untransfur.block.MSCTankBlock;
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

	public static final RegistryObject<Block> MSC_TANK = registerWithItem(
		"msc_controller",
		() -> new MSCTankBlock(
			BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
		),
		new Item.Properties()
	);


	private static <T extends Block> RegistryObject<T> registerWithItem(String name, Supplier<T> supplier, Item.Properties properties) {
		var ret = BLOCK_REGISTRY.register(name, supplier);
		ITEM_REGISTRY.register(name, () -> new BlockItem( ret.get(), properties));
		return ret;
	}

}
