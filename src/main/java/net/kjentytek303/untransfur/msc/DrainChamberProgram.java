package net.kjentytek303.untransfur.msc;

import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.kjentytek303.untransfur.init.InitDamageSources;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;


public class DrainChamberProgram extends MSCScheduledCommand {
	public static final ResourceLocation ID = Untransfur.modResource("drain_chamber");
	public DrainChamberProgram() {
		super(ID);
	}

	@Override
	public Boolean apply(MSCControllerBlockEntity bentity, ItemStack argument) {
		bentity.fluid_level0 = bentity.fluid_level;
		bentity.fluid_level -= 0.05f / 15.0f;

		if (bentity.fluid_level > 0.5f) {
			bentity.ensureCapturedIsStillInside();
		} else if( bentity.entity_holder != null ) {
			bentity.entity_holder.getPassengers().forEach(Entity::stopRiding);
		}

		if( bentity.stabilized ) {
			bentity.getChamberedEntity().ifPresent( entity -> {
				entity.hurt(InitDamageSources.MSC_DISCONNECT.source(entity.level().registryAccess()), 30);
				bentity.wakeEntity();
				bentity.failure_chance += 0.1;
			});
		}
		if( bentity.isDrained() ) {
			bentity.fluid_level = 0.0f;
			bentity.fluid_level0 = 0.0f;
			bentity.extension_attempts = 0;
		}
		bentity.setChanged();
		return !bentity.isDrained();
	}

	@Override
	public boolean test(MSCControllerBlockEntity bentity) {
		boolean ret = !bentity.isDrained() && !bentity.isOpen();
		if(!ret) {
			bentity.failure_chance += 0.015;
			bentity.markUpdated();
		}
		return ret ;
	}
}
