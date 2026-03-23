package net.forixaim.battle_arts_api.battle_arts_skills.active.combat_arts;


import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillCategories;
import net.forixaim.battle_arts_api.battle_arts_skills.active.ActiveSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;

import java.util.function.Function;

/**
 * This class isn't supposed to be used. It's mainly here for organization
 */

public abstract class CombatArt extends ActiveSkill
{
    public static <B extends SkillBuilder<B>> SkillBuilder<?> createCombatArt(Function<B, ? extends Skill> constructor)
    {
        return ActiveSkill.createActiveSkill(constructor).setCategory(BattleArtsSkillCategories.COMBAT_ART);
    }
	public CombatArt(SkillBuilder<?> builder) {
		super(builder);
	}
}
