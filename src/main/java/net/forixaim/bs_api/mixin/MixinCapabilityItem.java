package net.forixaim.bs_api.mixin;

import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyle;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.animation.AnimationProvider;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.server.SPChangeSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
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
	@Unique
	private final CapabilityItem battleArtsAPI$inst = (CapabilityItem) (Object) this;

	@Inject(method = "getLivingMotionModifier", at = @At("RETURN"), remap = false, cancellable = true)
	public void getLivingMotionModifier(LivingEntityPatch<?> entityPatch, InteractionHand hand, final CallbackInfoReturnable<Map<LivingMotion, AnimationProvider<?>>> cir)
	{
		if (entityPatch instanceof PlayerPatch<?> playerPatch)
		{
			if (!playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).isEmpty() && playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getSkill() instanceof BattleStyle battleStyle)
			{
				if (battleStyle.modifiesUnarmedLMs())
					cir.setReturnValue(battleStyle.getUnarmedLivingMotions());
				if (battleStyle.modifiesUnarmedBMs() && playerPatch.isBattleMode())
					cir.setReturnValue(battleStyle.getUnarmedBattleMotions());
			}
		}
	}


	@Inject(method = "changeWeaponInnateSkill", at = @At("RETURN"), remap = false)
	public void changeWeaponInnate(PlayerPatch<?> playerPatch, ItemStack itemstack, CallbackInfo ci)
	{
		Skill weaponInnateSkill = null;
		Skill skill = null;
		if (!playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).isEmpty() && playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getSkill() instanceof BattleStyle battleStyle)
		{
			weaponInnateSkill = battleStyle.getUnarmedInnateSkill();
			skill = battleStyle.getUnarmedPassiveSkill();
		}

		String skillName = "";
		SPChangeSkill.State state = SPChangeSkill.State.ENABLE;
		SkillContainer weaponInnateSkillContainer = playerPatch.getSkill(SkillSlots.WEAPON_INNATE);
		if (weaponInnateSkill != null)
		{
			if (weaponInnateSkillContainer.getSkill() != weaponInnateSkill)
			{
				weaponInnateSkillContainer.setSkill(weaponInnateSkill);
			}

			skillName = weaponInnateSkill.toString();
		}
		else
		{
			state = SPChangeSkill.State.DISABLE;
		}


		if (playerPatch instanceof ServerPlayerPatch serverPlayerPatch)
		{
			if (!(serverPlayerPatch.getHoldingItemCapability(InteractionHand.MAIN_HAND) instanceof WeaponCapability))
				serverPlayerPatch.modifyLivingMotionByCurrentItem(false);
		}


		weaponInnateSkillContainer.setDisabled(weaponInnateSkill == null);
		EpicFightNetworkManager.sendToPlayer(new SPChangeSkill(SkillSlots.WEAPON_INNATE, skillName, state), (ServerPlayer)playerPatch.getOriginal());


		SkillContainer passiveSkillContainer = playerPatch.getSkill(SkillSlots.WEAPON_PASSIVE);
		if (skill != null) {
			if (passiveSkillContainer.getSkill() != skill) {
				passiveSkillContainer.setSkill(skill);
				EpicFightNetworkManager.sendToPlayer(new SPChangeSkill(SkillSlots.WEAPON_PASSIVE, skill.toString(), SPChangeSkill.State.ENABLE), (ServerPlayer)playerPatch.getOriginal());
			}
		} else {
			passiveSkillContainer.setSkill(null);
			EpicFightNetworkManager.sendToPlayer(new SPChangeSkill(SkillSlots.WEAPON_PASSIVE, "empty", SPChangeSkill.State.ENABLE), (ServerPlayer)playerPatch.getOriginal());
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
