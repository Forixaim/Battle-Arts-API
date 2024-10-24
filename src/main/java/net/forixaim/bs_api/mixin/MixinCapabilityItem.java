package net.forixaim.bs_api.mixin;

import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyle;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.animation.AnimationProvider;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

import java.util.List;
import java.util.Map;

@Mixin(value = CapabilityItem.class, remap = false)
public abstract class MixinCapabilityItem
{
	@Inject(method = "getLivingMotionModifier", at = @At("RETURN"), remap = false, cancellable = true)
	public void getLivingMotionModifier(LivingEntityPatch<?> entityPatch, InteractionHand hand, final CallbackInfoReturnable<Map<LivingMotion, AnimationProvider<?>>> cir)
	{
		if (entityPatch instanceof PlayerPatch<?> playerPatch)
		{
			if (!playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).isEmpty() && playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getSkill() instanceof BattleStyle battleStyle)
			{
				if (battleStyle.modifiesUnarmedLMs())
					cir.setReturnValue(battleStyle.getUnarmedLivingMotions());
			}
		}
	}

	@Inject(method = "getAutoAttckMotion", at = @At("HEAD"), remap = false, cancellable = true)
	public void getAutoAttackMotion(PlayerPatch<?> playerPatch, CallbackInfoReturnable<List<AnimationProvider<?>>> cir)
	{
		if (!playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).isEmpty() && playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getSkill() instanceof BattleStyle battleStyle)
		{
			if (battleStyle.modifiesUnarmedAttacks())
				cir.setReturnValue(battleStyle.getUnarmedAttackAnimations());
		}
	}
}
