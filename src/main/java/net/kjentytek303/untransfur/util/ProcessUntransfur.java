package net.kjentytek303.untransfur.util;

import net.kjentytek303.untransfur.init.InitDamageSources;
import net.kjentytek303.untransfur.network.UntransfurVariables;
import net.minecraft.world.entity.player.Player;


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
		vars.setUntfProgress(current_progress - amount);
		vars.syncPlayerVariables(player);
		return ProcUntfRetVal.SUCCESS;
	}

	public static double getPlayerFlinstonSolution( Player player ) {
		var vars = UntransfurVariables.of(player);
		if( vars == null ) {
			return -2.0;
		}
		return vars.getFlinstonProgress();
	}

	public static ProcUntfRetVal setPlayerFlinstonSolution( Player player, double amount) {
		var vars = UntransfurVariables.of(player);
		if( vars == null ) {
			return ProcUntfRetVal.ERROR;
		}
		vars.setFlinstonDissolvement(amount);
		vars.syncPlayerVariables(player);
		return ProcUntfRetVal.SUCCESS;
	}

	public static ProcUntfRetVal tickPlayerFlinstonProgress( Player player ) {
		return decrementPlayerFlinstonProgress(player, 0.0001220703125); // 1/8192
	}

	public static ProcUntfRetVal decrementPlayerFlinstonProgress( Player player, double amount ) {
		var vars = UntransfurVariables.of(player);
		if( vars == null ) { return ProcUntfRetVal.ERROR; }

		double current_progress = vars.getFlinstonProgress();
		vars.setFlinstonDissolvement(current_progress - amount);
		vars.syncPlayerVariables(player);
		return ProcUntfRetVal.SUCCESS;
	}

	public static ProcUntfRetVal incrementPlayerFlinstonProgress(Player player, double amount) {
		var vars = UntransfurVariables.of(player);
		if( vars == null ) { return ProcUntfRetVal.ERROR; }

		double current_progress = vars.getFlinstonProgress();
		vars.setFlinstonDissolvement(current_progress + amount );

		if( vars.getFlinstonProgress() > 1.0 ) {
			vars.setFlinstonDissolvement(1.0);
			player.hurt(InitDamageSources.FLINSTON_SOLUTION.source(player.level().registryAccess(), player), 2137 * 420 * 69);
			vars.syncPlayerVariables(player);
			return ProcUntfRetVal.UNTRANSFUR;
		}

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
