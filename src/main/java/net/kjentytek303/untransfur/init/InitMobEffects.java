package net.kjentytek303.untransfur.init;

import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.effect.FlinstonSolutionEffect;
import net.kjentytek303.untransfur.effect.UnsafeUntransfurEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class InitMobEffects {
	public static final DeferredRegister<MobEffect> EFFECT_REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Untransfur.MODID);

	public static final RegistryObject<MobEffect> UNSAFE_UNTRANSFUR = EFFECT_REGISTRY.register("unsafe_untransfur", UnsafeUntransfurEffect::new);
	public static final RegistryObject<MobEffect> FLINSTON_SOLUTION = EFFECT_REGISTRY.register("flinston_solution", FlinstonSolutionEffect::new);
}
