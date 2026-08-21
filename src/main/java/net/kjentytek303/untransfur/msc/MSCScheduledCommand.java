package net.kjentytek303.untransfur.msc;

import com.mojang.datafixers.util.Pair;
import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;


public class MSCScheduledCommand {
	private static final Map<String, Pair<Predicate<MSCControllerBlockEntity>, BiFunction<MSCControllerBlockEntity, Object, Boolean>>> COMMANDS = new HashMap<>();

	public static boolean contains(String s) {
		return COMMANDS.containsKey(s);
	}

	public static void addOrOverwrite( @NotNull String id, @NotNull Predicate<MSCControllerBlockEntity> start_condition, @NotNull BiFunction<MSCControllerBlockEntity, Object, Boolean> tick_func) {
		COMMANDS.put(id, new Pair<>( start_condition, tick_func));
	}

	public static boolean add(@NotNull String id, @NotNull Predicate<MSCControllerBlockEntity> start_condition, @NotNull BiFunction<MSCControllerBlockEntity, Object, Boolean> tick_func ) {
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

	public static @Nullable BiFunction<MSCControllerBlockEntity, Object, Boolean> getFunction(@NotNull String id) {
		if ( !COMMANDS.containsKey(id)) {
			return null;
		}
		return COMMANDS.get(id).getSecond();
	}

	static {
		add( "untransfur:open",
			msc -> !msc.isOpen() && msc.isDrained(),
			MSCDefaultCommands::openDoor
		);
		add( "untransfur:capture_entity",
			msc -> msc.isDrained() && msc.isOpen(),
			MSCDefaultCommands::captureEntity
		);
		add( "untransfur:close", MSCControllerBlockEntity::isOpen, MSCDefaultCommands::closeDoor);
		add( "untransfur:fill",
			msc -> !msc.isOpen() && msc.isFilled() && msc.getFluidType().isPresent(),
			MSCDefaultCommands::fillChamber
		);
		add( "untransfur:stabilize_entity",
			msc -> msc.isFilled() && msc.hasEntity(),
			MSCDefaultCommands::stabilizeEntity
		);
		add( "untransfur:wake_entity",
			msc -> msc.isFilled() && msc.hasEntity() && msc.isStabilized(),
			MSCDefaultCommands::wakeEntity
		);
		add( "untransfur:modify_entity",
			msc -> msc.isFilled() && msc.getChamberedLatex().isPresent(),
			MSCDefaultCommands::modifyEntity
		);

	}
}