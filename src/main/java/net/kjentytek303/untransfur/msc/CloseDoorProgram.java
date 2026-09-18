package net.kjentytek303.untransfur.msc;

import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CloseDoorProgram extends MSCScheduledCommand {

	public CloseDoorProgram() {
		super(Untransfur.modResource("close_door"));
	}

	@Override
	public Boolean apply(MSCControllerBlockEntity bentity, ItemStack argument) {
		bentity.closeDoor();
		return false;
	}

	@Override
	public boolean test(MSCControllerBlockEntity bentity) {
		if( !bentity.isOpen() || !bentity.isDrained() ) {
			bentity.failure_chance += 0.015;
		}
		return false;
	}
}
