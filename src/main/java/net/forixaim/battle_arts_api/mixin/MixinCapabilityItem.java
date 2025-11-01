package net.forixaim.battle_arts_api.mixin;

import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.battle_arts_api.battle_arts_skills.battle_style.BattleStyle;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.server.SPChangeSkill;
import yesman.epicfight.network.server.SPSetRemotePlayerSkill;
import yesman.epicfight.network.server.SPSetSkillContainerValue;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapability;

import java.util.List;
import java.util.Map;

@Mixin(value = CapabilityItem.class, remap = false)
public abstract class MixinCapabilityItem
{
	@Shadow public abstract Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>> getLivingMotionModifier(LivingEntityPatch<?> playerpatch, InteractionHand hand);

	@Unique
	private final CapabilityItem battleArtsAPI$inst = (CapabilityItem) (Object) this;

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
				cir.setReturnValue(style.getGuardMaps().get(skill).get(blockType));
			}
		}
	}

	@Inject(method = "changeWeaponInnateSkill", at = @At("RETURN"), remap = false)
	public void changeWeaponInnate(PlayerPatch<?> playerPatch, ItemStack itemstack, CallbackInfo ci)
	{
		EpicFightNetworkManager.PayloadBundleBuilder toLocal = EpicFightNetworkManager.PayloadBundleBuilder.create();
		EpicFightNetworkManager.PayloadBundleBuilder toRemote = EpicFightNetworkManager.PayloadBundleBuilder.create();
		if (!(playerPatch.getHoldingItemCapability(InteractionHand.MAIN_HAND) instanceof WeaponCapability) && playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getSkill() instanceof BattleStyle bs && bs.unarmedMoveset())
		{
			Skill weaponInnateSkill = null;
			Skill skill = null;
			if (!playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).isEmpty() && playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getSkill() instanceof BattleStyle battleStyle)
			{
				weaponInnateSkill = battleStyle.getUnarmedInnateSkill();
				skill = battleStyle.getUnarmedPassiveSkill();
			}

			if (bs.modifiesUnarmedLMs() && !playerPatch.isLogicalClient())
			{
				((ServerPlayerPatch) playerPatch).modifyLivingMotionByCurrentItem();
			}

            SkillContainer weaponInnateSkillContainer = playerPatch.getSkill(SkillSlots.WEAPON_INNATE);
			if (weaponInnateSkill != null)
			{
				if (weaponInnateSkillContainer.getSkill() != weaponInnateSkill)
				{
					weaponInnateSkillContainer.setSkill(weaponInnateSkill);
				}
				toLocal.and(new SPChangeSkill(SkillSlots.WEAPON_INNATE, playerPatch.getOriginal().getId(), weaponInnateSkill));
			}
			else
			{
				toLocal.and(SPSetSkillContainerValue.enable(SkillSlots.WEAPON_INNATE, false, playerPatch.getOriginal().getId()));
			}


			weaponInnateSkillContainer.setDisabled(weaponInnateSkill == null);
			toRemote.and(new SPSetRemotePlayerSkill(playerPatch.getOriginal().getId(), SkillSlots.WEAPON_INNATE, weaponInnateSkill));


			SkillContainer passiveSkillContainer = playerPatch.getSkill(SkillSlots.WEAPON_PASSIVE);
			if (skill != null) {
				if (passiveSkillContainer.getSkill() != skill) {
					passiveSkillContainer.setSkill(skill);
					toLocal.and(new SPChangeSkill(SkillSlots.WEAPON_PASSIVE, playerPatch.getOriginal().getId(), skill));
					toRemote.and(new SPSetRemotePlayerSkill(playerPatch.getOriginal().getId(), SkillSlots.WEAPON_PASSIVE, skill));				}
			} else {
				passiveSkillContainer.setSkill(null);
				toLocal.and(new SPChangeSkill(SkillSlots.WEAPON_PASSIVE, playerPatch.getOriginal().getId(), null));
				toRemote.and(new SPSetRemotePlayerSkill(playerPatch.getOriginal().getId(), SkillSlots.WEAPON_PASSIVE, skill));			}
		}
	}
}
