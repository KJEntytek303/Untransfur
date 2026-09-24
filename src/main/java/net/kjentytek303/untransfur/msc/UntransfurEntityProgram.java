package net.kjentytek303.untransfur.msc;

import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;


public class UntransfurEntityProgram extends MSCScheduledCommand {
	public static final ResourceLocation ID = Untransfur.modResource("untransfur_entity");
	public UntransfurEntityProgram() {
		super(ID);
	}

	//TODO
	@Override
	public Boolean apply(MSCControllerBlockEntity bentity, ItemStack argument) {
		return null;
	}

	@Override
	public boolean test(MSCControllerBlockEntity bentity) {
		return false;
	}
}
