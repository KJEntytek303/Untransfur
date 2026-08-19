package net.kjentytek303.untransfur.event;

import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.TransfurEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class UntransfurPlayerByEffectEvent extends TransfurEvents.UntransfurPlayerEvent {

	public final MobEffect effect;
	public UntransfurPlayerByEffectEvent(@NotNull Player player, @NotNull TransfurVariantInstance<?> variantInstance, @Nullable TransfurVariant<?> originalNextVariant, MobEffect effect) {
		super(player, variantInstance, originalNextVariant);
		this.effect = effect;
	}
}
