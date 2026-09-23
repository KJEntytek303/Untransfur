package net.kjentytek303.untransfur.msc;

import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.ltxprogrammer.changed.entity.ModifiableEntity;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.process.TransfurEvents;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;


public class MSCDefaultCommands {
	public static boolean applyModifications(@NotNull MSCControllerBlockEntity msc, @NotNull CompoundTag modifications) {
		msc.getChamberedLatex().ifPresent(entity -> {
			msc.skip_modify = true;
			if (entity.getChangedEntity() instanceof ModifiableEntity modifiable) {
				var vectors = modifiable.getModificationVectors();

				AtomicBoolean any_match = new AtomicBoolean(false);
				modifications.getAllKeys().forEach(key -> {
					if (!vectors.containsKey(key)) {
						return;
					}
					if (vectors.get(key).readFromTag(modifications.get(key))) {
						any_match.set(true);
					}
				});
				if (any_match.getAcquire()) {
					ChangedSounds.broadcastSound(entity.getEntity(), ChangedSounds.STASIS_CHAMBER_MODIFY_LATEX, 1.0f, 1.0f);
				}
				return;
			}
			ChangedTransfurVariants.Gendered.getOpposite(entity.getSelfVariant()).ifPresent(other_variant -> {
				entity.replaceVariant(other_variant);
				ChangedSounds.broadcastSound(entity.getEntity(), ChangedSounds.STASIS_CHAMBER_MODIFY_LATEX, 1.0f, 1.0f);
			});
		});
		msc.markUpdated();
		return true;
	}


	public static boolean modifyEntity( @NotNull MSCControllerBlockEntity msc, ItemStack args ) {
		if( !msc.ensureCapturedIsStillInside() ) {
			return false;
		}

		if( msc.getChamberedEntity().map( (entity) -> msc.shouldChamberIdle() ).orElse(false) ) {
			msc.one_time_menu_open = false;
			return true;
		}

		if( msc.one_time_menu_open && msc.openers_counter.getOpenerCount() <= 0 ) {
			msc.one_time_menu_open = false;

			boolean player_opened = msc.getChamberedEntity().map(entity -> {
				if (!(entity instanceof ServerPlayer player)) {
					return false;
				}
				NetworkHooks.openScreen(player, msc.getBlockState().getMenuProvider(msc.getLevel(), msc.getBlockPos()), extra -> {
					extra.writeBlockPos(msc.getBlockPos());
					extra.writeBoolean(true);
				});
				return true;
			}).orElse(false);

			if (player_opened) {
				return true;
			}
		}

		if ( msc.skip_modify ) {
			msc.skip_modify = false;
			msc.one_time_menu_open = true;
			return false;
		}

		msc.getChamberedLatex().ifPresent( entity -> {
			ChangedTransfurVariants.Gendered.getOpposite(entity.getSelfVariant()).ifPresent( other_variant -> {
				entity.replaceVariant(other_variant);
				ChangedSounds.broadcastSound(entity.getEntity(), ChangedSounds.STASIS_CHAMBER_MODIFY_LATEX, 1.0f, 1.0f);
			});
		});

		msc.one_time_menu_open = true;
		return false;
	}

	public static boolean UntransfurEntity(@NotNull MSCControllerBlockEntity msc, Object args ) {
		//Check if we have a flinston syringe.
		//Check if entity is stabilized
		//If latex: Check if we have enough biomass
		//If true, post untransfur event.
		msc.getChamberedEntity().ifPresent( entity -> {
			if( !( entity instanceof Player player )) {
				return;
				//apply untf
			}
			var event = new TransfurEvents.UntransfurPlayerByBlockEvent(msc.getBlockState(), msc.getBlockPos(), player, ProcessTransfur.getPlayerTransfurVariant(player), null);
			if(!MinecraftForge.EVENT_BUS.post(event)) {
				TransfurEvents.finalizeUntransfurPlayerEvent(event);
			}
		});

		return false;
	}
}
