package net.kjentytek303.untransfur.init;

import net.kjentytek303.untransfur.Untransfur;
import net.ltxprogrammer.changed.init.ChangedDamageSources;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;


public class InitDamageSources {
	public static final ChangedDamageSources.DamageTypeHolder FLINSTON_SOLUTION = new ChangedDamageSources.DamageTypeHolder(ResourceKey.create(Registries.DAMAGE_TYPE, Untransfur.modResource("flinston_solution")));
}
