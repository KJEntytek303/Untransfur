package net.kjentytek303.untransfur.msc;

import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class OpenDoorProgram extends MSCScheduledCommand {

	public static final ResourceLocation ID = Untransfur.modResource("open_door");
	public OpenDoorProgram() {
		super(ID);
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
			bentity.setChanged();
		}
		return ret;
	}
}
