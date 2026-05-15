package net.forixaim.battle_arts_api.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import yesman.epicfight.api.client.input.action.InputAction;
import yesman.epicfight.client.events.engine.ControlEngine;
import yesman.epicfight.skill.SkillSlot;

@Mixin(ControlEngine.class)
public interface ControlEngineInvoker
{
    @Invoker(value = "reserveKey", remap = false)
    void invokeReserveKey(SkillSlot slot, InputAction keyMapping);
}
