package net.forixaim.battle_arts_api.mixin;

import net.forixaim.battle_arts_api.Config;
import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.battle_arts_api.battle_arts_skills.battle_style.BattleStyle;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(value = CriticalHitEvent.class, remap = false)
public class MixinPlayerCritical
{
	@Unique
	CriticalHitEvent battleArtsAPI$self = (CriticalHitEvent) (Object) this;
	/**
	 * Disables critical hit damage modifier
	 */
	@Inject(method = "getDamageModifier", at = @At("HEAD"), cancellable = true, remap = false)
	private void getDamageModifier(CallbackInfoReturnable<Float> cir)
	{
		if (EpicFightCapabilities.getEntityPatch(battleArtsAPI$self.getEntity(), PlayerPatch.class) != null)
		{
			if (EpicFightCapabilities.getEntityPatch(battleArtsAPI$self.getEntity(), PlayerPatch.class).getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getSkill() instanceof BattleStyle battleStyle)
			{
				cir.setReturnValue(1.0F + battleStyle.getCriticalHitDamage());
			}
		}
		if (Config.randomCriticalHits)
			cir.setReturnValue(1.0F);

	}
}
