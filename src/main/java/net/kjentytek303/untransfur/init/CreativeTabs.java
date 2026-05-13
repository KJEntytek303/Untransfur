package net.kjentytek303.untransfur.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static net.kjentytek303.untransfur.Untransfur.MODID;

public class CreativeTabs
{
	public static final DeferredRegister<CreativeModeTab> CT_TABS_REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

	public static final RegistryObject<CreativeModeTab> UNTRANSFUR_CT_TAB = CT_TABS_REGISTRY.register(
		 "changed_untransfur",
		 	() -> CreativeModeTab.builder()
				.title( Component.translatable("untransfur.creative_tabs.untransfur") )
				.icon(() -> InitItems.UNTRANSFUR_SYRINGE.get().getDefaultInstance())
				.displayItems(
					(parameters, item) -> {
						item.accept(InitItems.UNTRANSFUR_SYRINGE.get());
						item.accept(InitBlocks.MSC_CONTROLLER.get());
					}
		        	)
				.build()
	);
}