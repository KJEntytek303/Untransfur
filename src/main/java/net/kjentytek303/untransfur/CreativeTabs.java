package net.kjentytek303.untransfur;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static net.kjentytek303.untransfur.Untransfur.MODID;

public class CreativeTabs
{
	//---------------------------------//
	//          PUBLIC FIELDS          //
	//---------------------------------//
	public static final DeferredRegister<CreativeModeTab> CT_TABS_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
	public static final RegistryObject<CreativeModeTab> UNTRANSFUR_CT_TAB = CT_TABS_REGISTER.register(
		 "changed_untransfur",
		    () -> CreativeModeTab.builder()
			  .title( Component.translatable("untransfur.creative_tabs.untransfur") )
		       .icon(() -> ModItems.UNTRANSFUR_SYRINGE.get().getDefaultInstance())
			  .displayItems(
				 (parameters, output) -> {
			        output.accept( ModItems.UNTRANSFUR_SYRINGE.get() );
			 	 }
		        )
		    .build()
	);
	
	public static void register(IEventBus eventbus) {
		CT_TABS_REGISTER.register(eventbus);
	}
}
