package net.forixaim.bs_api.mixin;

import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.bs_api.client.KeyBinds;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.client.input.EpicFightKeyMappings;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlot;

@Mixin(value = ControllEngine.class, remap = false)
public abstract class MixinControlEngine
{
	@Unique
	ControllEngine epic_fight_battle_styles$controlEngine = null;

	@Shadow(remap = false) private LocalPlayerPatch playerpatch;

	@Shadow(remap = false) private KeyMapping currentChargingKey;

	@Shadow(remap = false) protected abstract void reserveKey(SkillSlot slot, KeyMapping keyMapping);


    @Inject(method = "<init>", at = @At("RETURN"))
	public void ConHead(CallbackInfo ci)
	{
		epic_fight_battle_styles$controlEngine = (ControllEngine) (Object)this;
	}


	/**
	 * @author Forixaim
	 * @reason To make aerials more strict.
	 */
	@Inject(method = "handleEpicFightKeyMappings", at = @At("HEAD"), remap = false)
	public void handleEpicFightKeyMappings(CallbackInfo callbackInfo)
	{
		while (KeyBinds.USE_ART_1.consumeClick())
		{
			if (this.playerpatch.isBattleMode() && this.currentChargingKey != KeyBinds.USE_ART_1)
			{
				if (!EpicFightKeyMappings.ATTACK.getKey().equals(KeyBinds.USE_ART_1.getKey()))
				{
					SkillContainer skill = this.playerpatch.getSkill(BattleArtsSkillSlots.COMBAT_ART);
					if (epic_fight_battle_styles$executeRequest(skill))
					{
						this.reserveKey(BattleArtsSkillSlots.COMBAT_ART, KeyBinds.USE_ART_1);
					}
				}
			}
		}
		while (KeyBinds.USE_TAUNT.consumeClick())
		{
			if (this.playerpatch.isBattleMode() && this.currentChargingKey != KeyBinds.USE_TAUNT)
			{
				if (!EpicFightKeyMappings.ATTACK.getKey().equals(KeyBinds.USE_TAUNT.getKey()))
				{
					SkillContainer skill = this.playerpatch.getSkill(BattleArtsSkillSlots.TAUNT);
					if (epic_fight_battle_styles$executeRequest(skill))
					{
						this.reserveKey(BattleArtsSkillSlots.TAUNT, KeyBinds.USE_TAUNT);
					}
				}
			}
		}
		while (KeyBinds.USE_ULTIMATE_ART.consumeClick())
		{
			if (this.playerpatch.isBattleMode() && this.currentChargingKey != KeyBinds.USE_ULTIMATE_ART)
			{
				if (!EpicFightKeyMappings.ATTACK.getKey().equals(KeyBinds.USE_ULTIMATE_ART.getKey()))
				{
					SkillContainer skill = this.playerpatch.getSkill(BattleArtsSkillSlots.ULTIMATE_ART);
					if (epic_fight_battle_styles$executeRequest(skill))
					{
						this.reserveKey(BattleArtsSkillSlots.ULTIMATE_ART, KeyBinds.USE_ULTIMATE_ART);
					}
				}
			}
		}
		while (KeyBinds.USE_BURST_ART.consumeClick())
		{
			if (this.playerpatch.isBattleMode() && this.currentChargingKey != KeyBinds.USE_BURST_ART)
			{
				if (!EpicFightKeyMappings.ATTACK.getKey().equals(KeyBinds.USE_BURST_ART.getKey()))
				{
					SkillContainer skill = this.playerpatch.getSkill(BattleArtsSkillSlots.BURST_ART);
					if (epic_fight_battle_styles$executeRequest(skill))
					{
						this.reserveKey(BattleArtsSkillSlots.BURST_ART, KeyBinds.USE_BURST_ART);
					}
				}
			}
		}
	}



	@Unique
	private boolean epic_fight_battle_styles$executeRequest(SkillContainer skill) {
		return skill.sendExecuteRequest(this.playerpatch, epic_fight_battle_styles$controlEngine).shouldReserverKey();
	}
}
