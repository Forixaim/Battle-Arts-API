package net.forixaim.bs_api.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.bs_api.client.KeyBinds;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.client.event.InputEvent;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.client.input.EpicFightKeyMappings;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlot;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(value = ControllEngine.class, remap = false)
public abstract class MixinControlEngine
{
	@Unique
	ControllEngine battle_arts$controlEngine = null;
	@Unique
	private boolean manaExecuting = false;

	@Shadow(remap = false) private LocalPlayerPatch playerpatch;

	@Shadow(remap = false) private KeyMapping currentChargingKey;

	@Shadow(remap = false) protected abstract void reserveKey(SkillSlot slot, KeyMapping keyMapping);


	@Shadow private LocalPlayer player;

	@Shadow public abstract void lockHotkeys();

	@Inject(method = "<init>", at = @At("RETURN"))
	public void ConHead(CallbackInfo ci)
	{
		battle_arts$controlEngine = (ControllEngine) (Object)this;
	}


	@Unique
	private static boolean battle_arts$keyPressed(KeyMapping key, boolean eventCheck) {
		boolean consumes = key.consumeClick();

		if (consumes && eventCheck) {
			int mouseButton = InputConstants.Type.MOUSE == key.getKey().getType() ? key.getKey().getValue() : -1;
			InputEvent.InteractionKeyMappingTriggered inputEvent = net.minecraftforge.client.ForgeHooksClient.onClickInput(mouseButton, key, InteractionHand.MAIN_HAND);

			if (inputEvent.isCanceled()) {
				return false;
			}
		}

		return consumes;
	}

	/**
	 * @author Forixaim
	 * @reason To make aerials more strict.
	 */
	@Inject(method = "handleEpicFightKeyMappings", at = @At("HEAD"), remap = false)
	public void handleEpicFightKeyMappings(CallbackInfo callbackInfo)
	{
		while (battle_arts$keyPressed(KeyBinds.USE_ART_1, true))
		{
			if (this.playerpatch.isEpicFightMode() && this.currentChargingKey != KeyBinds.USE_ART_1)
			{
				if (!EpicFightKeyMappings.ATTACK.getKey().equals(KeyBinds.USE_ART_1.getKey()))
				{
					SkillContainer skill = this.playerpatch.getSkill(BattleArtsSkillSlots.COMBAT_ART);
					if (battle_arts$executeRequest(skill))
					{
						this.reserveKey(BattleArtsSkillSlots.COMBAT_ART, KeyBinds.USE_ART_1);
					}
				}
			}
		}
		while (battle_arts$keyPressed(KeyBinds.USE_MANA_ART, true))
		{
			if (this.playerpatch.isEpicFightMode() && this.currentChargingKey != KeyBinds.USE_MANA_ART) {
				if (!EpicFightKeyMappings.ATTACK.getKey().equals(KeyBinds.USE_MANA_ART.getKey())) {
					if (this.playerpatch.getSkill(BattleArtsSkillSlots.MANA_ART).sendExecuteRequest(this.playerpatch, battle_arts$controlEngine).shouldReserverKey()) {
						if (!this.player.isSpectator()) {
							this.reserveKey(BattleArtsSkillSlots.MANA_ART, KeyBinds.USE_MANA_ART);
						}
					} else {
						this.lockHotkeys();
					}
				}
			}
		}
		while (battle_arts$keyPressed(KeyBinds.USE_ULTIMATE_ART, true))
		{
			if (this.playerpatch.isEpicFightMode() && this.currentChargingKey != KeyBinds.USE_ULTIMATE_ART)
			{
				if (!EpicFightKeyMappings.ATTACK.getKey().equals(KeyBinds.USE_ULTIMATE_ART.getKey()))
				{
					SkillContainer skill = this.playerpatch.getSkill(BattleArtsSkillSlots.ULTIMATE_ART);
					if (battle_arts$executeRequest(skill))
					{
						this.reserveKey(BattleArtsSkillSlots.ULTIMATE_ART, KeyBinds.USE_ULTIMATE_ART);
					}
				}
			}
		}
		while (battle_arts$keyPressed(KeyBinds.USE_BURST_ART, true))
		{
			if (this.playerpatch.isEpicFightMode() && this.currentChargingKey != KeyBinds.USE_BURST_ART)
			{
				if (!EpicFightKeyMappings.ATTACK.getKey().equals(KeyBinds.USE_BURST_ART.getKey()))
				{
					SkillContainer skill = this.playerpatch.getSkill(BattleArtsSkillSlots.BURST_ART);
					if (battle_arts$executeRequest(skill))
					{
						this.reserveKey(BattleArtsSkillSlots.BURST_ART, KeyBinds.USE_BURST_ART);
					}
				}
			}
		}
	}

	@Inject(method = "inputTick", at = @At("HEAD"))
	public void tick(CallbackInfo ci)
	{
		if (!KeyBinds.USE_MANA_ART.isDown())
		{
			manaExecuting = false;
		}
	}


	@Unique
	private boolean battle_arts$executeRequest(SkillContainer skill) {
		return skill.sendExecuteRequest(this.playerpatch, battle_arts$controlEngine).shouldReserverKey();
	}
}
