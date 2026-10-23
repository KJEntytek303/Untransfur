package net.kjentytek303.untransfur.msc;

import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.kjentytek303.untransfur.config.ServerCfg;
import net.kjentytek303.untransfur.effect.FlinstonSolutionEffect;
import net.kjentytek303.untransfur.event.UntransfurPlayerByComplexMSC;
import net.kjentytek303.untransfur.init.InitMobEffects;
import net.kjentytek303.untransfur.util.ContainerWithIndex;
import net.kjentytek303.untransfur.util.ItemPredicate;
import net.kjentytek303.untransfur.util.UntfTags;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.process.TransfurEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;


public class UntransfurEntityProgram extends MSCScheduledCommand {
	public static final ResourceLocation ID = Untransfur.modResource("untransfur_entity");
	public UntransfurEntityProgram() {
		super(ID);
	}

	//TODO
	@Override
	public Boolean apply(MSCControllerBlockEntity bentity, ItemStack argument) {

		ItemPredicate reusable_predicate = new ItemPredicate(UntfTags.Items.MSC_COMPLEX_UNTRANSFUR_ITEMS);
		List<ContainerWithIndex> complex_syringes = bentity.findItems(reusable_predicate, true);

		reusable_predicate.accepted_tag = UntfTags.Items.MSC_UNTRANSFUR_ITEMS;
		List<ContainerWithIndex> untransfur_syringes = bentity.findItems(reusable_predicate, true);

		reusable_predicate.accepted_tag = UntfTags.Items.BIOMASS;
		List<ContainerWithIndex> biomass = bentity.findItems(reusable_predicate, true);

		LivingEntity entity = bentity.getChamberedEntity().orElse(null);
		if(entity == null) {
			bentity.failure_chance += 0.025;
			return false;
		}

		if(!(entity instanceof ServerPlayer player)) {
			if( !untransfur_syringes.isEmpty()) {
				entity.addEffect( new MobEffectInstance(InitMobEffects.UNSAFE_UNTRANSFUR.get(), 60, 2));
				untransfur_syringes.get(0).item_handler.extractItem(untransfur_syringes.get(0).slots.get(0), 1, false);
				return false;
			}
			return false;
		}

		goto_try_regular_untf: {

		//Complex Untransfur
		if (complex_syringes.isEmpty()) {
			break goto_try_regular_untf;
		}

		if( !bentity.setIntoOutput(new ItemStack(ChangedItems.SYRINGE.get())) ) {
			bentity.failure_chance += 0.025;
			return false; //Can't set into output.
		}

		if( ProcessTransfur.getPlayerTransfurVariantSafe(player).isPresent() && ProcessTransfur.getPlayerTransfurVariantSafe(player).get().getChangedEntity().getType().is(ChangedTags.EntityTypes.LATEX) ) {
			int biomass_counter = ServerCfg.MSC_BIOMASS_REQUIREMENT.get();
			int tmp = biomass_counter;
			for (var buffer : biomass) enough_biomass:{
				for (var slot : buffer.slots) {
					biomass_counter -= buffer.item_handler.getStackInSlot(slot).getCount();

					if (biomass_counter <= 0) {
						break enough_biomass;
					}
				}
			}

			if (biomass_counter > 0) {
				break goto_try_regular_untf;
			}

			biomass_counter = tmp;


			for (var buffer : biomass) finished_biomass:{
				for (var slot : buffer.slots) {

					biomass_counter -= buffer.item_handler.extractItem(slot, Math.min(buffer.item_handler.getStackInSlot(slot).getCount(), biomass_counter), false).getCount();

					if (biomass_counter == 0) {
						break finished_biomass;
					}
				}
			}
		}

		complex_syringes.get(0).item_handler.extractItem(complex_syringes.get(0).slots.get(0), 1, false);
		bentity.performComplexUntransfur();
		return false;

		} //try_regular_untf

		//If we can't do it safely, check configs for fallback.

		//Case SIMPLE && ORGANICS_ONLY
		//Check if we have untf syringe available. If yes, announce UntfByBlockEvent and return;

		//Case COMPLEX
		//if we have a latex inside, apply flinston solution effect, long enough to ensure that the said entity dies.
		//if said entity is an organic, apply unsafe_untransfur effect, long enough to ensure

		switch( ServerCfg.UNTRANSFUR_HANDLE_MODE.get()) {
			case SIMPLE:
			case ORGANICS_ONLY:
				if( ProcessTransfur.getPlayerTransfurVariantSafe(player).isPresent() ) {
					var event = new UntransfurPlayerByComplexMSC(bentity.getBlockState(), bentity.getBlockPos(), player, ProcessTransfur.getPlayerTransfurVariantSafe(player).get(), null);
					if (!MinecraftForge.EVENT_BUS.post(event)) {
						TransfurEvents.finalizeUntransfurPlayerEvent(event);
					}
				}
				return false;
			case COMPLEX:
				if( ProcessTransfur.getPlayerTransfurVariantSafe(player).isPresent() && ProcessTransfur.getPlayerTransfurVariantSafe(player).get().getChangedEntity().getType().is(ChangedTags.EntityTypes.LATEX) ) {
					player.addEffect(new MobEffectInstance(InitMobEffects.FLINSTON_SOLUTION.get(), 60, 2));
				} else {
					player.addEffect(new MobEffectInstance(InitMobEffects.UNSAFE_UNTRANSFUR.get(), 60, 2));
				}
				return false;
		}

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
