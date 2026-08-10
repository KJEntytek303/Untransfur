package net.kjentytek303.untransfur.msc;

import com.mojang.datafixers.util.Pair;
import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.ltxprogrammer.changed.entity.ModifiableEntity;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Predicate;


public class MSCScheduledCommand {
	private static final Map<String, Pair<Predicate<MSCControllerBlockEntity>, BiFunction<MSCControllerBlockEntity, CompoundTag, Boolean>>> COMMANDS = new HashMap<>();

	public static void addOrOverwrite( @NotNull String id, @NotNull Predicate<MSCControllerBlockEntity> start_condition, @NotNull BiFunction<MSCControllerBlockEntity, CompoundTag, Boolean> tick_func) {
		COMMANDS.put(id, new Pair<>( start_condition, tick_func));
	}

	public static boolean add(@NotNull String id, @NotNull Predicate<MSCControllerBlockEntity> start_condition, @NotNull BiFunction<MSCControllerBlockEntity, CompoundTag, Boolean> tick_func ) {
		return COMMANDS.putIfAbsent(id, new Pair<>(start_condition, tick_func)) == null;
	}

	public static void remove(@NotNull String id) {
		COMMANDS.remove( id );
	}

	public static @Nullable Predicate<MSCControllerBlockEntity> getPredicate(@NotNull String id) {
		if ( !COMMANDS.containsKey(id)) {
			return null;
		}
		return COMMANDS.get(id).getFirst();
	}

	public static @Nullable BiFunction<MSCControllerBlockEntity, CompoundTag, Boolean> getFunction(@NotNull String id) {
		if ( !COMMANDS.containsKey(id)) {
			return null;
		}
		return COMMANDS.get(id).getSecond();
	}

	public static class MSCDefaultCommands {
		public static boolean applyModifications(@NotNull MSCControllerBlockEntity msc, @NotNull CompoundTag modifications) {
			msc.getChamberedLatex().ifPresent( entity -> {
				msc.skip_modify = true;
				if( entity.getChangedEntity() instanceof ModifiableEntity modifiable ) {
					var vectors = modifiable.getModificationVectors();

					AtomicBoolean any_match = new AtomicBoolean(false);
					modifications.getAllKeys().forEach( key -> {
						if (!vectors.containsKey(key)) {
							return;
						}
						if( vectors.get(key).readFromTag(modifications.get(key))) {
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
	}
}