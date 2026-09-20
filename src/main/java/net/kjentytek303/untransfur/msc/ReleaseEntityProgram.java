package net.kjentytek303.untransfur.msc;

import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;


public class ReleaseEntityProgram extends MSCScheduledCommand {

	public static final ResourceLocation ID = Untransfur.modResource("release_entity");
	public ReleaseEntityProgram() {
		super(ID);
	}

	@Override
	public Boolean apply(MSCControllerBlockEntity bentity, ItemStack argument) {
		return false;
	}

	@Override
	public boolean test(MSCControllerBlockEntity bentity) {
		return false;
	}
}
