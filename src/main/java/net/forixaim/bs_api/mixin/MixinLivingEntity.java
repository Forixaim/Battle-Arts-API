package net.forixaim.bs_api.mixin;


import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyle;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import javax.annotation.Nullable;
import java.util.Objects;

@Mixin(value = LivingEntity.class)
public abstract class MixinLivingEntity
{
	@Shadow public abstract boolean hasEffect(MobEffect pEffect);

	@Shadow @Nullable public abstract MobEffectInstance getEffect(MobEffect pEffect);

	@Unique
	private final LivingEntity battle_arts$entity = (LivingEntity) (Object)this;

	@Inject(method = "getJumpBoostPower", at = @At("RETURN"), cancellable = true)
	private void ModifyJumpBoost(CallbackInfoReturnable<Float> info)
	{
		if (battle_arts$entity instanceof Player player)
		{
			PlayerPatch<?> playerPatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
			if (playerPatch != null && playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getSkill() instanceof BattleStyle battleStyle)
			{
				float basePower =  this.hasEffect(MobEffects.JUMP) ? 0.1F * ((float) Objects.requireNonNull(this.getEffect(MobEffects.JUMP)).getAmplifier() + 1.0F) : 0.0F;
				basePower += battleStyle.getJumpBoostPower();
				info.setReturnValue(basePower);
			}
		}
	}
}
