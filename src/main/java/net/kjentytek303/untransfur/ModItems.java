package net.kjentytek303.untransfur;


import net.kjentytek303.untransfur.item.UntransfurSyringeItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems
{
	//---------------------------------//
	//         PUBLIC METHODS          //
	//---------------------------------//
	
	public static void register (IEventBus eventbus ) {
		ITEM_REGISTER.register(eventbus);
	}
	
	
	//---------------------------------//
	//         PUBLIC FIELDS           //
	//---------------------------------//
	public static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, Untransfur.MODID);
	
	//Registry
	public static RegistryObject<Item> UNTRANSFUR_SYRINGE = ITEM_REGISTER.register(
		   "untransfur_syringe",
		   () -> new UntransfurSyringeItem( new Item.Properties()
				 .stacksTo(1) )
	);
}
