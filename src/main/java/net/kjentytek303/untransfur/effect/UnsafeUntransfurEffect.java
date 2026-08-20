package net.kjentytek303.untransfur.effect;

import net.kjentytek303.untransfur.event.UntransfurPlayerByEffectEvent;
import net.kjentytek303.untransfur.init.InitDamageSources;
import net.kjentytek303.untransfur.init.InitMobEffects;
import net.kjentytek303.untransfur.util.ProcessUntransfur;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.process.TransfurEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;

import static net.kjentytek303.untransfur.util.ProcessUntransfur.ProcUntfRetVal.UNTRANSFUR;


public class UnsafeUntransfurEffect extends MobEffect {
	public UnsafeUntransfurEffect() { super(MobEffectCategory.NEUTRAL, 0xf66924); }

	@Override
	public boolean isDurationEffectTick( int duration, int amplifier ) { return true; }
	@Override
	public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {

		//Deal 0.25 * `effect strength` damage constantly ( 1 DPS for lvl 1, 2 DPS for lvl 2, etc. )
		pLivingEntity.hurt(InitDamageSources.FLINSTON_SOLUTION.source(pLivingEntity.level().registryAccess()), 0.25f * (pAmplifier + 1));

		if( ! (pLivingEntity instanceof Player player )) {
			return;
		}

		//Progress untransfur by 0.05% * `effect strength` / `tick` (untransfur after 100 seconds)
		if(ProcessUntransfur.incrementPlayerUntransfurProgress(player, 0.0005 * (pAmplifier + 1) ) == UNTRANSFUR ) {

			//On untransfur, deal 8 * `effect strength` damage to the entity. Damage is halved for organics.
			player.hurt(InitDamageSources.FLINSTON_SOLUTION.source(player.level().registryAccess()), 4 * (ProcessTransfur.isPlayerLatex(player) ? 2 : 1) * (1 + pAmplifier));

			var event = new UntransfurPlayerByEffectEvent(player, ProcessTransfur.getPlayerTransfurVariant(player), null, InitMobEffects.UNSAFE_UNTRANSFUR.get());
			if (MinecraftForge.EVENT_BUS.post(event)) {return;}
			TransfurEvents.finalizeUntransfurPlayerEvent(event);
		}

		//TODO: //LATER
		//If caddon is installed, progress it's untransfur too.
		//If progressing CAddon untransfur would result in untransfur, deal damage like normal.
	}
}
