package net.kjentytek303.untransfur.init;

import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.client.screen.MSCBusMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class InitMenus {
	public static final DeferredRegister<MenuType<?>> MENU_REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Untransfur.MODID);

	public static final RegistryObject<MenuType<MSCBusMenu>> MSC_BUS_MENU = MENU_REGISTRY.register(
		"msc_bus_menu",
		() -> IForgeMenuType.create(MSCBusMenu::new)
	);

}
