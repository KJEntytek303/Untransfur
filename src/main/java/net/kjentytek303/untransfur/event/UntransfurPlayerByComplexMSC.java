package net.kjentytek303.untransfur.event;

import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.TransfurEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UntransfurPlayerByComplexMSC extends TransfurEvents.UntransfurPlayerByBlockEvent {
	public UntransfurPlayerByComplexMSC(@NotNull BlockState blockState, @NotNull BlockPos blockPosition, @NotNull Player player, @NotNull TransfurVariantInstance<?> variantInstance, @Nullable TransfurVariant<?> originalNextVariant) {
		super(blockState, blockPosition, player, variantInstance, originalNextVariant);
	}
}