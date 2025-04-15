package net.forixaim.bs_api.battle_arts_skills.active.ultimate_arts;


import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillCategories;
import net.forixaim.bs_api.battle_arts_skills.active.ActiveSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;

/**
 * This class isn't supposed to be used. It's mainly here for organization
 */

public abstract class UltimateArt extends ActiveSkill
{

	public static SkillBuilder<UltimateArt> createUltimateArt()
	{
		return (new SkillBuilder<UltimateArt>().setCategory(BattleArtsSkillCategories.ULTIMATE_ART).setResource(Resource.COOLDOWN));
	}

	public UltimateArt(SkillBuilder<? extends UltimateArt> builder) {
		super(builder);
	}

}
