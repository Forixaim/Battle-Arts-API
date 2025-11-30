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
                InputManager.triggerOnPress(BattleArtsInputAction.COMBAT_ART, () -> handleExecute(BattleArtsSkillSlots.COMBAT_ART));

                InputManager.triggerOnPress(BattleArtsInputAction.MANA_ART, () -> handleExecute(BattleArtsSkillSlots.SPECIAL_ART));

                InputManager.triggerOnPress(BattleArtsInputAction.BURST_ART, () -> handleExecute(BattleArtsSkillSlots.BURST_ART));

                InputManager.triggerOnPress(BattleArtsInputAction.ULTIMATE_ART, () -> handleExecute(BattleArtsSkillSlots.ULTIMATE_ART));

            }
            else
            {
                castActiveSkill(BattleArtsInputAction.COMBAT_ART, BattleArtsSkillSlots.COMBAT_ART, localPlayerPatch);
                castActiveSkill(BattleArtsInputAction.MANA_ART, BattleArtsSkillSlots.SPECIAL_ART, localPlayerPatch);
                castActiveSkill(BattleArtsInputAction.BURST_ART, BattleArtsSkillSlots.BURST_ART, localPlayerPatch);
                castActiveSkill(BattleArtsInputAction.ULTIMATE_ART, BattleArtsSkillSlots.ULTIMATE_ART, localPlayerPatch);
            }

        }
    }

    public static void handleExecute(SkillSlot skillSlotConsumer)
    {
        SkillContainer activeSlot = localPlayerPatch.getSkill(skillSlotConsumer);
        if (activeSlot.sendCastRequest(localPlayerPatch, ClientEngine.getInstance().controlEngine).shouldReserveKey())
        {
            ((ControlEngineInvoker)ClientEngine.getInstance().controlEngine).invokeReserveKey(activeSlot.getSlot(), BattleArtsInputAction.COMBAT_ART.keyMapping());
        }
    }

    private static void castActiveSkill(BattleArtsInputAction action, SkillSlot skillSlotConsumer, LocalPlayerPatch localPlayerPatch) {
        Runnable castActiveSkill = () -> {
            SkillContainer activeSlot = localPlayerPatch.getSkill(skillSlotConsumer);
            if (activeSlot.sendCastRequest(localPlayerPatch, ClientEngine.getInstance().controlEngine).shouldReserveKey()) {
                ((ControlEngineInvoker) ClientEngine.getInstance().controlEngine).invokeReserveKey(activeSlot.getSlot(), BattleArtsInputAction.COMBAT_ART.keyMapping());
            }
        };
        InputManager.triggerOnPress(action, castActiveSkill);
    }
}
