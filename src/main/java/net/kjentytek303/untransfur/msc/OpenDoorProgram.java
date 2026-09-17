package net.kjentytek303.untransfur.msc;

import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class OpenDoorProgram extends MSCScheduledCommand {

	public OpenDoorProgram() {
		super(Untransfur.modResource("open_door"));
	}

	@Override
	public Boolean apply(MSCControllerBlockEntity bentity, ItemStack argument) {
		bentity.openDoor();
		return false;
	}

	@Override
	public boolean test(MSCControllerBlockEntity bentity) {
		boolean ret = bentity.isDrained() && !bentity.isOpen();
		if( !ret ) {
			bentity.failure_chance += 0.015;
			bentity.markUpdated();
		}
		return ret;
	}
}
