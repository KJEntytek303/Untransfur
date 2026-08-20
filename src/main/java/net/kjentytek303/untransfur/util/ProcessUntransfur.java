package net.kjentytek303.untransfur.util;

import net.kjentytek303.untransfur.Untransfur;
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
		return 0.0;
	}

	public static void setPlayerFlinstonSolution( Player player, double amount) {

	}

	public enum ProcUntfRetVal {
		SUCCESS,
		ERROR,
		PLAYER_IMMUNE
	}
}
