package net.kjentytek303.untransfur.init;

import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.client.screen.MSCInputBusMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class InitMenus {
	public static final DeferredRegister<MenuType<?>> MENU_REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Untransfur.MODID);

	public static final RegistryObject<MenuType<MSCInputBusMenu>> MSC_INPUT_BUS_MENU = MENU_REGISTRY.register(
		"msc_input_bus_menu",
		() -> IForgeMenuType.create(MSCInputBusMenu::new)
	);

}
