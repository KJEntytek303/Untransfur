package net.kjentytek303.untransfur.msc;

import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;


public interface IMSCAugment {
	void addController(MSCControllerBlockEntity ctrl);
	default void invalidateController() {
		addController(null);
		updateController();
	}
	void updateController();
}