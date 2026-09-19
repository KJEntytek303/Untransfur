package net.kjentytek303.untransfur.msc;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;


public enum ControllerStatus implements StringRepresentable {
	DISASSEMBLED,
	INACTIVE,
	RUNNING,
	ERRORED;

	@Override
	@NotNull
	public String getSerializedName() {
		return switch (this) {
			case DISASSEMBLED -> "disassembled";
			case INACTIVE -> "inactive";
			case RUNNING -> "running";
			case ERRORED -> "errored";
		};
	}
}