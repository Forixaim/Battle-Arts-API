package net.forixaim.bs_api.mixin;

import com.yesman.epicskills.client.gui.screen.SkillTreeScreen;
import com.yesman.epicskills.world.capability.AbilityPoints;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import yesman.epicfight.world.capabilities.skill.CapabilitySkill;

@Mixin(SkillTreeScreen.class)
public interface SkillTreeScreenAccessor
{
    @Accessor(value = "playerSkills", remap = false)
    CapabilitySkill getPlayerSkills();

    @Accessor(value = "backgroundMode", remap = false)
    boolean getBackgroundMode();

    @Accessor(value = "playerAbilityPoints", remap = false)
    AbilityPoints getPlayerAbilityPoints();
}
