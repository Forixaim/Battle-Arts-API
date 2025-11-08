package net.forixaim.battle_arts_api.mixin;

import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import yesman.epicfight.client.events.engine.ControlEngine;
import yesman.epicfight.skill.SkillSlot;

@Mixin(ControlEngine.class)
public interface ControlEngineInvoker
{
    @Invoker("reserveKey")
    void invokeReserveKey(SkillSlot slot, KeyMapping keyMapping);
}
