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
				.displayItems( (parameters, item) -> {
					InitItems.ITEM_REGISTRY.getEntries().forEach(
						itemRegistryObject -> item.accept(itemRegistryObject.get())
					);
					item.accept(mscProgrammedRom(Untransfur.modResource("open_door")));
					item.accept(mscProgrammedRom(Untransfur.modResource("capture_entity")));

					item.accept(mscProgrammedRom(Untransfur.modResource("close_door")));
					item.accept(mscProgrammedRom(Untransfur.modResource("fill_chamber")));
					item.accept(mscProgrammedRom(Untransfur.modResource("extend_stasis")));

					item.accept(mscProgrammedRom(Untransfur.modResource("transfur")));
					item.accept(mscProgrammedRom(Untransfur.modResource("untransfur")));
					item.accept(mscProgrammedRom(Untransfur.modResource("modify")));

					item.accept(mscProgrammedRom(Untransfur.modResource("drain_chamber")));
				})
				.build()
	);

	public static ItemStack mscProgrammedRom(ResourceLocation loc) {
		CompoundTag tag = new CompoundTag();
		tag.putString("program", "untransfur.msc.program." + loc.toString() );
		var ret = new ItemStack(InitItems.MSC_PROGRAM_ROM.get());
		ret.setTag(tag);
		return ret;
	}
}