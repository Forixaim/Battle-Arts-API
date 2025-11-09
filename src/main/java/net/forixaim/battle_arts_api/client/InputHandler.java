package net.forixaim.battle_arts_api.client;

import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.battle_arts_api.client.input.action.BattleArtsInputActions;
import net.forixaim.battle_arts_api.client.input.handlers.InputManager;
import net.forixaim.battle_arts_api.mixin.ControlEngineInvoker;
import net.minecraft.client.Minecraft;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlot;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

public class InputHandler
{
    private static final boolean simpleInput = false;

    private static final LocalPlayerPatch localPlayerPatch = EpicFightCapabilities.getEntityPatch(Minecraft.getInstance().player, LocalPlayerPatch.class);
    public static void handleKeybinds()
    {
        if (localPlayerPatch != null)
        {
            if (simpleInput)
            {
                if (KeyBinds.USE_ART_1.consumeClick()) {
                    handleExecute(BattleArtsSkillSlots.COMBAT_ART);
                }
                else if (KeyBinds.USE_MANA_ART.consumeClick()) {
                    handleExecute(BattleArtsSkillSlots.SPECIAL_ART);
                }
                else if (KeyBinds.USE_BURST_ART.consumeClick()) {
                    handleExecute(BattleArtsSkillSlots.BURST_ART);
                }
                else if (KeyBinds.USE_ULTIMATE_ART.consumeClick()) {
                    handleExecute(BattleArtsSkillSlots.ULTIMATE_ART);
                }

            }
            else
            {
                InputManager.castActiveSkill(BattleArtsInputActions.COMBAT_ART, true, BattleArtsSkillSlots.COMBAT_ART, localPlayerPatch);
                InputManager.castActiveSkill(BattleArtsInputActions.MANA_ART, true, BattleArtsSkillSlots.SPECIAL_ART, localPlayerPatch);
                InputManager.castActiveSkill(BattleArtsInputActions.BURST_ART, true, BattleArtsSkillSlots.BURST_ART, localPlayerPatch);
                InputManager.castActiveSkill(BattleArtsInputActions.ULTIMATE_ART, true, BattleArtsSkillSlots.ULTIMATE_ART, localPlayerPatch);
            }

        }
    }

    public static void handleExecute(SkillSlot skillSlotConsumer)
    {
        SkillContainer activeSlot = localPlayerPatch.getSkill(skillSlotConsumer);
        if (activeSlot.sendCastRequest(localPlayerPatch, ClientEngine.getInstance().controlEngine).shouldReserveKey())
        {
            ((ControlEngineInvoker)ClientEngine.getInstance().controlEngine).invokeReserveKey(activeSlot.getSlot(), BattleArtsInputActions.COMBAT_ART.keyMapping());
        }
    }
}
