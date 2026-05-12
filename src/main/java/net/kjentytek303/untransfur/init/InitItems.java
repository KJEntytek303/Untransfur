package net.kjentytek303.untransfur.init;


import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.item.UntransfurSyringeItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class InitItems
{
	public static final DeferredRegister<Item> ITEM_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Untransfur.MODID);

	public static final RegistryObject<Item> UNTRANSFUR_SYRINGE = ITEM_REGISTRY.register(
		   "untransfur_syringe",
		   () -> new UntransfurSyringeItem( new Item.Properties().stacksTo(1) )
	);


}