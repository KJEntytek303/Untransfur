package net.kjentytek303.untransfur.util;

import net.kjentytek303.untransfur.init.InitDamageSources;
import net.kjentytek303.untransfur.network.LivingEntityVariables;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

import static net.ltxprogrammer.changed.init.ChangedAttributes.JUMP_STRENGTH;


public class ProcessFlinston {

	public static final UUID FLINSTON_UUID = UUID.fromString("13ebb29a-927c-4d3f-ad17-cd2cf6d14ac4");

	public static double getPlayerFlinstonSolution( LivingEntity entity ) {
		var vars = LivingEntityVariables.of(entity);
		if( vars == null ) {
			return -2.0;
		}
		return vars.getDissolvement();
	}

	public static ProcessUntransfur.ProcUntfRetVal setDissolvement(LivingEntity entity, double amount) {
		var vars = LivingEntityVariables.of(entity);
		if( vars == null ) {
			return ProcessUntransfur.ProcUntfRetVal.ERROR;
		}
		vars.setDissolvement(amount);
		applyDissolvementAttribute(entity, vars);
		vars.syncPlayerVariables(entity);
		return ProcessUntransfur.ProcUntfRetVal.SUCCESS;
	}

	public static ProcessUntransfur.ProcUntfRetVal tickDissolvement(LivingEntity entity ) {
		return decrementDissolvement(entity, 0.0001220703125); // 1/8192
	}

	public static ProcessUntransfur.ProcUntfRetVal decrementDissolvement(LivingEntity entity, double amount ) {
		var vars = LivingEntityVariables.of(entity);
		if( vars == null ) { return ProcessUntransfur.ProcUntfRetVal.ERROR; }

		double current_progress = vars.getDissolvement();
		vars.setDissolvement(Math.max(current_progress - amount, 0.0));
		applyDissolvementAttribute(entity, vars);
		vars.syncPlayerVariables(entity);
		return ProcessUntransfur.ProcUntfRetVal.SUCCESS;
	}

	public static ProcessUntransfur.ProcUntfRetVal incrementDissolvement(LivingEntity entity, double amount) {
		var vars = LivingEntityVariables.of(entity);
		if( vars == null ) { return ProcessUntransfur.ProcUntfRetVal.ERROR; }

		double current_progress = vars.getDissolvement();
		vars.setDissolvement(current_progress + amount );

		if( vars.getDissolvement() > 1.0 ) {
			vars.setDissolvement(1.0);
			entity.hurt(InitDamageSources.FLINSTON_SOLUTION.source(entity.level().registryAccess(), entity), 2137 * 303 * 67);
			vars.syncPlayerVariables(entity);
			return ProcessUntransfur.ProcUntfRetVal.UNTRANSFUR;
		}

		applyDissolvementAttribute(entity, vars);
		vars.syncPlayerVariables(entity);
		return ProcessUntransfur.ProcUntfRetVal.SUCCESS;
	}

	public static void applyDissolvementAttribute(LivingEntity entity, LivingEntityVariables.Vars vars) {

		AttributeInstance[] attributes = {
			entity.getAttribute(Attributes.MAX_HEALTH),
			entity.getAttribute(Attributes.MOVEMENT_SPEED),
			entity.getAttribute(JUMP_STRENGTH.get()),
			entity.getAttribute(Attributes.ATTACK_DAMAGE),
			entity.getAttribute(Attributes.ATTACK_SPEED)
		};

		for( var attribute : attributes ) {
			if (attribute == null) {
				continue;
			}
			if (attribute.getModifier(FLINSTON_UUID) != null) {
				attribute.removeModifier(FLINSTON_UUID);
			}
			attribute.addTransientModifier(new AttributeModifier(FLINSTON_UUID, "untransfur:dissolvement", Math.max(0.0 - vars.getDissolvement(), -0.99), AttributeModifier.Operation.MULTIPLY_TOTAL));
		}
	}
}
