package net.forixaim.battle_arts_api.mixin;

import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.battle_arts_api.battle_arts_skills.battle_style.BattleStyle;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.registry.entries.EpicFightSkillDataKeys;
import yesman.epicfight.skill.*;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapability;

import java.util.List;
import java.util.Map;

@Mixin(value = CapabilityItem.class, remap = false)
public abstract class MixinCapabilityItem
{
	@Shadow public abstract Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>> getLivingMotionModifier(LivingEntityPatch<?> playerpatch, InteractionHand hand);

	@Inject(method = "getLivingMotionModifier", at = @At("HEAD"), remap = false, cancellable = true)
	public void getLivingMotionModifierEX(LivingEntityPatch<?> entityPatch, InteractionHand hand, final CallbackInfoReturnable<Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>>> cir)
	{
		if (entityPatch instanceof PlayerPatch<?> playerPatch && !(playerPatch.getHoldingItemCapability(InteractionHand.MAIN_HAND) instanceof WeaponCapability))
		{
			if (!playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).isEmpty() && playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getSkill() instanceof BattleStyle battleStyle)
			{
				if (battleStyle.modifiesUnarmedLMs() && playerPatch.getOriginal().getMainHandItem().is(Items.AIR))
					cir.setReturnValue(battleStyle.getUnarmedLivingMotions());
			}
		}
	}

	@Inject(method = "getGuardMotion", at = @At("RETURN"), remap = false, cancellable = true)
	public void getGuardEX(GuardSkill skill, GuardSkill.BlockType blockType, PlayerPatch<?> playerpatch, CallbackInfoReturnable<AnimationManager.AnimationAccessor<? extends StaticAnimation>> cir)
	{
		if (playerpatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getSkill() instanceof BattleStyle style)
		{
			if (style.getGuardMaps() != null && !style.getGuardMaps().isEmpty() && style.getGuardMaps().get(skill) != null &&!style.getGuardMaps().get(skill).isEmpty() && style.getGuardMaps().get(skill).containsKey(blockType))
			{
                List<AnimationManager.AnimationAccessor<? extends StaticAnimation>> animations = style.getGuardMaps().get(skill).get(blockType);
                SkillDataManager dataManager = playerpatch.getSkill(skill).getDataManager();

                if (animations != null && !animations.isEmpty())
                {
                    if (dataManager.hasData(EpicFightSkillDataKeys.PARRY_MOTION_COUNTER) && blockType == GuardSkill.BlockType.ADVANCED_GUARD)
                    {
                        int motionCounter = dataManager.getDataValue(EpicFightSkillDataKeys.PARRY_MOTION_COUNTER);
                        dataManager.setDataF(EpicFightSkillDataKeys.PARRY_MOTION_COUNTER, (v) -> v + 1);
                        motionCounter %= animations.size();
                        cir.setReturnValue(animations.get(motionCounter));
                    }
                    cir.setReturnValue(animations.get(playerpatch.getOriginal().getRandom().nextInt(animations.size())));
                }
			}
		}
	}
}
