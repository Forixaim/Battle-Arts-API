package net.forixaim.battle_arts_api.client;

import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.battle_arts_api.client.input.action.BattleArtsInputAction;
import net.forixaim.battle_arts_api.mixin.ControlEngineInvoker;
import net.minecraft.client.Minecraft;
import yesman.epicfight.api.client.input.InputManager;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.events.engine.ControlEngine;
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
            castActiveSkill(BattleArtsInputAction.COMBAT_ART, BattleArtsSkillSlots.COMBAT_ART, localPlayerPatch);
            castActiveSkill(BattleArtsInputAction.MANA_ART, BattleArtsSkillSlots.SPECIAL_ART, localPlayerPatch);
            castActiveSkill(BattleArtsInputAction.BURST_ART, BattleArtsSkillSlots.BURST_ART, localPlayerPatch);
            castActiveSkill(BattleArtsInputAction.ULTIMATE_ART, BattleArtsSkillSlots.ULTIMATE_ART, localPlayerPatch);
        }
    }

    private static void castActiveSkill(BattleArtsInputAction action, SkillSlot skillSlotConsumer, LocalPlayerPatch localPlayerPatch) {
        Runnable castActiveSkill = () -> {
            SkillContainer activeSlot = localPlayerPatch.getSkill(skillSlotConsumer);
            if (activeSlot.sendCastRequest(localPlayerPatch, ControlEngine.getInstance()).shouldReserveKey()) {
                ((ControlEngineInvoker) ControlEngine.getInstance()).invokeReserveKey(activeSlot.getSlot(), BattleArtsInputAction.COMBAT_ART.keyMapping());
            }
        };
        InputManager.triggerOnPress(action, castActiveSkill);
    }
}
