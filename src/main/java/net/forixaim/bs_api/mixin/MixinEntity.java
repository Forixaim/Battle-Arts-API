package net.forixaim.bs_api.mixin;


import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyle;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(Entity.class)
public class MixinEntity
{
	@Unique
	private final Entity battle_arts$entity = (Entity) (Object)this;

	@Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
	private void isInvulnerableTo(DamageSource pSource, CallbackInfoReturnable<Boolean> cir)
	{
		if (!pSource.typeHolder().containsTag(DamageTypeTags.BYPASSES_INVULNERABILITY))
		{
			EntityPatch<?> entityPatch = EpicFightCapabilities.getEntityPatch(battle_arts$entity, EntityPatch.class);
			if (entityPatch instanceof PlayerPatch<?> playerPatch)
			{
				if (!playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).isEmpty() && playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getSkill() instanceof BattleStyle battleStyle)
				{
					for (ResourceKey<DamageType> damageType : battleStyle.getImmuneDamages())
					{
						if (pSource.is(damageType))
						{
							cir.setReturnValue(true);
						}
					}
					for (TagKey<DamageType> damageType : battleStyle.getImmuneModdedDamages())
					{
						if (pSource.is(damageType))
						{
							cir.setReturnValue(true);
						}
					}
				}
			}
		}
	}
}
