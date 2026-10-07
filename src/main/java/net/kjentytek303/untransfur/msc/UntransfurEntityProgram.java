package net.kjentytek303.untransfur.msc;

import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
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

		//check if we can do MSCComplexUnransfur.
			//Organics - untf syringe.
			//Latexes - untf syringe and 16 biomass inside MSCInput.
			//Announce UntransfurByComplexMSC event on success and return;

		//If we can't do it safely, check configs for fallback.

		//Case SIMPLE && ORGANICS_ONLY
		//Check if we have untf syringe available. If yes, announce UntfByBlockEvent and return;

		//Case COMPLEX
		//if we have a latex inside, apply flinston solution effect, long enough to ensure that the said entity dies.

		bentity.performComplexUntransfur();

		return false;
	}

	@Override
	public boolean test(MSCControllerBlockEntity bentity) {
		boolean ret = !bentity.isOpen() &&
			bentity.isFilled() &&
			bentity.isStabilized() &&
			bentity.getChamberedLatex().isPresent()
			;
		if( !ret ) {
			bentity.failure_chance += 0.015;
			bentity.markUpdated();
		}
		return ret;
	}
}
