package net.kjentytek303.untransfur;

import net.kjentytek303.untransfur.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static net.kjentytek303.untransfur.Untransfur.MODID;

public class CreativeTabs
{
	//---------------------------------//
	//          PUBLIC FIELDS          //
	//---------------------------------//
	public static final DeferredRegister<CreativeModeTab> CT_TABS_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
	public static final RegistryObject<CreativeModeTab> UNTRANSFUR_CT_TAB = CT_TABS_REGISTER.register("Changed Untransfur", () -> CreativeModeTab.builder()
		   .withTabsBefore(CreativeModeTabs.COMBAT)
		   .icon(() -> ModItems.UNTRANSFUR_SYRINGE.get().getDefaultInstance())
		   .displayItems(
				 (parameters, output) -> {
			        output.accept(ModItems.UNTRANSFUR_SYRINGE.get());
				 })
		   .build()
	);
	
	
}
