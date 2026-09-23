package net.kjentytek303.untransfur.msc;

import com.ibm.icu.impl.Pair;
import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.kjentytek303.untransfur.init.InitDamageSources;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.ai.ImmediateTransfurDecision;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;


public class TransfurEntityProgram extends MSCScheduledCommand {
	public static final ResourceLocation ID = Untransfur.modResource("transfur_entity");

	public TransfurEntityProgram() {
		super(ID);
	}

	@Override
	public Boolean apply(MSCControllerBlockEntity bentity, ItemStack argument) {
		if(!bentity.ensureCapturedIsStillInside()) {
			bentity.failure_chance += 0.025;
			return false;
		}
		Pair<TransfurVariant<?>, Boolean> got = bentity.findTransfurVariant(true);

		TransfurVariant<?> variant = got.first;
		boolean is_safe = got.second;

		if(variant == null) {
			bentity.failure_chance += 0.025;
			bentity.setChanged();
			return false;
		}

		bentity.getChamberedEntity().ifPresent(living_entity -> {
			if(!living_entity.getType().is(ChangedTags.EntityTypes.HUMANOIDS) ) {
				bentity.failure_chance += 0.025;
				living_entity.hurt(InitDamageSources.MSC_DISCONNECT.source(living_entity.level().registryAccess()), 15 );
				return;
			}
			if(is_safe) {
				ProcessTransfur.transfur(living_entity, ImmediateTransfurDecision.safe(variant, TransfurCause.STASIS_CHAMBER));
			} else {
				ProcessTransfur.transfur(living_entity, ImmediateTransfurDecision.unsafe(variant, TransfurCause.STASIS_CHAMBER));
			}
		});

		return false;
	}

	@Override
	public boolean test(MSCControllerBlockEntity bentity) {
		Pair<TransfurVariant<?>, Boolean> got = bentity.findTransfurVariant(false);
		boolean ret = got.first != null &&
			!bentity.isOpen() &&
			bentity.isFilled() &&
			bentity.stabilized &&
			bentity.getChamberedEntity().isPresent() &&
			bentity.getChamberedEntity().get().getType().is(ChangedTags.EntityTypes.HUMANOIDS);
		if( !ret ) {
			bentity.failure_chance += 0.015;
			bentity.setChanged();
		}
		return ret;
	}

	@Override
	public MSCCommandInstance asInstance(ItemStack argument) {
		MSCCommandInstance ret = super.asInstance(argument);
		ret.additional_delay += 19 * 20;
		return ret;
	}
}
