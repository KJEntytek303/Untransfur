package net.kjentytek303.untransfur.msc;

import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;


public class WakeEntityProgram extends MSCScheduledCommand {
	public static final ResourceLocation ID = Untransfur.modResource("wake_entity");
	public WakeEntityProgram() {
		super(ID);
	}

	@Override
	public Boolean apply(MSCControllerBlockEntity bentity, ItemStack argument) {
		if(bentity.wakeEntity()) {
			return false;
		}
		bentity.failure_chance += 0.025;
		bentity.setChanged();
		return false;
	}

	@Override
	public boolean test(MSCControllerBlockEntity bentity) {
		boolean ret = !bentity.isOpen() && bentity.isFilled() && bentity.stabilized;
		if(!ret) {
			bentity.failure_chance += 0.015;
			bentity.setChanged();
		}
		return false;
	}
}
