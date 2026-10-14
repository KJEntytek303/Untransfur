package net.kjentytek303.untransfur.mixin.compat.csp;

import net.kjentytek303.untransfur.event.UntransfurPlayerByComplexMSC;
import net.ltxprogrammer.changed.extension.RequiredMods;
import net.ltxprogrammer.changed.process.TransfurEvents;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import net.stonenibbler.changed_survive_protocol.common.event.CSPTransfurEvents;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(value = CSPTransfurEvents.class, remap = false)
@RequiredMods("csp")
public class CSPTransfurEventsMixin {

	@Shadow
	private static void tryCompleteCommandUntransfurReset(ServerPlayer player, String reason) {throw new UnsupportedOperationException("Implemented via mixin");}

	@Inject(method = "onUntransfurPlayer", at = @At("HEAD"), remap = false, cancellable = true)
	public static void UntransfurCSPCompat_onUntransfurPlayer(TransfurEvents.UntransfurPlayerEvent event, CallbackInfo ci) {
		if(!(event.player instanceof ServerPlayer player)) {
			return;
		}
		if( event instanceof UntransfurPlayerByComplexMSC) {
			tryCompleteCommandUntransfurReset(player, "complex msc");
			ci.cancel();
		}
	}
}
