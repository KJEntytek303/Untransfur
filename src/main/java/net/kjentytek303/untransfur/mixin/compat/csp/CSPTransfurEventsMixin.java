package net.kjentytek303.untransfur.mixin.compat.csp;

import net.kjentytek303.untransfur.event.UntransfurPlayerByComplexMSC;
import net.ltxprogrammer.changed.extension.RequiredMods;
import net.ltxprogrammer.changed.process.TransfurEvents;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import net.stonenibbler.changed_survive_protocol.common.event.CSPTransfurEvents;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.UUID;


@Mixin(value = CSPTransfurEvents.class, remap = false)
@RequiredMods("changed_survive_protocol")
public class CSPTransfurEventsMixin {

	@Shadow
	private static void tryCompleteCommandUntransfurReset(ServerPlayer player, String reason) {throw new UnsupportedOperationException("Implemented via mixin");}

	@Final
	@Shadow
	private static Set<UUID> COMMAND_UNTRANSFUR_RESETS;

	@Inject(method = "onUntransfurPlayer", at=@At("HEAD"), remap = false, cancellable = true)
	private static void UntransfurCSPCompat_onUntransfurPlayer(TransfurEvents.UntransfurPlayerEvent event, CallbackInfo ci) {
		if(event instanceof UntransfurPlayerByComplexMSC) {
			if(event.player instanceof ServerPlayer player) {
				COMMAND_UNTRANSFUR_RESETS.add(player.getUUID());
				tryCompleteCommandUntransfurReset(player, "untransfur:msc_complex");
				ci.cancel();

			}
		}
	}
}
