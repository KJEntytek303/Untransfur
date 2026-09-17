package net.kjentytek303.untransfur.msc;

import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Predicate;


public abstract class MSCScheduledCommand implements BiFunction<MSCControllerBlockEntity, ItemStack, Boolean>, Predicate<MSCControllerBlockEntity> {
	public MSCScheduledCommand(@NotNull ResourceLocation id ) {
		this.command_id = id;
	}
	public final ResourceLocation command_id;

	/**
	 * This function is ran by the MSC every tick.
	 * @param bentity - Command executor.
	 * @param argument - ItemStack argument provided by the Advanced Logic Adapter.
	 *                 - Expect this to be ItemStack.EMPTY, as it is the default
	 * @return - Returns true if the command didn't finish.
	 * 		Returns false when command finished and next ScheduledCommand should be run.
	 */

	public abstract Boolean apply(MSCControllerBlockEntity bentity, ItemStack argument);

	/**
	 * This function is ran by the MSC before deciding whether it should run the ScheduledCommand,
	 * or evict it from the queue and take damage from unfulfilled predicates.
	 * @param bentity - Host of the command.
	 * @return should MSC run the command? This is assumed to increase bentity failure chance.
	 */
	public abstract boolean test(MSCControllerBlockEntity bentity );
}