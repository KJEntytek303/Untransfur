package net.kjentytek303.untransfur.mixin;

import net.kjentytek303.untransfur.util.ProcessFlinston;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@Inject(method = "tick", at=@At("HEAD"), remap = false)
	private void untransfur$livingEntityPreTick(CallbackInfo ci) {
		ProcessFlinston.tickDissolvement((LivingEntity)(Object)this);
	}
}