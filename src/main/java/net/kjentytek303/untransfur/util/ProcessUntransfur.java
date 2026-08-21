package net.kjentytek303.untransfur.util;

import net.kjentytek303.untransfur.init.InitDamageSources;
import net.kjentytek303.untransfur.network.UntransfurVariables;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ProcessUntransfur {

	//-2 on error, -1 on immunity
	public static double getUntransfurProgress(Player player) {
		var vars = UntransfurVariables.of(player);
		if( vars == null ) {
			return -2.0;
		}
		return vars.getUntfProgress();
	}

	public static ProcUntfRetVal setPlayerUntransfurProgress(Player player, double amount) {
		var vars = UntransfurVariables.of(player);
		if( vars == null ) {
			return ProcUntfRetVal.ERROR;
		}
		vars.setUntfProgress(amount);
		vars.syncPlayerVariables(player);
		return ProcUntfRetVal.SUCCESS;
	}

	public static ProcUntfRetVal incrementPlayerUntransfurProgress(Player player, double amount) {
		var vars = UntransfurVariables.of(player);
		if( vars == null ) { return ProcUntfRetVal.ERROR; }

		double current_progress = vars.getUntfProgress();
		vars.setUntfProgress(current_progress + amount );

		if( vars.getUntfProgress() > 1.0 ) {
			vars.setUntfProgress(0.0);
			vars.syncPlayerVariables(player);
			return ProcUntfRetVal.UNTRANSFUR;
		}

		vars.syncPlayerVariables(player);
		return ProcUntfRetVal.SUCCESS;
	}

	public static ProcUntfRetVal tickPlayerUntransfurProgress( Player player ) {
		return decrementPlayerUntransfurProgress(player, 0.0001220703125); // 1/8192
	}

	public static ProcUntfRetVal decrementPlayerUntransfurProgress( Player player, double amount ) {
		var vars = UntransfurVariables.of(player);
		if( vars == null ) { return ProcUntfRetVal.ERROR; }

		double current_progress = vars.getUntfProgress();
		vars.setUntfProgress(Math.max(current_progress - amount, 0.0));
		vars.syncPlayerVariables(player);
		return ProcUntfRetVal.SUCCESS;
	}

	public enum ProcUntfRetVal {
		SUCCESS,
		UNTRANSFUR,
		ERROR,
		PLAYER_IMMUNE
	}
}
