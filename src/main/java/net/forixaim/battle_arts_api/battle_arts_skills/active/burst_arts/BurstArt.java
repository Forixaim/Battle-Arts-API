package net.forixaim.battle_arts_api.battle_arts_skills.active.burst_arts;


import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillCategories;
import net.forixaim.battle_arts_api.battle_arts_skills.active.ActiveSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;

import java.util.function.Function;

/**
 * This class isn't supposed to be used. It's mainly here for organization
 */

public abstract class BurstArt extends ActiveSkill
{
    public static <B extends SkillBuilder<B>> SkillBuilder<?> createBurstArt(Function<B, ? extends Skill> constructor)
    {
        return ActiveSkill.createActiveSkill(constructor).setCategory(BattleArtsSkillCategories.BURST_ART);
    }
    public BurstArt(SkillBuilder<?> builder) {
        super(builder);
    }
}
