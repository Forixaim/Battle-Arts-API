package net.forixaim.battle_arts_api.client;

import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.battle_arts_api.client.input.action.BattleArtsInputAction;
import net.forixaim.battle_arts_api.mixin.ControlEngineInvoker;
import net.minecraft.client.Minecraft;
import yesman.epicfight.api.client.input.InputManager;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlot;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

/**
 * Handles input for battle arts skills.
 * @author Forixaim
 * @author EchoEllet
 */
public class InputHandler
{
    private static final LocalPlayerPatch localPlayerPatch = EpicFightCapabilities.getEntityPatch(Minecraft.getInstance().player, LocalPlayerPatch.class);
    public static void handleKeybinds()
    {
        if (localPlayerPatch != null)
        {
            castActiveSkill(BattleArtsInputAction.COMBAT_ART, BattleArtsSkillSlots.COMBAT_ART);
            castActiveSkill(BattleArtsInputAction.MANA_ART, BattleArtsSkillSlots.SPECIAL_ART);
            castActiveSkill(BattleArtsInputAction.BURST_ART, BattleArtsSkillSlots.BURST_ART);
            castActiveSkill(BattleArtsInputAction.ULTIMATE_ART, BattleArtsSkillSlots.ULTIMATE_ART);
        }
    }

    private static void castActiveSkill(BattleArtsInputAction action, SkillSlot skillSlotConsumer) {
        if (localPlayerPatch == null)
            return;
        Runnable castActiveSkill = () -> {
            SkillContainer activeSlot = InputHandler.localPlayerPatch.getSkill(skillSlotConsumer);
            if (activeSlot.sendCastRequest(InputHandler.localPlayerPatch, ClientEngine.getInstance().controlEngine).shouldReserveKey() && ClientEngine.getInstance().controlEngine instanceof ControlEngineInvoker invoker) {
                invoker.invokeReserveKey(activeSlot.getSlot(), action.keyMapping());
            }

        };
        InputManager.triggerOnPress(action, castActiveSkill);
    }
}
