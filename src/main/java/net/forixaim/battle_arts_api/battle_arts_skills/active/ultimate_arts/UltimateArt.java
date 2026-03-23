package net.forixaim.battle_arts_api.battle_arts_skills.active.ultimate_arts;


import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillCategories;
import net.forixaim.battle_arts_api.battle_arts_skills.active.ActiveSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;

import java.util.function.Function;

/**
 * This class isn't supposed to be used. It's mainly here for organization
 */

public abstract class UltimateArt extends ActiveSkill
{
	public UltimateArt(SkillBuilder<?> builder) {
		super(builder);
	}
    public static <B extends SkillBuilder<B>> SkillBuilder<?> createUltimateArt(Function<B, ? extends Skill> constructor)
    {
        return ActiveSkill.createActiveSkill(constructor).setCategory(BattleArtsSkillCategories.ULTIMATE_ART);
    }
}
