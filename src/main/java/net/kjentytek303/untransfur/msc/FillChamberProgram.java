package net.kjentytek303.untransfur.msc;

import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;


public class FillChamberProgram extends MSCScheduledCommand {
	public static final ResourceLocation ID = Untransfur.modResource("fill_chamber");
	public FillChamberProgram() {
		super(ID);
	}


	@Override
	public Boolean apply(MSCControllerBlockEntity bentity, ItemStack argument) {

		bentity.fluid_level0 = bentity.fluid_level;
		bentity.fluid_level += (0.05f / 15.0f); //15 seconds to fill

		if (bentity.fluid_level > 0.5f) {
			bentity.ensureCapturedIsStillInside();
		}

		if (bentity.isFilled()) {
			bentity.fluid_level0 = bentity.fluid_level = 1.0f;
		}
		bentity.markUpdated();
		return !bentity.isFilled();
	}

	@Override
	public boolean test(MSCControllerBlockEntity bentity) {
		boolean ret = !bentity.is_opened && bentity.isDrained();
		if(!ret ) {
			bentity.failure_chance += 0.015;
			bentity.markUpdated();
		}
		return ret;
	}
}
