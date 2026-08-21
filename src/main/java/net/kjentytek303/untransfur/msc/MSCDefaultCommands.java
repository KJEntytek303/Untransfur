package net.kjentytek303.untransfur.msc;

import net.kjentytek303.untransfur.block.MSCControllerBlock;
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

	public static boolean openDoor(@NotNull MSCControllerBlockEntity msc, Object args) {
		msc.openDoor();
		return false;
	}

	public static boolean captureEntity(@NotNull MSCControllerBlockEntity msc, Object args) {
		var entities = msc.getEntitiesWithin();
		if (entities.size() != 1) {
			return true;
		}
		msc.cached_entity = entities.get(0);
		msc.closeDoor();
		return false;
	}

	public static boolean closeDoor(@NotNull MSCControllerBlockEntity msc, Object args) {
		msc.closeDoor();
		return false;
	}

	public static boolean fillChamber(@NotNull MSCControllerBlockEntity msc, Object args) {
		if (msc.getBlockState().getBlock() instanceof MSCControllerBlock msc_bl) {
			msc_bl.markAsActive(msc.getBlockState(), msc.getLevel(), msc.getBlockPos());
		}

		msc.fluid_level0 = msc.fluid_level;
		msc.fluid_level += (0.05f / 15.0f); //15 seconds to fill

		if (msc.fluid_level > 0.5f) {
			msc.ensureCapturedIsStillInside();
		}

		if (msc.isFilled()) {
			msc.fluid_level0 = msc.fluid_level = 1.0f;
		}
		msc.markUpdated();
		return !msc.isFilled();
	}

	public static boolean stabilizeEntity( @NotNull MSCControllerBlockEntity msc, Object args ) {
		if( !msc.ensureCapturedIsStillInside()) {
			return false;
		}

		msc.stabilized = true;
		msc.getChamberedEntity().map(EntityUtil::playerOrNull).map(Player::level).ifPresent( level -> {
			if( level instanceof ServerLevel server_level) {
				server_level.updateSleepingPlayerList();
			}
		});
		return false;
	}

	public static boolean wakeEntity(@NotNull MSCControllerBlockEntity msc, Object args ) {
		if( !msc.ensureCapturedIsStillInside() ) {
			return false;
		}
		msc.stabilized = false;
		msc.getChamberedEntity().map(EntityUtil::playerOrNull).map(Player::level).ifPresent( level -> {
			if( level instanceof ServerLevel server_level) {
				server_level.updateSleepingPlayerList();
			}
		});
		return false;
	}

	public static boolean modifyEntity( @NotNull MSCControllerBlockEntity msc, Object args ) {
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

	/*
	public static boolean TransfurEntity(@NotNull MSCControllerBlockEntity msc, Object args ) {
		if ( ! msc.ensureCapturedIsStillInside()) {
			return false;
		}

		msc.getChamberedEntity().ifPresent( entity -> {
			if ( TransfurVariant.getEntityVariant(entity) != null ) return;
			if ( !entity.getType)
		})
	}*/

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
