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


public class FlinstonSolutionEffect extends MobEffect {
	public FlinstonSolutionEffect() {
		super(MobEffectCategory.HARMFUL, 0xd89069 );
	}

	@Override
	public boolean isDurationEffectTick( int duration, int amplifier ) { return true; }


	@Override
	public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
		//Increase player's "untransfur:flinston" attribute modifier group, which depending on the player flinston contamination
		if(!(pLivingEntity instanceof Player player)) {
			pLivingEntity.hurt(InitDamageSources.FLINSTON_SOLUTION.source(pLivingEntity.level().registryAccess(), pLivingEntity), 2 * (pAmplifier+1));
			return;
		}

		//contamination decreases player max HP, damage, mining speed, jump strength and speed by %. If this attribute reaches 1, the player dies with the cause
		//"Player XYZ dissolved themselves" or "Player XYZ poisoned themselves with Flinston solution"

		//untransfur:flinston increases at 0.2% / tick.
		//Organics have halved speed.
		//Untransfurred players have quartered speed.
		double progress_amount = 0.0005 * (ProcessTransfur.isPlayerTransfurred(player) ? 2 : 1) * (ProcessTransfur.isPlayerLatex(player) ? 2 : 1);
		var flinston_reaction = ProcessUntransfur.incrementPlayerFlinstonProgress(player, progress_amount * (pAmplifier + 1) );

		//Additionally, Flinston Solution progresses untransfur, at 0.025% speed / tick.
		var untf_reaction = ProcessUntransfur.incrementPlayerUntransfurProgress(player, 0.00025 * (pAmplifier + 1) );
		if( flinston_reaction == UNTRANSFUR || untf_reaction == UNTRANSFUR ) {
			if(player.isDeadOrDying()) {
				return;
			}
			if(flinston_reaction == UNTRANSFUR ) {
				var event = new UntransfurPlayerByEffectEvent(player, ProcessTransfur.getPlayerTransfurVariant(player), null, InitMobEffects.FLINSTON_SOLUTION.get());
				if (MinecraftForge.EVENT_BUS.post(event)) {return;}
				TransfurEvents.finalizeUntransfurPlayerEvent(event);
			} else {
				var event = new UntransfurPlayerByEffectEvent(player, ProcessTransfur.getPlayerTransfurVariant(player), null, InitMobEffects.UNSAFE_UNTRANSFUR.get());
				if (MinecraftForge.EVENT_BUS.post(event)) {return;}
				TransfurEvents.finalizeUntransfurPlayerEvent(event);
			}
		}
	}
}