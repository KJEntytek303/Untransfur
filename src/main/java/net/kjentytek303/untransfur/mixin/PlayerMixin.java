package net.kjentytek303.untransfur.mixin;

import net.kjentytek303.untransfur.util.ProcessUntransfur;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Player.class)
public class PlayerMixin {
	@Inject(method = "tick", at=@At("HEAD"), remap = false)
	public void untransfur$tickUntfProgress(CallbackInfo ci) {
		ProcessUntransfur.tickPlayerUntransfurProgress((Player)(Object)this);
	}
}