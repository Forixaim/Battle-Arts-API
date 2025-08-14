package net.forixaim.battle_arts_api.mixin;

import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.battle_arts_api.battle_arts_skills.battle_style.BattleStyle;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(value = EnchantmentHelper.class)
public abstract class MixinEnchant
{
	//Base sneaking speed is 0.3F
	@Inject(method = "getSneakingSpeedBonus", at = @At("RETURN"), cancellable = true)
	private static void getSneaky(LivingEntity entity, final CallbackInfoReturnable<Float> cir)
	{
		LivingEntityPatch<?> entityPatch = EpicFightCapabilities.getEntityPatch(entity, LivingEntityPatch.class);
		if (entityPatch instanceof PlayerPatch<?> playerPatch)
		{
			if (playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getSkill() instanceof BattleStyle battleStyle)
			{
				if (battleStyle.getSneakIsDisabledKey() != null)
				{
					if (playerPatch.getSkill(battleStyle).getDataManager().getDataValue(battleStyle.getSneakIsDisabledKey()))
					{
						cir.setReturnValue(-0.3F);
					}
				}
			}
		}
	}
}
