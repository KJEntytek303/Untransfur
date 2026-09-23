package net.kjentytek303.untransfur.msc;

import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;


public class StabilizeEntityProgram extends MSCScheduledCommand {
	public static final ResourceLocation ID = Untransfur.modResource("stabilize_entity");

	public StabilizeEntityProgram(){
		super(ID);
	}

	@Override
	public Boolean apply(MSCControllerBlockEntity bentity, ItemStack argument) {
		if(bentity.stabilizeEntity()) {
			return false;
		}
		bentity.failure_chance += 0.025;
		bentity.setChanged();
		return false;
	}

	@Override
	public boolean test(MSCControllerBlockEntity bentity) {
		boolean ret = !bentity.stabilized && !bentity.isOpen() && bentity.isFilled() && bentity.hasEntity();
		if(!ret) {
			bentity.failure_chance += 0.015;
			bentity.setChanged();
		}
		return ret;
	}
}
