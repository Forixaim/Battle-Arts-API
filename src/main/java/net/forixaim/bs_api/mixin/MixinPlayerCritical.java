package net.forixaim.bs_api.mixin;

import net.forixaim.bs_api.Config;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CriticalHitEvent.class, remap = false)
public class MixinPlayerCritical
{
	/**
	 * Disables critical hit damage modifier
	 */
	@Inject(method = "getDamageModifier", at = @At("HEAD"), cancellable = true, remap = false)
	private void getDamageModifier(CallbackInfoReturnable<Float> cir)
	{
		if (Config.randomCriticalHits)
			cir.setReturnValue(1.0F);
	}
}
