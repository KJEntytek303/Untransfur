package net.kjentytek303.untransfur.init;


import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.item.MSCProgramRomItem;
import net.kjentytek303.untransfur.item.UntransfurSyringeItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
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

	public static final RegistryObject<Item> MSC_PROGRAM_ROM = ITEM_REGISTRY.register(
		"msc_program_rom",
		() -> new MSCProgramRomItem(new Item.Properties().stacksTo(1))
	);

	public static final RegistryObject<Item> BIOMASS = ITEM_REGISTRY.register(
		"biomass",
		() -> new Item(new Item.Properties()
			.food(new FoodProperties.Builder()
				.meat()
				.nutrition(1)
				.saturationMod(-0.1f)
				.effect(() -> new MobEffectInstance(MobEffects.HUNGER, 40, 2), 1)
				.effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 50, 0), 1)
				.effect(() -> new MobEffectInstance(MobEffects.POISON, 60, 2), 1)
				.effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, 2), 1)
				.build())
		)
	);
}