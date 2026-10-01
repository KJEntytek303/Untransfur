package net.kjentytek303.untransfur.msc;

import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.block.MSCControllerBlock;
import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;


public class CaptureEntityProgram extends MSCScheduledCommand {

	public static final ResourceLocation ID = Untransfur.modResource("capture_entity");
	public CaptureEntityProgram() {
		super(ID);
	}

	@Override
	public Boolean apply(MSCControllerBlockEntity bentity, ItemStack argument) {
		return bentity.getEntitiesWithin(LivingEntity.class, MSCControllerBlock.getDetectionSizeForEntrance(bentity.getBlockState(), bentity.getBlockPos())).isEmpty(); // if empty - continue, else return;
	}

	@Override
	public boolean test(MSCControllerBlockEntity bentity) {
		boolean ret = bentity.isOpen() && bentity.isDrained();
		if(!ret) {
			bentity.failure_chance += 0.015;
			bentity.markUpdated();
		}
		return ret;
	}
}
