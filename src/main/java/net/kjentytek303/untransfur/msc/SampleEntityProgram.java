package net.kjentytek303.untransfur.msc;

import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.kjentytek303.untransfur.util.ContainerWithIndex;
import net.kjentytek303.untransfur.util.ItemPredicate;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedDamageSources;
import net.ltxprogrammer.changed.item.Syringe;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static net.ltxprogrammer.changed.init.ChangedItems.BLOOD_SYRINGE;
import static net.ltxprogrammer.changed.init.ChangedItems.LATEX_SYRINGE;
import static net.ltxprogrammer.changed.init.ChangedItems.SYRINGE;

public class SampleEntityProgram extends MSCScheduledCommand {
	public static final ResourceLocation ID = Untransfur.modResource("sample_entity");
	public SampleEntityProgram() {
		super(ID);
	}

	@Override
	public Boolean apply(MSCControllerBlockEntity bentity, ItemStack argument) {
		bentity.getChamberedEntity().ifPresent(entity-> {
			List<ContainerWithIndex> syringes = bentity.findItems(new ItemPredicate(new ItemStack(SYRINGE.get())), true);
			ItemStack retSyring;
			if( entity instanceof ChangedEntity changedEntity) {
				TransfurVariant<?> variant = changedEntity.getSelfVariant();
				if (syringes.isEmpty()) {
					bentity.failure_chance += 0.025;
					return;
				}

				retSyring = new ItemStack(LATEX_SYRINGE.get());
				if (variant == null) {
					bentity.failure_chance += 0.025;
					return;
				}

				Syringe.setUnpureVariant(retSyring, variant.getFormId());

			} else if( entity instanceof Player player) {

				if( ProcessTransfur.getPlayerTransfurVariant(player) != null) {
					retSyring = new ItemStack(LATEX_SYRINGE.get());
					Syringe.setUnpureVariant(retSyring, ProcessTransfur.getPlayerTransfurVariant(player).getFormId());
				} else {
					retSyring = new ItemStack(BLOOD_SYRINGE.get());
				}
				Syringe.setOwner(retSyring, player);

			} else if( entity instanceof Villager) {
				retSyring = new ItemStack(BLOOD_SYRINGE.get());
			} else {
				bentity.failure_chance += 0.025;
				return;

			}

			if (!bentity.setIntoOutput(retSyring)) {
				bentity.failure_chance += 0.025;
				return;
			}
			syringes.get(0).item_handler.extractItem(syringes.get(0).slots.get(0), 1, false);
			entity.hurt(ChangedDamageSources.BLOODLOSS.source(bentity.getLevel().registryAccess()), 0.35F);
		});
		return false;
	}

	@Override
	public boolean test(MSCControllerBlockEntity bentity) {
		boolean ret = bentity.getChamberedEntity().isPresent();

		if( !ret) {
			bentity.failure_chance += 0.015;
		}

		return ret;
	}
}
