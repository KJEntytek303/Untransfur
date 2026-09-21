package net.kjentytek303.untransfur.init;

import net.kjentytek303.untransfur.Untransfur;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
				.displayItems( (parameters, tab_builder) -> {
					InitItems.ITEM_REGISTRY.getEntries().forEach(
						itemRegistryObject -> tab_builder.accept(itemRegistryObject.get())
					);
					for( var program : InitMSCCommands.MSC_COMMAND_REGISTRY.getEntries() ) {
						tab_builder.accept(mscProgrammedRom( program.get().command_id ));
					}
				})
				.build()
	);

	public static ItemStack mscProgrammedRom(ResourceLocation loc) {
		CompoundTag tag = new CompoundTag();
		tag.putString("program", loc.toString() );
		var ret = new ItemStack(InitItems.MSC_PROGRAM_ROM.get());
		ret.setTag(tag);
		return ret;
	}
}